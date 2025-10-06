package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.version.VRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.function.Function;

public record EnchantmentsExactAdder(String prefix) implements ExactAdder<ItemEnchantments> {
	@Override
	public void addToBuilder(ClientLevel level, ItemEnchantments enchantments, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		if (enchantments.isEmpty()) {
			builder.translate(key("%s.any".formatted(prefix)), Styler::condition);
		} else {
			Registry<Enchantment> registry = VRegistry.get(level, Registries.ENCHANTMENT);
			CompositeContainerComponent.Builder enchantmentsBuilder = CompositeContainerComponent.builder()
					.space()
					.translate(key(prefix), Styler::condition)
					.space()
					.cycle(cycleBuilder -> enchantments.entrySet().forEach(entry -> {
						Holder<Enchantment> key = entry.getKey();
						Enchantment enchantment = key.unwrap().map(registry::getValueOrThrow, Function.identity());
						int enchantmentLevel = entry.getIntValue();
						cycleBuilder.component(CompositeContainerComponent.builder()
								.translate(
										key("%s.entry".formatted(prefix)),
										Styler::condition,
										Styler.name(enchantment.description().copy()),
										Styler.number(enchantmentLevel)
								)
								.build());
					}));
			builder.component(enchantmentsBuilder.build());
		}
	}
}
