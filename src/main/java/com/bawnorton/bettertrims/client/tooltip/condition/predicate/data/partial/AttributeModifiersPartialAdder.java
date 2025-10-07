package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.PredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import net.minecraft.advancements.critereon.CollectionPredicate;
import net.minecraft.core.component.predicates.AttributeModifiersPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class AttributeModifiersPartialAdder implements PartialAdder</*$ attribute_modifiers_predicate >>*/ AttributeModifiersPredicate > {
	@Override
	public void addToBuilder(ClientLevel level, /*$ attribute_modifiers_predicate >>*/ AttributeModifiersPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		Optional<CollectionPredicate<ItemAttributeModifiers.Entry, /*$ attribute_modifiers_predicate >>*/ AttributeModifiersPredicate .EntryPredicate>> modifiers = predicate.modifiers();
		if (modifiers.isPresent()) {
			addCollectionToBuilder(
					level,
					modifiers.orElseThrow(),
					"attribute_modifiers",
					(predicateLevel, entryPredicate, predicateState, collectionBuilder) -> {
						Optional<ResourceLocation> id = entryPredicate.id();
						Optional<HolderSet<Attribute>> attributeSet = entryPredicate.attribute();
						Optional<EquipmentSlotGroup> slot = entryPredicate.slot();
						Optional<AttributeModifier.Operation> operation = entryPredicate.operation();
						MinMaxBounds.Doubles amount = entryPredicate.amount();

						if (attributeSet.isPresent()) {
							PredicateTooltip.addRegisteredElementsToBuilder(
									level,
									key("attribute_modifiers.attribute"),
									Registries.ATTRIBUTE,
									attributeSet.orElseThrow(),
									(attribute, appender) -> {
										if (operation.isPresent() && !amount.isAny()) {
											CompositeContainerComponent.Builder detailBuilder = CompositeContainerComponent.builder();
											PredicateTooltip.addMinMaxToBuilder(
													key("attribute_modifiers.amount.%s".formatted(operation.orElseThrow().id())),
													false,
													amount,
													value -> Component.literal((value > 0 ? "+" : "-") + ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(value)),
													predicateState,
													detailBuilder
											);
											appender.component(detailBuilder.build())
													.space()
													.textComponent(Styler.positive(Component.translatable(attribute.getDescriptionId())));
											return Component.literal("");
										}
										return Component.literal("").append(Styler.positive(Component.translatable(attribute.getDescriptionId())));
									},
									predicateState.withPrefixSpace(false),
									collectionBuilder
							);
							predicateState.withPrefixSpace(true);
						}

						if (id.isPresent()) {
							collectionBuilder.space()
									.translate(key("attribute_modifiers.id"), Styler::condition, Styler.value(Component.literal(id.orElseThrow().toString())));
						}

						if (slot.isPresent()) {
							//? if >=1.21.8 {
							List<EquipmentSlot> slots = slot.orElseThrow().slots();
							 //?} else {
							/*List<EquipmentSlot> slots = Arrays.stream(EquipmentSlot.values()).filter(slot.orElseThrow()::test).toList();
							if (!slots.isEmpty()) {
								PredicateTooltip.addEnumListToBuilder(
										key("attribute_modifiers.slot"),
										slots,
										s -> Component.translatable(StringUtils.capitalize(s.getName())),
										predicateState,
										collectionBuilder
								);
							}
							*///?}
						}
					},
					state,
					builder
			);
		} else {
			builder.space().translate(key("attribute_modifiers.any"), Styler::condition);
		}
	}
}
