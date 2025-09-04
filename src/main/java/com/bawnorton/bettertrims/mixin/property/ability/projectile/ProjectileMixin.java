package com.bawnorton.bettertrims.mixin.property.ability.projectile;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinEnvironment
@Mixin(Projectile.class)
abstract class ProjectileMixin extends Entity {
    ProjectileMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    @Nullable
    public abstract Entity getOwner();

    @Inject(
            method = "applyOnProjectileSpawned",
            at = @At("TAIL")
    )
    private void applyTrimsToSpawnedProjectiles(ServerLevel level, ItemStack spawnedFrom, CallbackInfo ci) {
        if(getOwner() instanceof LivingEntity wearer) {
            for(TrimProperty property : TrimProperties.getProperties(level)) {
                property.abilityHolders().forEach(ability -> ability.onProjectileSpanwed(wearer, (Projectile) (Object) this));
            }
        }
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void applyTrimsToProjectileTick(CallbackInfo ci) {
        if(getOwner() instanceof LivingEntity wearer) {
            for(TrimProperty property : TrimProperties.getProperties(level())) {
                property.abilityHolders().forEach(ability -> ability.onProjectileTick(wearer, (Projectile) (Object) this));
            }
        }
    }
}
