package com.bawnorton.bettertrims.mixin.attributes.celestial;

import com.bawnorton.bettertrims.effect.CelestialEffect;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.registry.content.TrimEffects;
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
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityExtender {
    //$ attribute_shadow
    @Shadow public abstract double getAttributeValue(EntityAttribute attribute);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyReturnValue(
            method = "getMovementSpeed()F",
            at = @At("RETURN")
    )
    protected float applyCelestialToMovementSpeed(float original) {
        return original + bettertrims$getCelestialLevel() * bettertrims$getCelestial().map(CelestialEffect::getMovementSpeed).orElse(0f);
    }

    @ModifyReturnValue(
            method = "applyArmorToDamage",
            at = @At("RETURN")
    )
    protected float applyCelestialToDamage(float original) {
        return original * (1 - bettertrims$getCelestialLevel() * bettertrims$getCelestial().map(CelestialEffect::getDamageResistance).orElse(0f));
    }

    @Unique
    protected float bettertrims$applyCelestialToAttackDamage(float original) {
        return original + bettertrims$getCelestialLevel() * bettertrims$getCelestial().map(CelestialEffect::getAttackDamage).orElse(0f);
    }

    @Unique
    protected float bettertrims$applyCelestialToAttackSpeed(float original) {
        return original + bettertrims$getCelestialLevel() * bettertrims$getCelestial().map(CelestialEffect::getAttackSpeed).orElse(0f);
    }

    @Unique
    public int bettertrims$applyCelestialToAttackCooldown(int original) {
        return (int) (original - bettertrims$getCelestialLevel() / bettertrims$getCelestial().map(CelestialEffect::getAttackSpeed).orElse(1f));
    }

    @Unique
    protected int bettertrims$getCelestialLevel() {
        Optional<CelestialEffect> effectOptional = bettertrims$getCelestial();
        return effectOptional.map(effect -> (int) getAttributeValue(effect.getEntityAttribute())).orElse(0);
    }

    @Unique
    protected Optional<CelestialEffect> bettertrims$getCelestial() {
        if(getWorld().isDay()) {
            return Optional.of(TrimEffects.GOLD);
        } else if (getWorld().isNight()) {
            return Optional.of(TrimEffects.SILVER);
        } else if (getWorld().getRegistryKey().equals(World.NETHER)) {
            return Optional.of(TrimEffects.GLOWSTONE);
        } else if (getWorld().getRegistryKey().equals(World.END)) {
            return Optional.of(TrimEffects.STARRITE);
        }
        return Optional.empty();
    }
}
