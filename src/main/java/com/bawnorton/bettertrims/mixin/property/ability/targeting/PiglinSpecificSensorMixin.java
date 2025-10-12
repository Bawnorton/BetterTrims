package com.bawnorton.bettertrims.mixin.property.ability.targeting;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.type.misc.PiglinSafeAbility;
import com.bawnorton.bettertrims.property.context.TrimContexts;
import com.bawnorton.bettertrims.property.element.ElementMatcher;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.PiglinSpecificSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PiglinSpecificSensor.class)
abstract class PiglinSpecificSensorMixin {
	@WrapOperation(
			method = "doTick",
			at = @At(
					value = "INVOKE",
					//? if >=1.21.8 {
					target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;isWearingSafeArmor(Lnet/minecraft/world/entity/LivingEntity;)Z"
					//?} else {
					/*target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;isWearingGold(Lnet/minecraft/world/entity/LivingEntity;)Z"
					*///?}
			)
	)
	private static boolean isWearingSafeTrim(LivingEntity target, Operation<Boolean> original, ServerLevel level, LivingEntity entity) {
		boolean wearingSafeArmour = original.call(target);
		if (wearingSafeArmour) return true;

		for (TrimProperty property : TrimProperties.getProperties(level)) {
			for (ElementMatcher<?> ability : property.getAbilityElements(TrimAbilityComponents.PIGLIN_SAFE)) {
				if (ability.getElement() instanceof PiglinSafeAbility piglinSafeAbility
						&& ability.matches(target, TrimContexts.entity(level, ability.getMatchingItems(target), entity, entity.position()))
						&& piglinSafeAbility.entityLooksForGold(entity)) {
					return true;
				}
			}
		}
		return false;
	}
}
