package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.ExactDataComponentPredicateTooltipAdders;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface ExactAdder<T> {
	Function<DataComponentType<?>, ExactAdder<?>> UNKNOWN = type -> (level, object, state, builder) -> builder.translate(
			ExactDataComponentPredicateTooltipAdders.key("unknown_type"),
			Styler::property,
			Objects.requireNonNullElse(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type), "[unregistred]").toString()
	);

	void addToBuilder(ClientLevel level, T object, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder);

	default String key(String key) {
		return ExactDataComponentPredicateTooltipAdders.key(key);
	}

	static <T> ExactAdder<T> simple(BiConsumer<T, CompositeContainerComponent.Builder> consumer) {
		return (level, object, state, builder) -> {
			consumer.accept(object, builder);
		};
	}

	static <T extends Enum<T>> ExactAdder<T> ofEnum(String key) {
		return simple((enumInstance, builder) -> builder.translate(
				ExactDataComponentPredicateTooltipAdders.key(key),
				Styler::condition,
				Styler.value(Component.literal(StringUtils.capitalize(enumInstance.name()))))
		);
	}
}