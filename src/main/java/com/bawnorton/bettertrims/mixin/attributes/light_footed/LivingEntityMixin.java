package com.bawnorton.bettertrims.mixin.attributes.light_footed;

import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @ModifyReturnValue(
            method = "getAttackDistanceScalingFactor",
            at = @At("RETURN")
    )
    private double applyLightFooted(double original) {
        int lightFootedLevel = (int) getAttributeValue(TrimEntityAttributes.LIGHT_FOOTED);
        if (lightFootedLevel <= 0) return original;

        lightFootedLevel++;
        return original / (lightFootedLevel * AttributeSettings.LightFooted.noiseDampening);
    }
}
