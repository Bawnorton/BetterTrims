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

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin {
    @Unique
    private final Map<Class<? extends LivingEntity>, Function<Predicate<LivingEntity>, Predicate<LivingEntity>>> trimPredicates = Map.ofEntries(
            Map.entry(IllagerEntity.class, original -> getTrimPredicate(original, ArmorTrimEffects.PLATINUM)),
            Map.entry(GuardianEntity.class, original -> getTrimPredicate(original, ArmorTrimEffects.PRISMARINE_SHARD)),
            Map.entry(BlazeEntity.class, original -> getTrimPredicate(original, ArmorTrimEffects.NETHER_BRICK)),
            Map.entry(WitherSkeletonEntity.class, original -> getTrimPredicate(original, ArmorTrimEffects.NETHER_BRICK, 2))
    );

    @ModifyArg(method = "<init>(Lnet/minecraft/entity/mob/MobEntity;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/TargetPredicate;setPredicate(Ljava/util/function/Predicate;)Lnet/minecraft/entity/ai/TargetPredicate;"))
    private Predicate<LivingEntity> checkPlayerTrims(@Nullable Predicate<LivingEntity> predicate, @Local MobEntity mob) {
        return trimPredicates.getOrDefault(mob.getClass(), Function.identity()).apply(predicate);
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
