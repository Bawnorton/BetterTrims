package com.bawnorton.bettertrims.mixin.attributes.resistance;

import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityExtender {
    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @ModifyReturnValue(
            method = "applyArmorToDamage",
            at = @At("RETURN")
    )
    private float applyResistanceToDamage(float original, DamageSource source, float amount) {
        double resistance = getAttributeValue(TrimEntityAttributes.RESISTANCE) - 1;
        return (float) (original * (1 - resistance));
    }
}
