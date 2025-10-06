package com.bawnorton.bettertrims.mixin.property.ability.post_attack;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimEntityAbilityRunner;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(EnchantmentHelper.class)
abstract class EnchantmentHelperMixin {
	@Inject(
			//? if 1.21.8 {
			method = "doPostAttackEffectsWithItemSourceOnBreak",
			//?} elif 1.21.1 {
			/*method = "doPostAttackEffectsWithItemSource",
			*///?}
			at = @At("TAIL")
	)
	private static void doTrimPostAttackAbilities(CallbackInfo ci,
	                                              @Local(argsOnly = true) ServerLevel level,
	                                              @Local(argsOnly = true) Entity entity,
	                                              @Local(argsOnly = true) DamageSource damageSource,
	                                              @Local(argsOnly = true) ItemStack itemSource
	) {
		Entity source = damageSource.getEntity();
		if (!(source instanceof LivingEntity wearer)) return;

		for (TrimProperty property : TrimProperties.getProperties(level)) {
			for (TrimEntityAbilityRunner<?> ability : property.getEntityAbilityRunners(TrimAbilityComponents.POST_ATTACK)) {
				ability.runDamage(level, wearer, entity, damageSource, itemSource);
			}
		}
	}
}
