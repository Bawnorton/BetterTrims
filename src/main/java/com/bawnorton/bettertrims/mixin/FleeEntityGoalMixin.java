package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(FleeEntityGoal.class)
public abstract class FleeEntityGoalMixin {
    @ModifyArg(method = "<init>(Lnet/minecraft/entity/mob/PathAwareEntity;Ljava/lang/Class;Ljava/util/function/Predicate;FDDLjava/util/function/Predicate;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/TargetPredicate;setPredicate(Ljava/util/function/Predicate;)Lnet/minecraft/entity/ai/TargetPredicate;"))
    private Predicate<LivingEntity> checkPlayerTrims(@Nullable Predicate<LivingEntity> predicate, @Local(argsOnly = true) Class<LivingEntity> classToFleeFrom, @Local(argsOnly = true) PathAwareEntity mob) {
        if (mob instanceof AnimalEntity && classToFleeFrom.isAssignableFrom(PlayerEntity.class)) {
            return getTrimPredicate(predicate);
        }
        return predicate;
    }

    @Unique
    private Predicate<LivingEntity> getTrimPredicate(Predicate<LivingEntity> original) {
        return target -> {
            NumberWrapper trimCount = NumberWrapper.zero();
            ArmorTrimEffects.RABBIT_HIDE.apply(((LivingEntityExtender) target).betterTrims$getTrimmables(), () -> trimCount.increment(1));
            return trimCount.getInt() < 1 && (original == null || original.test(target));
        };
    }
}
