//? if <1.21.8 {
/*package com.bawnorton.bettertrims.mixin.property.ability.post_attack;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimEntityAbilityRunner;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment
@Mixin(ThrownPotion.class)
abstract class ThrownPotionMixin extends ThrowableItemProjectile {
	ThrownPotionMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
		super(entityType, level);
	}

	@WrapOperation(
			method = "onHit",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/projectile/ThrownPotion;applySplash(Ljava/lang/Iterable;Lnet/minecraft/world/entity/Entity;)V"
			)
	)
	private void triggerTrimOnHit(ThrownPotion instance, Iterable<MobEffectInstance> i, Entity entity, Operation<Void> original) {
		original.call(instance, i, entity);
		if (!(getEffectSource() instanceof LivingEntity wearer)) return;
		if (entity == wearer) return;
		if (!(level() instanceof ServerLevel level)) return;

		for (TrimProperty property : TrimProperties.getProperties(level)) {
			for (TrimEntityAbilityRunner<?> ability : property.getEntityAbilityRunners(TrimAbilityComponents.POST_ATTACK)) {
				ability.runDamage(level, wearer, entity, level.damageSources().magic(), getItem());
			}
		}

	}
}
*///?}