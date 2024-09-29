package com.bawnorton.bettertrims.mixin.attributes.projectile_deflect_chance;

import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityExtender {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    //$ attribute_shadow
    @Shadow public abstract double getAttributeValue(EntityAttribute attribute);

    @ModifyVariable(
            method = "applyArmorToDamage",
            at = @At("HEAD"),
            argsOnly = true
    )
    protected float applyDeflectChance(float original, DamageSource source, float amount) {
        if(original <= 0) return original;
        if (!source.isIn(DamageTypeTags.IS_PROJECTILE)) return original;

        double chance = getAttributeValue(TrimEntityAttributes.PROJECTILE_DEFLECT_CHANCE) - 1;
        return deflect(chance) ? 0 : original;
    }
}
