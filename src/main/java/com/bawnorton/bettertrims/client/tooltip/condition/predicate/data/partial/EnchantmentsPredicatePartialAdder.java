package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.PredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;
import java.util.Optional;

//? if >=1.21.8 {
import com.bawnorton.bettertrims.client.mixin.accessor.EnchantmentsPredicateAccessor;
//?} else {
/*import com.bawnorton.bettertrims.client.mixin.accessor.ItemsEnchantmentsPredicateAccessor;
*///?}

public final class EnchantmentsPredicatePartialAdder<T extends /*$ enchantments_predicate >>*/ EnchantmentsPredicate > implements PartialAdder<T> {
	@Override
	public void addToBuilder(ClientLevel level, T predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		//? if >=1.21.8 {
		EnchantmentsPredicateAccessor enchantmentsPredicate = (EnchantmentsPredicateAccessor) predicate;
		 //?} else {
		/*ItemsEnchantmentsPredicateAccessor enchantmentsPredicate = (ItemsEnchantmentsPredicateAccessor) predicate;
		*///?}
		List<EnchantmentPredicate> enchantmentPredicates = enchantmentsPredicate.bettertrims$enchantments();
		if (enchantmentPredicates.isEmpty()) {
			builder.translate(key("enchantments.any"), Styler::condition);
			return;
		}
		boolean useAnd = false;
		for (EnchantmentPredicate enchantmentPredicate : enchantmentPredicates) {
			Optional<HolderSet<Enchantment>> enchantments = enchantmentPredicate.enchantments();
			MinMaxBounds.Ints enchLevel = enchantmentPredicate.level();
			CompositeContainerComponent.Builder enchBuilder = CompositeContainerComponent.builder();
			if (enchantments.isPresent()) {
				HolderSet<Enchantment> enchantmentHolderSet = enchantments.orElseThrow();
				PredicateTooltip.addRegisteredElementsToBuilder(
						level,
						useAnd ? "bettertrims.tooltip.and" : enchantmentHolderSet.size() == 1 ? key("enchantments.matches") : key("enchantments.matches_multiple"),
						Registries.ENCHANTMENT,
						enchantmentHolderSet,
						(enchantment, appender) -> {
							if (!enchLevel.isAny()) {
								appender.space();
								PredicateTooltip.addMinMaxToBuilder(
										key("enchantments.level"),
										false,
										enchLevel,
										value -> Component.translatable("enchantment.level.%s".formatted("%.0f".formatted(value))),
										state, appender
								);
							}
							return enchantment.description();
						},
						state, enchBuilder
				);
				useAnd = true;
			} else {
				enchBuilder.space()
						.translate(key("enchantments.any"), Styler::condition)
						.space();
				if (!enchLevel.isAny()) {
					PredicateTooltip.addMinMaxToBuilder(
							key("enchantments.level"),
							false,
							enchLevel,
							value -> Component.translatable("enchantment.level.%s".formatted("%.0f".formatted(value))),
							state, enchBuilder
					);
				}
			}
			builder.component(enchBuilder.build());
		}
	}
}
