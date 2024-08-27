package com.bawnorton.bettertrims.mixin.attributes.echoing;

import com.bawnorton.bettertrims.effect.EchoShardTrimEffect;
import com.bawnorton.bettertrims.networking.packet.s2c.EchoTriggeredS2CPacket;
import com.bawnorton.bettertrims.networking.packet.s2c.EntityEchoedS2CPacket;
import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.bettertrims.registry.content.TrimSoundEvents;
import com.bawnorton.bettertrims.registry.content.TrimStatusEffects;
import com.google.common.collect.EvictingQueue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract float getHealth();

    @Shadow public abstract float getHeadYaw();

    @Shadow public abstract void setHealth(float health);

    @Shadow public abstract boolean clearStatusEffects();

    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @WrapOperation(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z"
            )
    )
    private boolean applyEchoing(LivingEntity instance, DamageSource source, Operation<Boolean> original) {
        if(original.call(instance, source)) return true;
        if(source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;

        EvictingQueue<EchoShardTrimEffect.Echo> echoes = TrimEffects.ECHO_SHARD.getEchoes(instance);
        if(echoes == null || echoes.isEmpty()) return false;

        EchoShardTrimEffect.Echo oldest = echoes.peek();
        if(oldest == null) return false;

        echoes.clear();
        Vec3d pos = oldest.pos();
        Vec3d oldPos = getPos();
        updatePositionAndAngles(pos.x, pos.y, pos.z, oldest.yaw(), oldest.pitch());
        setHealth(oldest.health());
        clearStatusEffects();

        ServerWorld world = (ServerWorld) getWorld();
        List<? extends PlayerEntity> players = world.getPlayers();
        players.forEach(player -> {
            if(player instanceof ServerPlayerEntity serverPlayer) {
                ServerPlayNetworking.send(serverPlayer, new EntityEchoedS2CPacket(oldPos, oldest));
            }
        });

        int echoingLevel = (int) getAttributeValue(TrimEntityAttributes.ECHOING);
        addStatusEffect(new StatusEffectInstance(TrimStatusEffects.DAMPENED, 20 * 300 / echoingLevel, 0));
        for(int i = 5; i > 0; i--) {
            float pitch = i * 0.1f;
            CompletableFuture.delayedExecutor(50L * i, TimeUnit.MILLISECONDS).execute(() -> world.getServer().execute(() -> {
                world.playSound((LivingEntity) (Object) this, BlockPos.ofFloored(pos), TrimSoundEvents.ECHO_REWIND, SoundCategory.PLAYERS, 2f, pitch);
                world.playSound((LivingEntity) (Object) this, BlockPos.ofFloored(oldPos), TrimSoundEvents.ECHO_REWIND, SoundCategory.PLAYERS, 2f, pitch);
            }));
        }
        if(instance instanceof ServerPlayerEntity player) {
            ServerPlayNetworking.send(player, new EchoTriggeredS2CPacket(oldest));
        }
        return true;
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;sendEquipmentChanges()V"
            )
    )
    private void createEcho(CallbackInfo ci) {
        TrimEffects.ECHO_SHARD.createEcho((LivingEntity) (Object) this, getPos(), getPitch(), getHeadYaw(), getHealth());
    }
}
