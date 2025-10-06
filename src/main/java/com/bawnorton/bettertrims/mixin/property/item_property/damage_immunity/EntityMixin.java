package com.bawnorton.bettertrims.mixin.property.item_property.damage_immunity;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.context.TrimContexts;
import com.bawnorton.bettertrims.property.element.ElementMatcher;
import com.bawnorton.bettertrims.property.item.TrimItemPropertyComponents;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
abstract class EntityMixin {
	@Shadow
	private Level level;

	@SuppressWarnings("ConstantValue")
	@ModifyReturnValue(
			//? if 1.21.8 {
			method = "isInvulnerableToBase",
			//?} elif 1.21.1 {
			/*method = "isInvulnerableTo",
			*///?}
			at = @At("RETURN")
	)
	private boolean isTrimInvulnerableTo(boolean original, DamageSource damageSource) {
		if (original) return true;
		if (!((Object) this instanceof ItemEntity itemEntity)) return false;
		if (!(level instanceof ServerLevel serverLevel)) return false;

		ItemStack stack = itemEntity.getItem();
		for (TrimProperty property : TrimProperties.getProperties(serverLevel)) {
			for (ElementMatcher<?> elementMatcher : property.getItemPropertyElements(TrimItemPropertyComponents.DAMAGE_IMMUNITY)) {
				if (elementMatcher.matches(stack, TrimContexts.damageItem(serverLevel, stack, itemEntity, damageSource))) {
					return true;
				}
			}
		}
		return false;
	}
}
