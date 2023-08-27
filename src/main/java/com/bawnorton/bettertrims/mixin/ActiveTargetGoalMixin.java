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
        if(!(mob instanceof EntityExtender extender)) return predicate;

        if(mob instanceof IllagerEntity) {
            return getTrimPredicate(predicate, extender, ArmorTrimEffects.PLATINUM);
        }
        if(mob instanceof GuardianEntity) {
            return getTrimPredicate(predicate, extender, ArmorTrimEffects.PRISMARINE_SHARD);
        }
        if(mob instanceof BlazeEntity) {
            return getTrimPredicate(predicate, extender, ArmorTrimEffects.NETHER_BRICK, 2);
        }
        if(mob instanceof WitherSkeletonEntity) {
            return getTrimPredicate(predicate, extender, ArmorTrimEffects.NETHER_BRICK, 4);
        }
        return predicate;
    }

    @Unique
    private Predicate<LivingEntity> getTrimPredicate(Predicate<LivingEntity> original, EntityExtender extender, ArmorTrimEffect effect) {
        return getTrimPredicate(original, extender, effect, 1);
    }

    @Unique
    private Predicate<LivingEntity> getTrimPredicate(Predicate<LivingEntity> original, EntityExtender extender, ArmorTrimEffect effect, int required) {
        NumberWrapper trimCount = NumberWrapper.zero();
        effect.apply(extender.betterTrims$getTrimmables(), stack -> trimCount.increment(1));
        return target -> trimCount.getInt() < required && (original == null || original.test(target));
    }
}
