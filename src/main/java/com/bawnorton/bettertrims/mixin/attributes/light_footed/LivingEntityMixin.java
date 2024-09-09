package com.bawnorton.bettertrims.mixin.attributes.light_footed;

import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
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
    private double applyLightFooted(double original, Entity entity) {
        int lightFootedLevel = (int) getAttributeValue(TrimEntityAttributes.LIGHT_FOOTED);
        if (lightFootedLevel <= 0) return original;

        return original / ((lightFootedLevel + 1) * AttributeSettings.LightFooted.noiseDampening);
    }
}
