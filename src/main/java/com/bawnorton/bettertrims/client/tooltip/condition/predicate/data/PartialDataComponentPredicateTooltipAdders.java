//~ item_data_predicates
package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data;

import com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.partial.*;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.PredicateTooltip;
import net.minecraft.client.multiplayer.ClientLevel;

import java.util.HashMap;
import java.util.Map;

//? if >=1.21.8 {
import net.minecraft.core.component.predicates.*;
//?} else {
/*import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.DataComponentPredicate;
*///?}

public final class PartialDataComponentPredicateTooltipAdders {
	public static final Map</*$ item_data_predicate >>*/ DataComponentPredicate .Type<?>, PartialAdder<? extends /*$ item_data_predicate >>*/ DataComponentPredicate >> PARTIAL_ADDER_MAP = new HashMap<>();

	static {
		register(DataComponentPredicates.ATTRIBUTE_MODIFIERS, new AttributeModifiersPartialAdder());
		register(DataComponentPredicates.ARMOR_TRIM, new TrimPredicatePartialAdder());
		register(DataComponentPredicates.BUNDLE_CONTENTS, new BundlePredicatePartialAdder());
		register(DataComponentPredicates.CONTAINER, new ContainerPredicatePartialAdder());
		register(DataComponentPredicates.CUSTOM_DATA, new CustomDataPredicatePartialAdder());
		register(DataComponentPredicates.DAMAGE, new DamagePredicatePartialAdder());
		register(DataComponentPredicates.ENCHANTMENTS, new EnchantmentsPredicatePartialAdder<>());
		register(DataComponentPredicates.FIREWORK_EXPLOSION, new FireworkExplosionPredicatePartialAdder());
		register(DataComponentPredicates.FIREWORKS, new FireworksPredicatePartialAdder());
		register(DataComponentPredicates.STORED_ENCHANTMENTS, new EnchantmentsPredicatePartialAdder<>());
		register(DataComponentPredicates.POTIONS, new PotionsPredicatePartialAdder());
		register(DataComponentPredicates.WRITABLE_BOOK, new WritableBookPartialAdder());
		register(DataComponentPredicates.WRITTEN_BOOK, new WrittenBookPartialAdder());
		register(DataComponentPredicates.JUKEBOX_PLAYABLE, new JukeboxPlayablePartialAdder());
	}

	private static <T extends /*$ item_data_predicate >>*/ DataComponentPredicate > void register(/*$ item_data_predicate >>*/ DataComponentPredicate .Type<T> type, PartialAdder<T> adder) {
		if (PARTIAL_ADDER_MAP.containsKey(type)) {
			throw new IllegalStateException("Duplicate data component predicate type: " + type);
		}
		PARTIAL_ADDER_MAP.put(type, adder);
	}

	@SuppressWarnings("unchecked")
	public static void addToBuilder(ClientLevel level, /*$ item_data_predicate >>*/ DataComponentPredicate .Type<?> type, /*$ item_data_predicate >>*/ DataComponentPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		PartialAdder</*$ item_data_predicate >>*/ DataComponentPredicate > adder = (PartialAdder</*$ item_data_predicate >>*/ DataComponentPredicate >) PARTIAL_ADDER_MAP.getOrDefault(type, PartialAdder.UNKNOWN.apply(type));
		adder.addToBuilder(level, predicate, state, builder);
	}

	public static String key(String key) {
		return PredicateTooltip.key("data.partial.%s".formatted(key));
	}
}