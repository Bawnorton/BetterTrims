package com.bawnorton.bettertrims.mixin.attributes.hydrophobic;

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
            method = "hurtByWater",
            at = @At("RETURN")
    )
    private boolean applyHydrophobic(boolean original) {
        return original || getAttributeValue(TrimEntityAttributes.HYDROPHOBIC) > 0;
    }
}
