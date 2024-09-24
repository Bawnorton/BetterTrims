package com.bawnorton.bettertrims.mixin.attributes.hydrophobic;

import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    //$ attribute_shadow
    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @ModifyReturnValue(
            method = "hurtByWater",
            at = @At("RETURN")
    )
    private boolean applyHydrophobic(boolean original) {
        return original || getAttributeValue(TrimEntityAttributes.HYDROPHOBIC) > 0;
    }

    @SuppressWarnings("ConstantValue")
    @ModifyExpressionValue(
            method = "tickMovement",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    private boolean triggerHydrophobicWaterDamage(boolean original) {
        if(original && (Object) this instanceof ServerPlayerEntity player && getAttributeValue(TrimEntityAttributes.HYDROPHOBIC) > 0) {
            TrimCriteria.HYDROPHOBIC_TOUCH_WATER.trigger(player);
        }
        return original;
    }
}
