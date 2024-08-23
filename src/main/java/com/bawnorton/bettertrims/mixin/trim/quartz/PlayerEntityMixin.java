package com.bawnorton.bettertrims.mixin.trim.quartz;

import com.bawnorton.bettertrims.effect.TrimEffects;
import com.bawnorton.bettertrims.effect.context.TrimContext;
import com.bawnorton.bettertrims.effect.context.TrimContextParameterSet;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @ModifyVariable(method = "addExperience", at = @At("HEAD"), argsOnly = true)
    private int applyQuartzTrim(int original) {
        if(!TrimEffects.QUARTZ.matches(this)) return original;

        TrimContextParameterSet.Builder builder = TrimContextParameterSet.builder()
                .add(TrimContextParameters.EXPERIENCE, original);
        return TrimEffects.QUARTZ.getApplicator().apply(new TrimContext(builder), (LivingEntity) (Object) this);
    }
}
