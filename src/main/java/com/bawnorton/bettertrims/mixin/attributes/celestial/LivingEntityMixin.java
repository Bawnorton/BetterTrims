package com.bawnorton.bettertrims.mixin.attributes.celestial;

import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityExtender {
    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyReturnValue(
            method = "getMovementSpeed()F",
            at = @At("RETURN")
    )
    protected float applyCelestialToMovementSpeed(float original) {
        return original + 0.05f * bettertrims$getCelestialLevel();
    }

    @ModifyReturnValue(
            method = "applyArmorToDamage",
            at = @At("RETURN")
    )
    protected float applyCelestialToDamage(float original) {
        return original * (1 - bettertrims$getCelestialLevel() * 0.03f);
    }

    @Unique
    protected float bettertrims$applyCelestialToAttackDamage(float original) {
        return original + 0.5f * bettertrims$getCelestialLevel();
    }

    @Unique
    protected float bettertrims$applyCelestialToAttackSpeed(float original) {
        return original + 0.3f * bettertrims$getCelestialLevel();
    }

    @Unique
    public int bettertrims$applyCelestialToAttackCooldown(int original) {
        return (int) (original - bettertrims$getCelestialLevel() / 0.3f);
    }

    @Unique
    protected int bettertrims$getCelestialLevel() {
        if(getWorld().isDay()) {
            return (int) getAttributeValue(TrimEntityAttributes.SUNS_BLESSING);
        } else if (getWorld().isNight()) {
            return (int) getAttributeValue(TrimEntityAttributes.MOONS_BLESSING);
        } else if (getWorld().getRegistryKey().equals(World.NETHER)) {
            return (int) getAttributeValue(TrimEntityAttributes.HELLS_BLESSING);
        } else if (getWorld().getRegistryKey().equals(World.END)) {
            return (int) getAttributeValue(TrimEntityAttributes.ENDS_BLESSING);
        }
        return 0;
    }
}
