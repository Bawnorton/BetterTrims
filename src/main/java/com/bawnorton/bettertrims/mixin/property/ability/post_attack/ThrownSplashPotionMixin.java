//? if >=1.21.8 {
package com.bawnorton.bettertrims.mixin.property.ability.post_attack;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimEntityAbilityRunner;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractThrownPotion;
import net.minecraft.world.entity.projectile.ThrownSplashPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ThrownSplashPotion.class)
abstract class ThrownSplashPotionMixin extends AbstractThrownPotion {
	ThrownSplashPotionMixin(EntityType<? extends AbstractThrownPotion> entityType, Level level) {
		super(entityType, level);
	}

	@WrapOperation(
			method = "onHitAsPotion",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;isEmpty()Z"
			)
	)
	private boolean triggerTrimOnHit(List<LivingEntity> instance, Operation<Boolean> original, ServerLevel level, ItemStack stack, HitResult hitResult) {
		boolean isEmpty = original.call(instance);
		if (!(getEffectSource() instanceof LivingEntity wearer)) return isEmpty;

		for (LivingEntity entity : instance) {
			if (entity == wearer) continue;

			for (TrimProperty property : TrimProperties.getProperties(level)) {
				for (TrimEntityAbilityRunner<?> ability : property.getEntityAbilityRunners(TrimAbilityComponents.POST_ATTACK)) {
					ability.runDamage(level, wearer, entity, level.damageSources().magic(), stack);
				}
			}
		}
		return false;
	}
}
//?}