package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.extend.StatusEffectInstanceExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.bawnorton.bettertrims.util.RandomHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(StatusEffectInstance.class)
public abstract class StatusEffectInstanceMixin implements StatusEffectInstanceExtender {
    @Unique
    private static final ThreadLocal<Boolean> firstUpdateThreadLocal = ThreadLocal.withInitial(() -> false);
    @Shadow
    @Final
    private StatusEffect type;
    @Shadow
    private int duration;
    @Shadow
    private int amplifier;
    @Unique
    private boolean hadFirstUpdate;

    @Inject(method = "fromNbt(Lnet/minecraft/entity/effect/StatusEffect;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/effect/StatusEffectInstance;", at = @At("HEAD"))
    private static void readAdditionalNbt(StatusEffect type, NbtCompound nbt, CallbackInfoReturnable<StatusEffectInstance> cir) {
        firstUpdateThreadLocal.set(nbt.getBoolean("HadFirstUpdate"));
    }

    @Inject(method = "<init>(Lnet/minecraft/entity/effect/StatusEffect;IIZZZLnet/minecraft/entity/effect/StatusEffectInstance;Ljava/util/Optional;)V", at = @At("RETURN"))
    private void init(StatusEffect type, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, StatusEffectInstance hiddenEffect, Optional factorCalculationData, CallbackInfo ci) {
        hadFirstUpdate = firstUpdateThreadLocal.get();
        firstUpdateThreadLocal.remove();
    }

    @Inject(method = "copyFrom", at = @At("RETURN"))
    private void copyFirstUpgradeBoolean(StatusEffectInstance other, CallbackInfo ci) {
        hadFirstUpdate = ((StatusEffectInstanceExtender) other).betterTrims$hadFirstUpdate();
    }

    @Inject(method = "upgrade", at = @At("RETURN"))
    private void copyFirstUpgradeBoolean(StatusEffectInstance that, CallbackInfoReturnable<Boolean> cir) {
        hadFirstUpdate = ((StatusEffectInstanceExtender) that).betterTrims$hadFirstUpdate();
    }

    @Inject(method = "update", at = @At("HEAD"))
    private void applyGlowstoneUpgrade(LivingEntity entity, Runnable overwriteCallback, CallbackInfoReturnable<Boolean> cir) {
        if (hadFirstUpdate) return;

        hadFirstUpdate = true;
        NumberWrapper chance = NumberWrapper.zero();
        ArmorTrimEffects.GLOWSTONE_DUST.apply(((EntityExtender) entity).betterTrims$getTrimmables(), () -> chance.increment(ConfigManager.getConfig().glowstonePotionAmplifierIncreaseChance));
        if (RandomHelper.nextFloat() < chance.getFloat()) this.amplifier += type.isBeneficial() ? 1 : 0;
    }

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;updateDuration()I", shift = At.Shift.AFTER))
    private void applyTrimDurationBuff(LivingEntity entity, Runnable overwriteCallback, CallbackInfoReturnable<Boolean> cir) {
        NumberWrapper chance = NumberWrapper.zero();
        ArmorTrimEffects.AMETHYST.apply(((EntityExtender) entity).betterTrims$getTrimmables(), () -> chance.increment(ConfigManager.getConfig().amethystPotionDurationModifyChance));
        if (RandomHelper.nextFloat() < chance.getFloat()) duration += type.isBeneficial() ? 1 : -1;
    }

    @Inject(method = "writeTypelessNbt", at = @At("HEAD"))
    private void writeAdditionalNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("HadFirstUpdate", hadFirstUpdate);
    }

    @Override
    public boolean betterTrims$hadFirstUpdate() {
        return hadFirstUpdate;
    }
}
