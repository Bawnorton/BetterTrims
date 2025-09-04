package com.bawnorton.bettertrims.mixin.property.ability.projectile;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityRunner;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@MixinEnvironment
@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
    LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyVariable(
            method = "hurtServer",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float applyTrimToProjectileDamage(float original, @Local(argsOnly = true) DamageSource source) {
        Entity damageCauser = source.getDirectEntity();
        Entity damageOwner = source.getEntity();
        if (!(damageOwner instanceof LivingEntity wearer && damageCauser instanceof Projectile projectile)) return original;

        for(TrimProperty property : TrimProperties.getProperties(level())) {
            for(TrimAbilityRunner ability : property.abilityHolders()) {
                original = ability.modifyProjectileDamage(wearer, (LivingEntity) (Object) this, projectile, original);
            }
        }
        return original;
    }
}
