package com.bawnorton.bettertrims.mixin.attributes.magic_protection;

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
    private float applyMagicProtectionToDamage(float original, DamageSource source, float amount) {
        if(!TrimEntityAttributes.MAGIC_PROTECTION.isUsingAlias()) return original;
        if (!source.isIn(DamageTypeTags.WITCH_RESISTANT_TO)) return original;

        double magicProtection = getAttributeValue(TrimEntityAttributes.MAGIC_PROTECTION.get());
        original -= (float) magicProtection;
        return Math.max(original, 0);
    }
}
