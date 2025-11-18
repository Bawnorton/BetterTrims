package com.bawnorton.bettertrims.mixin.property.item_property.damage_immunity;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.context.TrimContexts;
import com.bawnorton.bettertrims.property.element.ConditionalElementMatcher;
import com.bawnorton.bettertrims.property.element.ElementMatcher;
import com.bawnorton.bettertrims.property.item.TrimItemPropertyComponents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
	LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	//? if fabric {
	@WrapOperation(
			method = "doHurtEquipment",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;canBeHurtBy(Lnet/minecraft/world/damagesource/DamageSource;)Z"
			)
	)
	private boolean isTrimInvulnerableTo(ItemStack instance, DamageSource damageSource, Operation<Boolean> original) {
		boolean canBeHurt = original.call(instance, damageSource);
		if (!canBeHurt) return false;
		if (!(level() instanceof ServerLevel level)) return false;

		for (TrimProperty property : TrimProperties.getProperties(level)) {
			for (ElementMatcher<?> elementMatcher : property.getItemPropertyElements(TrimItemPropertyComponents.DAMAGE_IMMUNITY)) {
				if (elementMatcher.matches(instance, TrimContexts.damageItem(level, instance, null, damageSource))) {
					return true;
				}
			}
		}
		return true;
	}
	//?} else {
	/*@WrapOperation(
			method = "doHurtEquipment",
			at = @At(
					value = "INVOKE",
					target = "Lnet/neoforged/neoforge/common/CommonHooks;onArmorHurt(Lnet/minecraft/world/damagesource/DamageSource;[Lnet/minecraft/world/entity/EquipmentSlot;FLnet/minecraft/world/entity/LivingEntity;)V"
			)
	)
	private void isTrimInvulnerableTo(DamageSource damageSource, EquipmentSlot[] slots, float damage, LivingEntity wearer, Operation<Void> original) {
		if (!(level() instanceof ServerLevel level)) {
			original.call(damageSource, slots, damage, wearer);
			return;
		}

		for (TrimProperty property : TrimProperties.getProperties(level)) {
			for (ElementMatcher<?> elementMatcher : property.getItemPropertyElements(TrimItemPropertyComponents.DAMAGE_IMMUNITY)) {
				for (EquipmentSlot slot : slots) {
					ItemStack itemStack = wearer.getItemBySlot(slot);
					if (elementMatcher.matches(itemStack, TrimContexts.damageItem(level, itemStack, null, damageSource))) {
						return;
					}
				}
			}
		}
		original.call(damageSource, slots, damage, wearer);
	}
	*///?}
}
