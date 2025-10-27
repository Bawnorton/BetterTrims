package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

//? if <1.21.8 {
/*import net.minecraft.world.item.ItemStack;
import com.bawnorton.bettertrims.mixin.accessor.ItemStackAccessor;
*///?}

public final class ItemAttributeModifiersExactAdder implements ExactAdder<ItemAttributeModifiers> {
	@Override
	public void addToBuilder(ClientLevel level, ItemAttributeModifiers modifiers, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		List<ItemAttributeModifiers.Entry> entries = modifiers.modifiers();
		if (entries.isEmpty()) {
			builder.translate(key("attribute_modifiers.any"), Styler::condition);
			return;
		}

		builder.space()
				.translate(key("attribute_modifiers"), Styler::condition)
				.space()
				.cycle(cycleBuilder -> entries.forEach(entry -> {
					CompositeContainerComponent.Builder entryBuilder = CompositeContainerComponent.builder();
					Holder<Attribute> attribute = entry.attribute();
					EquipmentSlotGroup slot = entry.slot();
					AttributeModifier modifier = entry.modifier();
					//? if >=1.21.8 {
					ItemAttributeModifiers.Display.attributeModifiers().apply(
					//?} else {
					/*((ItemStackAccessor) (Object) ItemStack.EMPTY).bettertrims$addModifierTooltip(
					 *///?}
							component -> entryBuilder.textComponent(component)
									.space()
									.translate(
											key("attribute_modifiers.slot"),
											Styler::condition,
											Styler.name(Component.literal(StringUtils.capitalize(slot.getSerializedName())))
									),
							Minecraft.getInstance().player,
							attribute,
							modifier
					);
					cycleBuilder.component(entryBuilder.build());
				}));
	}
}
