package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffect;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
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
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin {
    @Unique
    // @formatter:off
    private final Map<Class<? extends LivingEntity>, BiFunction<Predicate<LivingEntity>, LivingEntity, Predicate<LivingEntity>>> trimPredicates = Map.ofEntries(
            Map.entry(IllagerEntity.class, (original, mob) -> getTrimPredicate(original, mob, ArmorTrimEffects.PLATINUM, ConfigManager.getConfig().platinumEffects.piecesForIllagersIgnore, ConfigManager.getConfig().platinumEffects.illagersIgnore)),
            Map.entry(GuardianEntity.class, (original, mob) -> getTrimPredicate(original, mob, ArmorTrimEffects.PRISMARINE_SHARD, ConfigManager.getConfig().prismarineShardEffects.piecesForGuardiansIgnore, ConfigManager.getConfig().prismarineShardEffects.guardiansIgnore)),
            Map.entry(BlazeEntity.class, (original, mob) -> getTrimPredicate(original, mob, ArmorTrimEffects.NETHER_BRICK, ConfigManager.getConfig().netherBrickEffects.piecesForBlazesIgnore, ConfigManager.getConfig().netherBrickEffects.blazesIgnore)),
            Map.entry(WitherSkeletonEntity.class, (original, mob) -> getTrimPredicate(original, mob, ArmorTrimEffects.NETHER_BRICK, ConfigManager.getConfig().netherBrickEffects.piecesForWitherSkeletonsIgnore, ConfigManager.getConfig().netherBrickEffects.witherSkeletonsIgnore))
    );

    @ModifyArg(method = "<init>(Lnet/minecraft/entity/mob/MobEntity;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/TargetPredicate;setPredicate(Ljava/util/function/Predicate;)Lnet/minecraft/entity/ai/TargetPredicate;"))
    private Predicate<LivingEntity> checkPlayerTrims(@Nullable Predicate<LivingEntity> predicate, @Local(argsOnly=true) MobEntity mob) {
        return trimPredicates.getOrDefault(mob.getClass(), (a, b) -> a).apply(predicate, mob);
    }

    @Unique
    private Predicate<LivingEntity> getTrimPredicate(Predicate<LivingEntity> original, LivingEntity mob, ArmorTrimEffect effect, int required, boolean enabled) {
        return target -> {
            if (!enabled) return original == null || original.test(target);

            NumberWrapper trimCount = NumberWrapper.zero();
            effect.apply(((LivingEntityExtender) target).betterTrims$getTrimmables(), () -> trimCount.increment(1));
            if(target.equals(mob.getAttacker())) return true;

            boolean satisfiesTrimCount = trimCount.getInt() >= required;
            if(!satisfiesTrimCount) return original == null || original.test(target);

            return false;
        };
    }
}
