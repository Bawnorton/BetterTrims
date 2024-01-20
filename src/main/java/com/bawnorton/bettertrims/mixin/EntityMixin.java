package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Unique
    private int ticksSinceLastTeleport = 0;

    @Shadow
    public abstract World getWorld();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract Vec3d getVelocity();

    @Shadow
    public abstract void setVelocity(double x, double y, double z);

    @Shadow
    public abstract void setOnFireFor(int seconds);

    @ModifyReturnValue(method = "isFireImmune", at = @At("RETURN"))
    protected boolean checkIfNetheriteTrimmed(boolean original) {
        return original;
    }

    @ModifyArg(method = "setOnFireFromLava", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    protected float reduceNetheriteTrimDamage(float original) {
        return original;
    }

    @ModifyReturnValue(method = "getStepHeight", at = @At("RETURN"))
    protected float applyTrimStepHeightIncrease(float original) {
        return original;
    }

    @WrapOperation(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onEntityLand(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;)V"))
    protected void onLand(Block instance, BlockView world, Entity entity, Operation<Void> original) {
        original.call(instance, world, entity);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void betterTrims$teleportOnChorusFruitTeleportation(CallbackInfo ci) {
        ticksSinceLastTeleport++;
    }

    @Unique
    public boolean betterTrims$shouldSilverApply() {
        DimensionType dimension = getWorld().getDimension();
        if (dimension.hasFixedTime()) {
            return ConfigManager.getConfig().silverEffects.applyInFixedTime;
        }

        long time = getWorld().getTimeOfDay() % 24000;
        return time >= 13000 && time <= 23000;
    }

    @Unique
    protected boolean didDodgeAttack(LivingEntity entity) {
        NumberWrapper dodgeChance = NumberWrapper.zero();
        ArmorTrimEffects.CHORUS_FRUIT.apply(((LivingEntityExtender) entity).betterTrims$getTrimmables(), () -> dodgeChance.increment(ConfigManager.getConfig().chorusFruitDodgeChance));
        if (Math.random() > dodgeChance.getFloat()) {
            return false;
        } else {
            betterTrims$randomTpEntity(entity);
        }
        return true;
    }

    /**
     * Chorus fruit teleportation code
     */
    @Unique
    protected void betterTrims$randomTpEntity(LivingEntity entity) {
        if (ticksSinceLastTeleport < 10) return;

        World world = entity.getWorld();
        if (world.isClient) return;

        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        for (int i = 0; i < 16; ++i) {
            double newX = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * (float) 16;
            double newY = MathHelper.clamp(entity.getY() + (double) (entity.getRandom()
                                                                           .nextInt((int) (float) 16) - 8), world.getBottomY(), world.getBottomY() + ((ServerWorld) world).getLogicalHeight() - 1);
            double newZ = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * (float) 16;
            if (entity.hasVehicle()) entity.stopRiding();

            Vec3d vec3d = entity.getPos();
            if (!entity.teleport(newX, newY, newZ, true)) continue;

            ticksSinceLastTeleport = 0;
            world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(entity));
            SoundEvent soundEvent = entity instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
            world.playSound(null, x, y, z, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
            entity.playSound(soundEvent, 1.0F, 1.0F);
            break;
        }
    }
}
