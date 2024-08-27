package com.bawnorton.bettertrims.mixin.attributes.projectile_dodge_chance;

import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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

    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @ModifyVariable(
            method = "applyArmorToDamage",
            at = @At("HEAD"),
            argsOnly = true
    )
    protected float applyDodgeChance(float original, DamageSource source, float amount) {
        if (!source.isIn(DamageTypeTags.IS_PROJECTILE)) return original;

        double chance = getAttributeValue(TrimEntityAttributes.PROJECTILE_DODGE_CHANCE) - 1;
        return dodge(chance) ? 0 : original;
    }
}
