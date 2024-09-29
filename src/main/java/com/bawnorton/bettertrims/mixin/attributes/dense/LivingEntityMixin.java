package com.bawnorton.bettertrims.mixin.attributes.dense;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
    //$ attribute_shadow
    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Override
    protected boolean applyDense(boolean original) {
        if (original) {
            return !(getAttributeValue(TrimEntityAttributes.DENSE) >= 1);
        }
        return false;

    }

    @ModifyReturnValue(
            method = "shouldSwimInFluids",
            at = @At("RETURN")
    )
    protected boolean shouldntSwimIfDense(boolean original) {
        if (original) {
            return !(getAttributeValue(TrimEntityAttributes.DENSE) >= 1);
        }
        return false;

    }
}
