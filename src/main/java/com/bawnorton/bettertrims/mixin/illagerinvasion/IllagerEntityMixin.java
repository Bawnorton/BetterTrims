package com.bawnorton.bettertrims.mixin.illagerinvasion;

import com.bawnorton.bettertrims.annotation.ConditionalMixin;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.mixin.LivingEntityMixin;
import fuzs.illagerinvasion.world.entity.monster.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({VindicatorEntity.class,
        PillagerEntity.class,
        EvokerEntity.class,
        IllusionerEntity.class,
        Alchemist.class,
        Archivist.class,
        Basher.class,
        Firecaller.class,
        Inquisitor.class,
        Invoker.class,
        Marauder.class,
        Necromancer.class,
        Provoker.class,
        Sorcerer.class
})
@ConditionalMixin(modid = "illagerinvasion")
public abstract class IllagerEntityMixin extends LivingEntityMixin {
    @ModifyArg(method = "initGoals", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V", ordinal = 1),
            slice = @Slice(
                from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/RevengeGoal;setGroupRevenge([Ljava/lang/Class;)Lnet/minecraft/entity/ai/goal/RevengeGoal;")
    ))
    private Goal replaceWithConditionalTargetGoal(Goal original) {
        return new ActiveTargetGoal<>(((IllagerEntity) (Object) this), PlayerEntity.class, true, target -> !ArmorTrimEffects.PLATINUM.appliesTo(((EntityExtender) target).betterTrims$getTrimmables()));
    }

    @Override
    protected void canTargetOverride(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (ArmorTrimEffects.PLATINUM.appliesTo(((EntityExtender) target).betterTrims$getTrimmables())) cir.setReturnValue(false);
    }
}





