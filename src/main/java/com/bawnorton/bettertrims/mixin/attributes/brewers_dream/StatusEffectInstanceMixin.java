package com.bawnorton.bettertrims.mixin.attributes.brewers_dream;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.extend.ModifiedTimeHolder;
import com.bawnorton.bettertrims.networking.packet.s2c.StatusEffectDurationModifiedS2CPacket;
import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.Codecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Optional;
import java.util.function.Function;

@Mixin(StatusEffectInstance.class)
public abstract class StatusEffectInstanceMixin implements ModifiedTimeHolder {
    //? if >=1.21 {
    /*@Shadow @Final private RegistryEntry<StatusEffect> type;
    *///?} else {
    @Shadow @Final private StatusEffect type;
    //?}

    @Unique
    private static final ThreadLocal<LivingEntity> bettertrims$ENTITY_CAPTURE = new ThreadLocal<>();
    @Unique
    private static final ThreadLocal<StatusEffect> bettertrims$STATUS_EFFECT_CAPTURE = new ThreadLocal<>();
    @Unique
    private static final ThreadLocal<StatusEffectInstance> bettertrims$INSTANCE_CAPTURE = new ThreadLocal<>();
    @Unique
    private int bettertrims$modifiedTime;

    @ModifyExpressionValue(
            //? if fabric {
            method = "method_48560",
            //?} elif neoforge {
            /*method = "lambda$tickDownDuration$3",
            *///?}
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=1"
            )
    )
    private static int applyBrewersDreamToDuration(int original) {
        LivingEntity entity = bettertrims$ENTITY_CAPTURE.get();
        StatusEffect effect = bettertrims$STATUS_EFFECT_CAPTURE.get();
        int level = (int) entity.getAttributeValue(TrimEntityAttributes.BREWERS_DREAM);
        float chance = level * AttributeSettings.BrewersDream.modificationChance;
        int modified;
        if (effect != null && BetterTrims.PROBABILITIES.passes(chance)) {
            modified = effect.isBeneficial() ? 0 : 2;
        } else {
            modified = 1;
        }
        StatusEffectInstance instance = bettertrims$INSTANCE_CAPTURE.get();
        if(instance.getDuration() - modified < 0) return original;

        if (modified != 1 && entity instanceof ServerPlayerEntity player) {
            ((ModifiedTimeHolder) instance).bettertrims$incrementModifiedTime();
            int modifiedTime = ((ModifiedTimeHolder) instance).bettertrims$getModifiedTime();
            //? if >=1.21 {
            /*RegistryEntry<StatusEffect> entry = Registries.STATUS_EFFECT.getEntry(effect);
            ServerPlayNetworking.send(player, new StatusEffectDurationModifiedS2CPacket(entry, modifiedTime));
            if(modifiedTime / player.getWorld().getTickManager().getTickRate() > 60) {
                TrimCriteria.BREWERS_DREAM_EXTENDED.trigger(player);
            }
            *///?} else {
            ServerPlayNetworking.send(player, new StatusEffectDurationModifiedS2CPacket(effect, modifiedTime));
            if(modifiedTime / 20 > 60) {
                TrimCriteria.BREWERS_DREAM_EXTENDED.trigger(player);
            }
            //?}
        }
        return modified;
    }

    @Inject(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/effect/StatusEffectInstance;updateDuration()I"
            )
    )
    private void captureData(LivingEntity entity, Runnable overwriteCallback, CallbackInfoReturnable<Boolean> cir) {
        bettertrims$ENTITY_CAPTURE.set(entity);
        //? if >=1.21 {
        /*bettertrims$STATUS_EFFECT_CAPTURE.set(type.value());
        *///?} else {
        bettertrims$STATUS_EFFECT_CAPTURE.set(type);
        //?}
        bettertrims$INSTANCE_CAPTURE.set((StatusEffectInstance) (Object) this);
    }

    @Override
    public void bettertrims$incrementModifiedTime() {
        bettertrims$modifiedTime++;
    }

    @Override
    public void bettertrims$setModifiedTime(int modifiedTime) {
        bettertrims$modifiedTime = modifiedTime;
    }

    @Override
    public int bettertrims$getModifiedTime() {
        return bettertrims$modifiedTime;
    }

    //? if >=1.21 {
    /*@Inject(
            method = "<init>(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/entity/effect/StatusEffectInstance$Parameters;)V",
            at = @At("TAIL")
    )
    private void attachModifiedTime(RegistryEntry<StatusEffect> effect, StatusEffectInstance.Parameters parameters, CallbackInfo ci) {
        bettertrims$modifiedTime = ((ModifiedTimeHolder) (Object) parameters).bettertrims$getModifiedTime();
    }

    @ModifyReturnValue(
            method = "asParameters",
            at = @At("RETURN")
    )
    private StatusEffectInstance.Parameters attachModifiedTime(StatusEffectInstance.Parameters original) {
        ((ModifiedTimeHolder) (Object) original).bettertrims$setModifiedTime(bettertrims$modifiedTime);
        return original;
    }

    @Inject(
            method = "copyFrom",
            at = @At("TAIL")
    )
    private void copyModifiedTime(StatusEffectInstance that, CallbackInfo ci) {
        bettertrims$modifiedTime = ((ModifiedTimeHolder) that).bettertrims$getModifiedTime();
    }

    @Mixin(StatusEffectInstance.Parameters.class)
    private static abstract class ParametersMixin implements ModifiedTimeHolder {
        @Unique
        private int bettertrims$modifiedTime;

        @ModifyArg(
                method = "method_56671",
                at = @At(
                        value = "INVOKE",
                        target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;mapCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;",
                        remap = false
                )
        )
        private static Function<RecordCodecBuilder.Instance<StatusEffectInstance.Parameters>, ? extends App<RecordCodecBuilder.Mu<StatusEffectInstance.Parameters>, StatusEffectInstance.Parameters>> attachModifiedTime(Function<RecordCodecBuilder.Instance<StatusEffectInstance.Parameters>, ? extends App<RecordCodecBuilder.Mu<StatusEffectInstance.Parameters>, StatusEffectInstance.Parameters>> builder) {
            return instance -> instance.group(
                    RecordCodecBuilder.mapCodec(builder).forGetter(Function.identity()),
                    Codecs.NONNEGATIVE_INT
                            .optionalFieldOf("bettertrims$modifiedTime")
                            .xmap(optional -> optional.orElse(0), Optional::ofNullable)
                            .forGetter(parameters -> ((ModifiedTimeHolder) (Object) parameters).bettertrims$getModifiedTime())
            ).apply(instance, (parameters, modifiedTime) -> {
                ((ModifiedTimeHolder) (Object) parameters).bettertrims$setModifiedTime(modifiedTime);
                return parameters;
            });
        }

        @Override
        public void bettertrims$setModifiedTime(int modifiedTime) {
            bettertrims$modifiedTime = modifiedTime;
        }

        @Override
        public int bettertrims$getModifiedTime() {
            return bettertrims$modifiedTime;
        }
    }
    *///?} else {
    @Inject(
            method = "writeTypelessNbt",
            at = @At("TAIL")
    )
    private void writeModifiedTime(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("bettertrims$modifiedTime", bettertrims$modifiedTime);
    }

    @ModifyReturnValue(
            method = "fromNbt(Lnet/minecraft/entity/effect/StatusEffect;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/effect/StatusEffectInstance;",
            at = @At("RETURN")
    )
    private static StatusEffectInstance readModifiedTime(StatusEffectInstance original, StatusEffect statusEffect, NbtCompound nbt) {
        int modifedTime = nbt.getInt("bettertrims$modifiedTime");
        ((ModifiedTimeHolder) original).bettertrims$setModifiedTime(modifedTime);
        return original;
    }
    //?}
}
