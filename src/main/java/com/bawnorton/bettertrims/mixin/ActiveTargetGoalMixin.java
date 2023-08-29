package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.effect.ArmorTrimEffect;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin {
    @ModifyArg(method = "<init>(Lnet/minecraft/entity/mob/MobEntity;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/TargetPredicate;setPredicate(Ljava/util/function/Predicate;)Lnet/minecraft/entity/ai/TargetPredicate;"))
    private Predicate<LivingEntity> checkPlayerTrims(@Nullable Predicate<LivingEntity> predicate, @Local MobEntity mob) {
        if(mob instanceof IllagerEntity) {
            return getTrimPredicate(predicate, ArmorTrimEffects.PLATINUM);
        }
        if(mob instanceof GuardianEntity) {
            return getTrimPredicate(predicate, ArmorTrimEffects.PRISMARINE_SHARD);
        }
        if(mob instanceof BlazeEntity) {
            return getTrimPredicate(predicate, ArmorTrimEffects.NETHER_BRICK);
        }
        if(mob instanceof WitherSkeletonEntity) {
            return getTrimPredicate(predicate, ArmorTrimEffects.NETHER_BRICK, 2);
        }
        return predicate;
    }

    @Unique
    private Predicate<LivingEntity> getTrimPredicate(Predicate<LivingEntity> original, ArmorTrimEffect effect) {
        return getTrimPredicate(original, effect, 1);
    }

    @Unique
    private Predicate<LivingEntity> getTrimPredicate(Predicate<LivingEntity> original, ArmorTrimEffect effect, int required) {
        return target -> {
            NumberWrapper trimCount = NumberWrapper.zero();
            effect.apply(((EntityExtender) target).betterTrims$getTrimmables(), () -> trimCount.increment(1));
            return trimCount.getInt() < required && (original == null || original.test(target));
        };
    }
}
