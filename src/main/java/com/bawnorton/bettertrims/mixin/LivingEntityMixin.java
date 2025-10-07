package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.registry.BetterTrimsAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends EntityMixin {
	@Shadow
	@Final
	private AttributeMap attributes;

	@ModifyReturnValue(
			method = "createLivingAttributes",
			at = @At("RETURN")
	)
	private static AttributeSupplier.Builder addAttributes(AttributeSupplier.Builder original) {
		return original.add(BetterTrimsAttributes.TRUE_INVISIBILITY);
	}

	@Override
	protected boolean isTrulyInvisible(Operation<Boolean> original) {
		return super.isTrulyInvisible(original) || attributes.getValue(BetterTrimsAttributes.TRUE_INVISIBILITY) >= 1;
	}

	@ModifyReturnValue(
			method = "getVisibilityPercent",
			at = @At("RETURN")
	)
	private double applyTrueInvisibility(double original) {
		double value = attributes.getValue(BetterTrimsAttributes.TRUE_INVISIBILITY);
		if(value < 1) return original;

		return Math.max(0.1, original * 0.1);
	}
}
