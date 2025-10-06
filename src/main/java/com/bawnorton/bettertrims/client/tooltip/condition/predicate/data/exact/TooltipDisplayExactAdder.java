//? if >=1.21.8 {
package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.SequencedSet;

public final class TooltipDisplayExactAdder implements ExactAdder<TooltipDisplay> {
	@Override
	public void addToBuilder(ClientLevel level, TooltipDisplay display, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		boolean hideTooltip = display.hideTooltip();
		SequencedSet<DataComponentType<?>> hiddenComponents = display.hiddenComponents();
		if (hideTooltip) {
			builder.translate(key("tooltip_display.hide_tooltip"), Styler::condition);
		}

		if (hiddenComponents.isEmpty()) {
			builder.space().translate(key("tooltip_display.none"), Styler::condition);
		} else {
			CompositeContainerComponent.Builder tooltipDisplayBuilder = CompositeContainerComponent.builder()
					.space()
					.translate(key("tooltip_display.hidden_components"), Styler::condition)
					.space()
					.cycle(cycleBuilder -> hiddenComponents.forEach(type -> {
						ResourceLocation typeKey = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type);
						String typeName = typeKey != null ? typeKey.toString() : "unregistered";
						cycleBuilder.textComponent(Styler.name(Component.literal(typeName)));
					}));
			builder.component(tooltipDisplayBuilder.build());
		}
	}
}
//?}