package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EscapeDangerGoal.class)
public abstract class EscapeDanagerGoalMixin {
    @Shadow
    @Final
    protected PathAwareEntity mob;

    @ModifyReturnValue(method = "isInDanger", at = @At("RETURN"))
    private boolean checkPlayerTrims(boolean original) {
        if (!original) return false;
        if (!(mob.getAttacker() instanceof EntityExtender extender)) return true;

        return !ArmorTrimEffects.RABBIT_HIDE.appliesTo(extender.betterTrims$getTrimmables());
    }
}
