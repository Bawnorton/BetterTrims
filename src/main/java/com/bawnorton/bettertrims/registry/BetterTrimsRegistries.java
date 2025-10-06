package com.bawnorton.bettertrims.registry;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.ability.type.TrimToggleAbility;
import com.bawnorton.bettertrims.property.ability.type.TrimValueAbility;
import com.bawnorton.bettertrims.property.count.CountBasedValueTypes;
import com.bawnorton.bettertrims.property.item.TrimItemPropertyComponents;
import com.bawnorton.bettertrims.property.item.type.TrimItemProperty;
import com.google.common.collect.Maps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class BetterTrimsRegistries {
	private static final Map<ResourceLocation, Supplier<?>> LOADERS = Maps.newLinkedHashMap();

	public static final Registry<DataComponentType<?>> TRIM_ABILITY_COMPONENT_TYPE = createRegistry(Keys.TRIM_ABILITY_COMPONENT_TYPE, TrimAbilityComponents::bootstrap);
	public static final Registry<DataComponentType<?>> TRIM_ITEM_PROPERTY_COMPONENT_TYPE = createRegistry(Keys.TRIM_ITEM_PROPERTY_COMPONENT_TYPE, TrimItemPropertyComponents::bootstrap);

	public static final Registry<MapCodec<? extends TrimToggleAbility>> TRIM_TOGGLE_ABILITY_TYPE = createRegistry(Keys.TRIM_TOGGLE_ABILITY_TYPE, TrimToggleAbility::bootstrap);
	public static final Registry<MapCodec<? extends TrimValueAbility>> TRIM_VALUE_ABILITY_TYPE = createRegistry(Keys.TRIM_VALUE_ABILITY_TYPE, TrimValueAbility::bootstrap);
	public static final Registry<MapCodec<? extends TrimEntityAbility>> TRIM_ENTITY_ABILITY_TYPE = createRegistry(Keys.TRIM_ENTITY_ABILITY_TYPE, TrimEntityAbility::bootstrap);

	public static final Registry<MapCodec<? extends TrimItemProperty>> TRIM_ITEM_PROPERTY_TYPES = createRegistry(Keys.TRIM_ITEM_PROPERTY_TYPES, TrimItemProperty::bootstrap);

	private static <T> Registry<T> createRegistry(ResourceKey<Registry<T>> key, Function<Registry<T>, Object> bootstrap) {
		Registry<T> registry = new MappedRegistry<>(key, Lifecycle.stable());
		LOADERS.put(key.location(), () -> bootstrap.apply(registry));
		return registry;
	}

	public static void createContents() {
		CountBasedValueTypes.init();
		BetterTrimsAttributes.init();

		LOADERS.forEach((key, supplier) -> {
			if (supplier.get() == null) {
				BetterTrims.LOGGER.error("Unable to bootstrap registry '{}'", key);
			}
		});
	}

	public static class Keys {
		public static final ResourceKey<Registry<TrimProperty>> TRIM_PROPERTIES = createRegistryKey("trim_properties");

		public static final ResourceKey<Registry<DataComponentType<?>>> TRIM_ABILITY_COMPONENT_TYPE = createRegistryKey("trim_ability_component_type");
		public static final ResourceKey<Registry<DataComponentType<?>>> TRIM_ITEM_PROPERTY_COMPONENT_TYPE = createRegistryKey("trim_item_property_component_type");

		public static final ResourceKey<Registry<MapCodec<? extends TrimToggleAbility>>> TRIM_TOGGLE_ABILITY_TYPE = createRegistryKey("trim_toggle_ability_type");
		public static final ResourceKey<Registry<MapCodec<? extends TrimValueAbility>>> TRIM_VALUE_ABILITY_TYPE = createRegistryKey("trim_value_ability_type");
		public static final ResourceKey<Registry<MapCodec<? extends TrimEntityAbility>>> TRIM_ENTITY_ABILITY_TYPE = createRegistryKey("trim_entity_ability_type");

		public static final ResourceKey<Registry<MapCodec<? extends TrimItemProperty>>> TRIM_ITEM_PROPERTY_TYPES = createRegistryKey("item_property_type");

		private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
			return ResourceKey.createRegistryKey(BetterTrims.rl(name));
		}
	}
}
