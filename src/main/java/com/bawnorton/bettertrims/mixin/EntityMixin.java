package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.bawnorton.bettertrims.config.Config;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityExtender {
    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @Shadow public abstract Iterable<ItemStack> getHandItems();

    @Shadow public abstract World getWorld();

    @ModifyReturnValue(method = "isFireImmune", at = @At("RETURN"))
    private boolean checkIfNetheriteTrimmed(boolean original) {
        NumberWrapper netheriteCount = NumberWrapper.zero();
        ArmorTrimEffects.NETHERITE.apply(betterTrims$getTrimmables(), stack -> netheriteCount.increment(Config.getInstance().netheriteFireResistance));
        return original || netheriteCount.getFloat() >= 0.99f;
    }

    @ModifyArg(method = "setOnFireFromLava", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private float reduceNetheriteTrimDamage(float original) {
        NumberWrapper netheriteCount = NumberWrapper.zero();
        ArmorTrimEffects.NETHERITE.apply(betterTrims$getTrimmables(), stack -> netheriteCount.increment(Config.getInstance().netheriteFireResistance));
        return original * (1 - netheriteCount.getFloat());
    }

    @ModifyReturnValue(method = "getStepHeight", at = @At("RETURN"))
    private float applyTrimStepHeightIncrease(float original) {
        NumberWrapper increase = NumberWrapper.zero();
        ArmorTrimEffects.LEATHER.apply(betterTrims$getTrimmables(), stack -> increase.increment(Config.getInstance().leatherStepHeightIncrease));
        return original + increase.getFloat();
    }

    @Unique
    public List<ItemStack> betterTrims$getTrimmables() {
        List<ItemStack> equipped = new ArrayList<>();
        for(ItemStack stack: getHandItems()) equipped.add(stack);
        equipped.removeIf(stack -> stack.getItem() instanceof ArmorItem);
        for(ItemStack stack: getArmorItems()) equipped.add(stack);
        equipped.removeIf(ItemStack::isEmpty);
        return equipped;
    }

    @Unique
    public boolean betterTrims$shouldSilverApply() {
        long time = getWorld().getTimeOfDay() % 24000;
        return time >= 13000 && time <= 23000;
    }

    @Unique
    protected boolean didDodgeAttack(Entity entity) {
        NumberWrapper dodgeChance = NumberWrapper.zero();
        ArmorTrimEffects.CHORUS_FRUIT.apply(((EntityExtender) entity).betterTrims$getTrimmables(), stack -> dodgeChance.increment(Config.getInstance().chorusFruitDodgeChance));
        if(Math.random() > dodgeChance.getFloat()) {
            return false;
        } else if (entity instanceof LivingEntity livingEntity) {
            betterTrims$randomTpEntity(livingEntity);
        }
        return true;
    }

    /**
     * Chorus fruit teleportation code
     */
    @Unique
    private void betterTrims$randomTpEntity(LivingEntity entity) {
        World world = entity.getWorld();
        if (!world.isClient) {
            double x = entity.getX();
            double y = entity.getY();
            double z = entity.getZ();

            for(int i = 0; i < 16; ++i) {
                double newX = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 16.0;
                double newY = MathHelper.clamp(entity.getY() + (double)(entity.getRandom().nextInt(16) - 8), world.getBottomY(), world.getBottomY() + ((ServerWorld)world).getLogicalHeight() - 1);
                double newZ = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 16.0;
                if (entity.hasVehicle()) {
                    entity.stopRiding();
                }

                Vec3d vec3d = entity.getPos();
                if (entity.teleport(newX, newY, newZ, true)) {
                    world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(entity));
                    SoundEvent soundEvent = entity instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                    world.playSound(null, x, y, z, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    entity.playSound(soundEvent, 1.0F, 1.0F);
                    break;
                }
            }
        }
    }
}
