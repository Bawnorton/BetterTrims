//? if >=1.21.8 {
package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.PredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Repairable;

public final class RepairableExactAdder implements ExactAdder<Repairable> {
	@Override
	public void addToBuilder(ClientLevel level, Repairable repairable, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		HolderSet<Item> items = repairable.items();
		if (items.size() == 0) {
			builder.translate(key("repairable.none"), Styler::condition);
		} else {
			PredicateTooltip.addRegisteredElementsToBuilder(level, key("repairable.items"), Registries.ITEM, items, item -> item.getName(), state, builder);
		}
	}
}
//?}