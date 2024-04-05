package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.AreaEffectCloudEntityExtender;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.mixin.accessor.AreaEffectCloudEntityAccessor;
import com.bawnorton.bettertrims.mixin.accessor.StatusEffectInstanceAccessor;
import com.bawnorton.bettertrims.util.EquippedStack;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.bawnorton.bettertrims.util.RandomHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin implements LivingEntityExtender {
    @Shadow
    @Final
    private Map<StatusEffect, StatusEffectInstance> activeStatusEffects;
    @Unique
    private int stopwatch = 0;

    @Shadow
    public abstract float getAbsorptionAmount();

    @Shadow
    public abstract void setAbsorptionAmount(float amount);

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract @Nullable LivingEntity getAttacker();

    @Unique
    public Iterable<EquippedStack> betterTrims$getTrimmables() {
        List<EquippedStack> equipped = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack equippedStack = getEquippedStack(slot);
            if (equippedStack.getItem() instanceof Equipment) {
                equipped.add(EquippedStack.of(equippedStack, slot));
            }
        }

        return equipped;
    }

    @Override
    protected boolean checkIfNetheriteTrimmed(boolean original) {
        NumberWrapper netheriteCount = NumberWrapper.zero();
        ArmorTrimEffects.NETHERITE.apply(betterTrims$getTrimmables(), () -> netheriteCount.increment(ConfigManager.getConfig().netheriteFireResistance));
        return original || netheriteCount.getFloat() >= 0.99f;
    }

    @Override
    protected float reduceNetheriteTrimDamage(float original) {
        NumberWrapper netheriteCount = NumberWrapper.zero();
        ArmorTrimEffects.NETHERITE.apply(betterTrims$getTrimmables(), () -> netheriteCount.increment(ConfigManager.getConfig().netheriteFireResistance));
        return original * (1 - netheriteCount.getFloat());
    }

    @Override
    protected float applyTrimStepHeightIncrease(float original) {
        NumberWrapper increase = NumberWrapper.zero();
        ArmorTrimEffects.LEATHER.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().leatherStepHeightIncrease));
        return original + increase.getFloat();
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V", ordinal = 0))
    private float applyTrimSwimSpeedIncrease(float original) {
        if (!ArmorTrimEffects.COPPER.appliesTo(betterTrims$getTrimmables())) return original;

        NumberWrapper increase = NumberWrapper.zero();
        ArmorTrimEffects.COPPER.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().copperSwimSpeedIncrease));
        int dsLevel = EnchantmentHelper.getDepthStrider((LivingEntity) (Object) this);
        if (dsLevel > 0) original += dsLevel * ConfigManager.getConfig().copperSwimSpeedIncrease;
        return original + increase.getFloat();
    }

    @ModifyReturnValue(method = "getMovementSpeed()F", at = @At("RETURN"))
    private float applyTrimSpeedIncrease(float original) {
        if (((LivingEntity) (Object) this) instanceof AbstractHorseEntity horseEntity) {
            if (horseEntity.getControllingPassenger() instanceof PlayerEntity player) {
                NumberWrapper increase = NumberWrapper.one();
                ArmorTrimEffects.REDSTONE.apply(((LivingEntityExtender) player).betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().redstoneMovementSpeedIncrease));
                if (betterTrims$shouldSilverApply()) {
                    ArmorTrimEffects.SILVER.apply(((LivingEntityExtender) player).betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().silverEffects.movementSpeed));
                }
                return original * increase.getFloat();
            }
        }
        NumberWrapper increase = NumberWrapper.one();
        ArmorTrimEffects.REDSTONE.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().redstoneMovementSpeedIncrease));
        if (betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().silverEffects.movementSpeed));
        }
        return original * increase.getFloat();
    }

    @ModifyExpressionValue(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;", ordinal = 2), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isFallFlying()Z")))
    private Vec3d applyTrimSpeedIncrease(Vec3d original) {
        NumberWrapper increase = NumberWrapper.one();
        ArmorTrimEffects.REDSTONE.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().redstoneMovementSpeedIncrease));
        if (betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().silverEffects.movementSpeed));
        }
        return original.multiply(increase.getFloat(), 1, increase.getFloat());
    }

    @WrapOperation(method = "applyArmorToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getDamageLeft(FFF)F"))
    private float applyTrimDamageReduction(float damage, float armor, float armorToughness, Operation<Float> original) {
        float orignal = original.call(damage, armor, armorToughness);
        NumberWrapper decrease = NumberWrapper.one();
        ArmorTrimEffects.DIAMOND.apply(betterTrims$getTrimmables(), () -> decrease.decrement(ConfigManager.getConfig().diamondDamageReduction));
        if (betterTrims$shouldSilverApply()) {
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), () -> decrease.decrement(ConfigManager.getConfig().silverEffects.damageReduction));
        }
        return decrease.getFloat() * orignal;
    }

    @ModifyReturnValue(method = "getJumpVelocity", at = @At("RETURN"))
    private float applyTrimJumpHeight(float original) {
        if (betterTrims$shouldSilverApply()) {
            NumberWrapper increase = NumberWrapper.zero();
            ArmorTrimEffects.SILVER.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().silverEffects.jumpHeight));
            return original + increase.getFloat();
        }
        return original;
    }

    @ModifyArg(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setAttacker(Lnet/minecraft/entity/LivingEntity;)V"))
    private @Nullable LivingEntity applyFireChargeTrim(@Nullable LivingEntity original) {
        if (original == null) return null;

        NumberWrapper duration = NumberWrapper.zero();
        ArmorTrimEffects.FIRE_CHARGE.apply(betterTrims$getTrimmables(), () -> duration.increment(ConfigManager.getConfig().fireChargeFireDuration));
        if (duration.getInt() > 0) {
            original.setOnFireFor(duration.getInt());
        }
        duration.set(0);
        ArmorTrimEffects.FIRE_CHARGE.apply(((LivingEntityExtender) original).betterTrims$getTrimmables(), () -> duration.increment(ConfigManager.getConfig().fireChargeFireDuration));
        if (duration.getInt() > 0) {
            setOnFireFor(duration.getInt());
        }
        return original;
    }

    @Override
    protected void onLand(Block instance, BlockView world, Entity entity, Operation<Void> original) {
        if (ConfigManager.getConfig().slimeBallEffects.bounce && ArmorTrimEffects.SLIME_BALL.appliesTo(getEquippedStack(EquipmentSlot.FEET))) {
            Vec3d vec3d = getVelocity();
            if (vec3d.y < 0.0) setVelocity(vec3d.x, -vec3d.y, vec3d.z);
        } else {
            super.onLand(instance, world, entity, original);
        }
    }

    @ModifyReturnValue(method = "computeFallDamage", at = @At("RETURN"))
    private int applyTrimFallDamageReduction(int original) {
        if (ConfigManager.getConfig().slimeBallEffects.bounce && ArmorTrimEffects.SLIME_BALL.appliesTo(getEquippedStack(EquipmentSlot.FEET)))
            return 0;

        NumberWrapper decrease = NumberWrapper.one();
        ArmorTrimEffects.SLIME_BALL.apply(betterTrims$getTrimmables(), () -> decrease.decrement(ConfigManager.getConfig().slimeBallEffects.fallDamageReduction));
        return (int) (original * decrease.getFloat());
    }

    @ModifyVariable(method = "takeKnockback", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private double applyTrimKnockbackDecrease(double original) {
        NumberWrapper increase = NumberWrapper.one();
        ArmorTrimEffects.SLIME_BALL.apply(betterTrims$getTrimmables(), () -> increase.increment(ConfigManager.getConfig().slimeBallEffects.knockbackIncrease));
        return original * increase.getFloat();
    }

    @Inject(method = "tickStatusEffects", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;", remap = false))
    private void manageAreaEffectCloud(CallbackInfo ci) {
        if (!ArmorTrimEffects.DRAGONS_BREATH.appliesTo(betterTrims$getTrimmables())) return;

        NumberWrapper radius = NumberWrapper.zero();
        ArmorTrimEffects.DRAGONS_BREATH.apply(betterTrims$getTrimmables(), () -> radius.increment(ConfigManager.getConfig().dragonBreathRadius));

        AreaEffectCloudEntity areaEffectCloud = new AreaEffectCloudEntity(getWorld(), getX(), getY(), getZ());
        activeStatusEffects.values().forEach(effect -> {
            StatusEffectInstance copy = new StatusEffectInstance(effect);
            ((StatusEffectInstanceAccessor) copy).setDuration(2);
            areaEffectCloud.addEffect(copy);
        });
        ((AreaEffectCloudEntityExtender) areaEffectCloud).betterTrims$setTrimOwner((LivingEntity) (Object) this);
        areaEffectCloud.setOwner((LivingEntity) (Object) this);
        areaEffectCloud.setRadius(radius.getFloat());
        areaEffectCloud.setDuration(1);
        ((AreaEffectCloudEntityAccessor) areaEffectCloud).invokeUpdateColor();
        getWorld().spawnEntity(areaEffectCloud);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void addAbsorptionHearts(CallbackInfo ci) {
        stopwatch++;
        if (!ArmorTrimEffects.ENCHANTED_GOLDEN_APPLE.appliesTo(betterTrims$getTrimmables())) return;

        NumberWrapper ticksUntilHeal = NumberWrapper.of(ConfigManager.getConfig().enchantedGoldenAppleEffects.absorptionDelay);
        NumberWrapper absorptionAmount = NumberWrapper.zero();
        ArmorTrimEffects.ENCHANTED_GOLDEN_APPLE.apply(betterTrims$getTrimmables(), () -> {
            ticksUntilHeal.decrement(ConfigManager.getConfig().enchantedGoldenAppleEffects.absorptionDelayReduction);
            absorptionAmount.increment(ConfigManager.getConfig().enchantedGoldenAppleEffects.absorptionAmount);
        });
        if (absorptionAmount.getFloat() > ConfigManager.getConfig().enchantedGoldenAppleEffects.maxAbsorption) {
            absorptionAmount.set(ConfigManager.getConfig().enchantedGoldenAppleEffects.maxAbsorption);
        }
        ticksUntilHeal.decrement(stopwatch);
        if (ticksUntilHeal.getInt() > 0) return;

        stopwatch = 0;
        if (getAbsorptionAmount() < absorptionAmount.getFloat()) {
            setAbsorptionAmount(absorptionAmount.getFloat());
        }
    }

    @ModifyExpressionValue(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isDead()Z", ordinal = 0))
    private boolean shouldDodge(boolean original, DamageSource source, float amount) {
        if (original) return true;
        if (!ArmorTrimEffects.ENDER_PEARL.appliesTo(betterTrims$getTrimmables())) return false;

        NumberWrapper chance = NumberWrapper.zero();
        ArmorTrimEffects.ENDER_PEARL.apply(betterTrims$getTrimmables(), () -> chance.increment(ConfigManager.getConfig().enderPearlEffects.dodgeChance));
        if (chance.getFloat() > RandomHelper.nextFloat() || source.isIn(DamageTypeTags.IS_DROWNING) && ConfigManager.getConfig().enderPearlEffects.waterDamagesUser) {
            betterTrims$randomTpEntity((LivingEntity) (Object) this);
        }
        return true;
    }

    @ModifyReturnValue(method = "hurtByWater", at = @At("RETURN"))
    private boolean checkTrims(boolean original) {
        if (original) return true;
        return ConfigManager.getConfig().enderPearlEffects.waterDamagesUser && ArmorTrimEffects.ENDER_PEARL.appliesTo(betterTrims$getTrimmables());
    }

    @ModifyReturnValue(method = "hasStatusEffect", at = @At("RETURN"))
    private boolean checkForSilverTrim(boolean original, StatusEffect statusEffect) {
        if (statusEffect != StatusEffects.NIGHT_VISION) return original;
        if (original) return true;

        return betterTrims$shouldSilverApply() && ArmorTrimEffects.SILVER.appliesTo(betterTrims$getTrimmables());
    }

    @ModifyVariable(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z", at = @At("HEAD"), argsOnly = true)
    private StatusEffectInstance addTrimAmplifierBuff(StatusEffectInstance original) {
        NumberWrapper chance = NumberWrapper.zero();
        ArmorTrimEffects.GLOWSTONE_DUST.apply(betterTrims$getTrimmables(), () -> chance.increment(ConfigManager.getConfig().glowstonePotionAmplifierIncreaseChance));
        if (chance.getFloat() > RandomHelper.nextFloat()) {
            original = new StatusEffectInstance(original.getEffectType(), original.getDuration(), original.getAmplifier() + 1, original.isAmbient(), original.shouldShowParticles(), original.shouldShowIcon());
        }
        return original;
    }
}
