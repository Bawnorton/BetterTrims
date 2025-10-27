package com.bawnorton.bettertrims.client.tooltip.vanilla;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.BetterTrimsClient;
import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CyclingComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.component.DynamicWidthComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.component.ItemComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.element.TrimElementTooltipProvider;
import com.bawnorton.bettertrims.client.tooltip.vanilla.element.TrimElementTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import com.bawnorton.bettertrims.property.Matcher;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.element.ConditionalElement;
import com.bawnorton.bettertrims.property.element.ConditionalElementMatcher;
import com.bawnorton.bettertrims.property.element.TrimElement;
import com.bawnorton.bettertrims.property.item.TrimItemPropertyComponents;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import com.bawnorton.bettertrims.version.VRegistry;
import com.bawnorton.bettertrims.version.VTrims;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class TrimTooltipPage {
	private final Matcher matcher;
	private final TrimProperty property;
	private ClientTooltipComponent component;

	public TrimTooltipPage(TrimProperty property, Matcher matcher) {
		this.matcher = matcher;
		this.property = property;
	}

	private static ClientTooltipComponent generateAsItemHeader() {
		return CompositeContainerComponent.builder()
				.translate("bettertrims.tooltip.properties.as_item", Styler::normal)
				.build();
	}

	private static @NotNull List<ItemStack> getPatternProviders(List<TrimPattern> patterns, ClientLevel level) {
		List<ItemStack> patternProviders = new ArrayList<>();
		for (Map.Entry<Holder<TrimPattern>, HolderSet<Item>> entry : BetterTrimsClient.getPatternSources().entrySet()) {
			Holder<TrimPattern> patternHolder = entry.getKey();
			TrimPattern pattern = patternHolder.unwrap()
			//? if >=1.21.8 {
					.map(key -> VRegistry.get(level, Registries.TRIM_PATTERN).getValueOrThrow(key), Function.identity());
			//?} else {
			/*.map(key -> VRegistry.get(level, Registries.TRIM_PATTERN).getOrThrow(key), Function.identity());
			 *///?}
			if (patterns.contains(pattern)) {
				HolderSet<Item> itemHolders = entry.getValue();
				patternProviders.addAll(itemHolders.stream()
						.map(holder -> holder.unwrap()
						//? if >=1.21.8 {
								.map(key -> VRegistry.get(level, Registries.ITEM).getValueOrThrow(key), Function.identity()))
						//?} else {
						/*.map(key -> VRegistry.get(level, Registries.ITEM).getOrThrow(key), Function.identity()))
						 *///?}
						.map(Item::getDefaultInstance)
						.toList());
			}
		}
		return patternProviders;
	}

	public static ClientTooltipComponent generateMatcherComponent(ClientLevel level, HolderSet<TrimMaterial> materials, HolderSet<TrimPattern> patterns) {
		List<TrimMaterial> materialList = materials.stream().map(Holder::value).toList();
		List<TrimPattern> patternList = patterns.stream().map(Holder::value).toList();
		if (materialList.isEmpty()) {
			return generateAnyMaterialComponent(patternList, level);
		} else if (patternList.isEmpty()) {
			return generateAnyPatternComponent(materialList, level);
		} else {
			return generateSpecificMaterialPatternComponent(patternList, materialList, level);
		}
	}

	private static ClientTooltipComponent generateAnyMaterialComponent(List<TrimPattern> patterns, ClientLevel level) {
		if (patterns.isEmpty()) {
			return generateAnyMaterialOrPatternComponent(level);
		}

		return generateMatcherComponentFor(getAllMaterialProviders(level), getPatternProviders(patterns, level));
	}

	private static ClientTooltipComponent generateAnyPatternComponent(List<TrimMaterial> materials, ClientLevel level) {
		if (materials.isEmpty()) {
			return generateAnyMaterialOrPatternComponent(level);
		}

		return generateMatcherComponentFor(getMaterialProviders(materials, level), getAllPatternProviders(level));
	}

	private static ClientTooltipComponent generateAnyMaterialOrPatternComponent(ClientLevel level) {
		return generateMatcherComponentFor(getAllMaterialProviders(level), getAllPatternProviders(level));
	}

	private static ClientTooltipComponent generateSpecificMaterialPatternComponent(List<TrimPattern> patterns, List<TrimMaterial> materials, ClientLevel level) {
		return generateMatcherComponentFor(getMaterialProviders(materials, level), getPatternProviders(patterns, level));
	}

	private static ClientTooltipComponent generateMatcherComponentFor(List<ItemStack> materialProviders, List<ItemStack> patternProviders) {
		return CompositeContainerComponent.builder()
				.cycle(builder -> materialProviders.stream().map(ItemComponent::new).forEach(builder::component))
				.literal("-", Styler::trim)
				.cycle(builder -> patternProviders.stream().map(ItemComponent::new).forEach(builder::component))
				.spaced()
				.centred(true)
				.build();
	}

	private static List<ItemStack> getMaterialProviders(List<TrimMaterial> materials, ClientLevel level) {
		return getAllMaterialProviders(level)
				.stream()
				.filter(item -> {
					TrimMaterial material = VTrims.getMaterialFromStack(level, item);
					return material != null && materials.contains(material);
				})
				.toList();
	}

	private static List<ItemStack> getAllMaterialProviders(ClientLevel level) {
		return VRegistry.get(level, Registries.ITEM)
				.stream()
				.map(Item::getDefaultInstance)
				.filter(stack -> VTrims.getMaterialFromStack(level, stack) != null)
				.toList();
	}

	private static List<ItemStack> getAllPatternProviders(ClientLevel level) {
		return BetterTrimsClient.getPatternSources()
				.values()
				.stream()
				.map(HolderSet::stream)
				.flatMap(stream -> stream.map(holder -> holder.unwrap()
						//? if >=1.21.8 {
						.map(key -> VRegistry.get(level, Registries.ITEM).getValueOrThrow(key), Function.identity())))
				//?} else {
				/*.map(key -> VRegistry.get(level, Registries.ITEM).getOrThrow(key), Function.identity())))
				 *///?}
				.map(Item::getDefaultInstance)
				.toList();
	}

	public ClientTooltipComponent getComponent() {
		if (component == null) {
			throw new IllegalStateException("Components have not been generated yet");
		}
		return component;
	}

	@SuppressWarnings("unchecked")
	public void generateComponent(ClientLevel level, Font font, int index, int total) {
		component = null;

		CompositeContainerComponent.Builder pageBuilder = CompositeContainerComponent.builder().vertical();
		pageBuilder.component(generateTitle(level, index, total));

		boolean addHeader = true;
		boolean hasAbilities = false;
		for (DataComponentType<?> type : BetterTrimsRegistries.TRIM_ABILITY_COMPONENT_TYPE) {
			DataComponentType<List<ConditionalElement<TrimElement>>> castType = (DataComponentType<List<ConditionalElement<TrimElement>>>) type;
			CompositeContainerComponent abilityComponent = generateAbilityTooltipComponent(level, font, castType);
			if (abilityComponent == null) continue;

			hasAbilities = true;
			if (addHeader) {
				pageBuilder.component(generateWearingAbilityHeader(level));
				addHeader = false;
			}

			Component typeTooltip = TrimAbilityComponents.TOOLTIPS.get(BetterTrimsRegistries.TRIM_ABILITY_COMPONENT_TYPE.getKey(type));
			generateElementComponent(pageBuilder, abilityComponent, typeTooltip);
		}

		addHeader = true;
		for (DataComponentType<?> type : BetterTrimsRegistries.TRIM_ITEM_PROPERTY_COMPONENT_TYPE) {
			DataComponentType<List<ConditionalElement<TrimElement>>> castType = (DataComponentType<List<ConditionalElement<TrimElement>>>) type;
			CompositeContainerComponent itemPropertyComponent = generateItemPropertyTooltipComponent(level, font, castType);
			if (itemPropertyComponent == null) continue;

			if (hasAbilities) {
				pageBuilder.space();
			}
			if (addHeader) {
				pageBuilder.component(generateAsItemHeader());
				addHeader = false;
			}

			Component typeTooltip = TrimItemPropertyComponents.TOOLTIPS.get(BetterTrimsRegistries.TRIM_ITEM_PROPERTY_COMPONENT_TYPE.getKey(type));
			generateElementComponent(pageBuilder, itemPropertyComponent, typeTooltip);
		}

		CompositeContainerComponent page = pageBuilder.build();
		component = TooltipComponentOptimiser.optimise(page, font);
	}

	private void generateElementComponent(CompositeContainerComponent.Builder pageBuilder, CompositeContainerComponent elementComponent, Component typeTooltip) {
		if (typeTooltip == null) {
			typeTooltip = Component.translatable("bettertrims.tooltip.component.unknown");
		}

		CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder()
				.vertical()
				.component(CompositeContainerComponent.builder()
						.space()
						.textComponent(Styler.component(typeTooltip.copy()))
						.build());
		if (!elementComponent.isEmpty()) {
			builder.component(CompositeContainerComponent.builder()
					.space()
					.space()
					.component(elementComponent)
					.build());
		}
		pageBuilder.component(builder.build());
	}

	public int getRenderedWidth(Font font) {
		return DynamicWidthComponent.getMaxWidth(font, component, 0);
	}

	private ClientTooltipComponent generateTitle(ClientLevel level, int index, int total) {
		ResourceLocation propertyId = VRegistry.get(level, BetterTrimsRegistries.Keys.TRIM_PROPERTIES).getKey(property);
		if (propertyId == null) {
			BetterTrims.LOGGER.warn("Property {} does not have a registry name", property);
			propertyId = BetterTrims.rl("unknown");
		}
		CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder()
				.translate("bettertrims.property.%s.%s".formatted(propertyId.getNamespace(), propertyId.getPath()), Styler::trim)
				.space()
				.literal("(", Styler::trim)
				.component(generateMatcherComponent(level, matcher.material(), matcher.pattern()))
				.literal(")", Styler::trim)
				.centred(true);
		if (total > 1) {
			builder.literal(" - [" + (index + 1) + "/" + total + "]", Styler::trim);
		}
		return builder.build();
	}

	private CompositeContainerComponent generateAbilityTooltipComponent(ClientLevel level, Font font, DataComponentType<List<ConditionalElement<TrimElement>>> type) {
		List<ConditionalElementMatcher<?>> elements = property.getAbilityElements(type);
		if (elements.isEmpty()) return null;

		CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder()
				.vertical();
		for (ConditionalElementMatcher<?> element : elements) {
			builder.component(getTooltipFromConditionalElement(level, font, element.getConditionalElement()));
		}
		return builder.build();
	}

	private CompositeContainerComponent generateItemPropertyTooltipComponent(ClientLevel level, Font font, DataComponentType<List<ConditionalElement<TrimElement>>> castType) {
		List<ConditionalElementMatcher<?>> elements = property.getItemPropertyElements(castType);
		if (elements.isEmpty()) return null;

		CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder()
				.vertical();
		for (ConditionalElementMatcher<?> element : elements) {
			builder.component(getTooltipFromConditionalElement(level, font, element.getConditionalElement()));
		}
		return builder.build();
	}

	private ClientTooltipComponent getTooltipFromConditionalElement(ClientLevel level, Font font, ConditionalElement<?> conditionalElement) {
		TrimElement element = conditionalElement.element();
		Optional<LootItemCondition> requirements = conditionalElement.requirements();
		CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder().vertical();
		requirements.map(condition -> LootConditionTooltips.getTooltip(level, font, condition)).ifPresent(builder::component);
		TrimElementTooltipProvider<TrimElement> provider = TrimElementTooltips.getProvider(element.getClass());
		ClientTooltipComponent tooltip = provider.getTooltip(level, element, true);
		if (tooltip != null) {
			builder.component(tooltip);
		}
		return builder.build();
	}

	private ClientTooltipComponent generateWearingAbilityHeader(ClientLevel level) {
		int minCount = matcher.minCount();
		List<CyclingComponent> trimmedItems = new ArrayList<>();
		HolderSet<TrimMaterial> materials = matcher.material();
		if (materials.size() == 0) {
			materials = HolderSet.direct(getAllMaterialProviders(level).stream()
					.map(stack -> Holder.direct(VTrims.getMaterialFromStack(level, stack)))
					.toList());
		}
		HolderSet<TrimPattern> patterns = matcher.pattern();
		if (patterns.size() == 0) {
			patterns = HolderSet.direct(BetterTrimsClient.getPatternSources().keySet().stream().toList());
		}
		List<Pair<Holder<TrimPattern>, Holder<TrimMaterial>>> pairs = new ArrayList<>();
		for (Holder<TrimMaterial> material : materials) {
			for (Holder<TrimPattern> pattern : patterns) {
				pairs.add(Pair.of(pattern, material));
			}
		}
		for (Pair<Holder<TrimPattern>, Holder<TrimMaterial>> pair : pairs) {
			List<ItemStack> items = new ArrayList<>();
			for (Item item : List.of(Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS)) {
				ArmorTrim trim = new ArmorTrim(pair.right(), pair.left());
				ItemStack stack = item.getDefaultInstance();
				stack.set(DataComponents.TRIM, trim);
				items.add(stack);
			}

			CyclingComponent.Builder builder = CyclingComponent.builder();
			items.stream().map(ItemComponent::new).forEach(builder::component);
			trimmedItems.add(builder.build());
		}

		return switch (minCount) {
			case 4 -> CompositeContainerComponent.builder()
					.translate("bettertrims.tooltip.properties.count.all_of", Styler::normal)
					.stack(trimmedItems, 16)
					.translate("bettertrims.tooltip.properties.count.equipped", Styler::normal)
					.spaced()
					.centred(true)
					.build();
			case 1 -> CompositeContainerComponent.builder()
					.translate("bettertrims.tooltip.properties.count.any_of", Styler::normal)
					.cycle(builder -> trimmedItems.stream()
							.flatMap(cycler -> cycler.getComponents().stream())
							.forEach(builder::component))
					.translate("bettertrims.tooltip.properties.count.equipped", Styler::normal)
					.spaced()
					.centred(true)
					.build();
			default -> CompositeContainerComponent.builder()
					.translate("bettertrims.tooltip.properties.count.n_of", Styler::normal, minCount)
					.stack(trimmedItems, 16)
					.translate("bettertrims.tooltip.properties.count.equipped", Styler::normal)
					.spaced()
					.centred(true)
					.build();
		};
	}
}