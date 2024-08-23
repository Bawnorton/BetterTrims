package com.bawnorton.bettertrims.mixin.trim.netherite;

import com.bawnorton.bettertrims.effect.TrimEffects;
import com.bawnorton.bettertrims.effect.context.TrimContext;
import com.bawnorton.bettertrims.effect.context.TrimContextParameterSet;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @ModifyReturnValue(
            method = "applyArmorToDamage",
            at = @At("RETURN")
    )
    protected float applyNetheriteTrimToDamage(float original, DamageSource source, float amount) {
        if(!TrimEffects.NETHERITE.matches(this)) return original;

        TrimContextParameterSet.Builder builder = TrimContextParameterSet.builder()
                .add(TrimContextParameters.DAMAGE_SOURCE, source)
                .add(TrimContextParameters.DAMAGE_AMOUNT, original);
        return TrimEffects.NETHERITE.getApplicator().apply(new TrimContext(builder), (LivingEntity) (Object) this);
    }
}
