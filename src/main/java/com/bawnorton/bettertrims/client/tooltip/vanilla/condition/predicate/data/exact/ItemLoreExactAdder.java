package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.component.ItemLore;

public final class ItemLoreExactAdder implements ExactAdder<ItemLore> {
	@Override
	public void addToBuilder(ClientLevel level, ItemLore lore, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		if (lore.lines().isEmpty()) {
			builder.translate(key("lore.any"), Styler::condition);
		} else {
			CompositeContainerComponent.Builder loreBuilder = CompositeContainerComponent.builder()
					.space()
					.translate(key("lore"), Styler::condition)
					.space()
					.cycle(cycleBuilder -> lore.styledLines().forEach(line -> cycleBuilder.textComponent(line.copy())));
			builder.component(loreBuilder.build());
		}
	}
}
