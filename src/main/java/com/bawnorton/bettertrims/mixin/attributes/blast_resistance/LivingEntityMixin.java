package com.bawnorton.bettertrims.mixin.attributes.blast_resistance;

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
    @Shadow public abstract double getAttributeValue(EntityAttribute attribute);

    @ModifyVariable(
            method = "applyArmorToDamage",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float applyBlastResistanceToDamage(float original, DamageSource source, float amount) {
        if (!source.isIn(DamageTypeTags.IS_EXPLOSION)) return original;

        double blastResistance = getAttributeValue(TrimEntityAttributes.BLAST_RESISTANCE) - 1;
        original *= (float) (1 - blastResistance);
        return Math.max(original, 0);
    }
}