package com.bawnorton.bettertrims.mixin.attributes.fire_resistance;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    //$ attribute_shadow
    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @ModifyVariable(
            method = "applyArmorToDamage",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float applyFireResistanceToDamage(float original, DamageSource source, float amount) {
        if (!source.isIn(DamageTypeTags.IS_FIRE)) {
            return original;
        }

        double fireResistance = getAttributeValue(TrimEntityAttributes.FIRE_RESISTANCE) - 1;
        original *= (float) (1 - fireResistance);
        return original;
    }
}
