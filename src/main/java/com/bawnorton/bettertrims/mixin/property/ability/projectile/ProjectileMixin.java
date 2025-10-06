package com.bawnorton.bettertrims.mixin.property.ability.projectile;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimEntityAbilityRunner;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
abstract class ProjectileMixin extends Entity {
	ProjectileMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	@Nullable
	public abstract Entity getOwner();

	@Inject(
			method = "tick",
			at = @At("HEAD")
	)
	private void applyTrimsToProjectileTick(CallbackInfo ci) {
		if (!(getOwner() instanceof LivingEntity wearer) || !(level() instanceof ServerLevel level)) return;

		for (TrimProperty property : TrimProperties.getProperties(level)) {
			for (TrimEntityAbilityRunner<?> ability : property.getEntityAbilityRunners(TrimAbilityComponents.PROJECTILE_TICK)) {
				ability.runTick(level, wearer, this, position());
			}
		}
	}
}
