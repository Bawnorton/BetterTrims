package com.bawnorton.bettertrims.client.tooltip;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.BetterTrimsClient;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.component.GapComponent;
import com.bawnorton.bettertrims.client.tooltip.component.ItemComponent;
import com.bawnorton.bettertrims.property.Matcher;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.element.ConditionalElement;
import com.bawnorton.bettertrims.property.element.ConditionalElementMatcher;
import com.bawnorton.bettertrims.property.element.TrimElement;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ProvidesTrimMaterial;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class TrimTooltipPage {
    private final Matcher matcher;
    private final TrimProperty property;
    private final List<ClientTooltipComponent> components;

    public TrimTooltipPage(TrimProperty property, Matcher matcher) {
        this.matcher = matcher;
        this.property = property;
        this.components = new ArrayList<>();
    }

    public List<ClientTooltipComponent> getComponents() {
        return components;
    }

    @SuppressWarnings("unchecked")
    public void generateComponents(ClientLevel level, ArmorTrim trim, Font font, int index, int total) {
        components.clear();

        CompositeContainerComponent.Builder pageBuilder = CompositeContainerComponent.builder().vertical();
        pageBuilder.component(generateTitle(level, index, total));

        boolean addHeader = true;
        for (DataComponentType<?> type : BetterTrimsRegistries.TRIM_ABILITY_COMPONENT_TYPE) {
            DataComponentType<List<ConditionalElement<TrimElement>>> castType = (DataComponentType<List<ConditionalElement<TrimElement>>>) type;
            CompositeContainerComponent abilityComponent = generateAbilityTooltipComponent(castType, level);
            if (abilityComponent == null) continue;

            if (addHeader) {
                pageBuilder.component(generateWearingAbilityHeader(trim));
                addHeader = false;
            }

            Component typeTooltip = TrimAbilityComponents.TOOLTIPS.get(BetterTrimsRegistries.TRIM_ABILITY_COMPONENT_TYPE.getKey(type));
            if (typeTooltip == null) {
                typeTooltip = Component.translatable("bettertrims.tooltip.component.unknown");
            }

            CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder()
                .vertical()
                .component(CompositeContainerComponent.builder()
                    .space()
                    .textComponent(Styler.component(typeTooltip.copy()))
                    .build());
            if(!abilityComponent.isEmpty()) {
                builder.component(CompositeContainerComponent.builder()
                    .space()
                    .space()
                    .component(abilityComponent)
                    .build());
            }
            pageBuilder.component(builder.build());
        }

        CompositeContainerComponent page = pageBuilder.build();
        components.addAll(page.getComponents());
        components.add(new GapComponent(page.getMaxWidth(font), 0));
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
                .map(key -> level.registryAccess().lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(key).value(), Function.identity());
            if (patterns.contains(pattern)) {
                HolderSet<Item> itemHolders = entry.getValue();
                patternProviders.addAll(itemHolders.stream()
                    .map(holder -> holder.unwrap().map(key -> BuiltInRegistries.ITEM.getOrThrow(key).value(), Function.identity()))
                    .map(Item::getDefaultInstance)
                    .toList());
            }
        }
        return patternProviders;
    }

    private ClientTooltipComponent generateTitle(ClientLevel level, int index, int total) {
        ResourceLocation propertyId = level.registryAccess().lookupOrThrow(BetterTrimsRegistries.Keys.TRIM_PROPERTIES).getKey(property);
        if (propertyId == null) {
            BetterTrims.LOGGER.warn("Property {} does not have a registry name", property);
            propertyId = BetterTrims.rl("unknown");
        }
        CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder()
            .translate("bettertrims.property.%s.%s".formatted(propertyId.getNamespace(), propertyId.getPath()), Styler::trim)
            .space()
            .literal("(", Styler::trim)
            .component(generateMatcherComponent(level))
            .literal(")", Styler::trim)
            .centred();
        if (total > 1) {
            builder.literal(" - [" + (index + 1) + "/" + total + "]", Styler::trim);
        }
        return builder.build();
    }

    private CompositeContainerComponent generateAbilityTooltipComponent(DataComponentType<List<ConditionalElement<TrimElement>>> type, ClientLevel level) {
        List<ConditionalElementMatcher<?>> elements = property.getAbilityElements(type);
        if (elements.isEmpty()) return null;

        CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder()
            .vertical();
        for (ConditionalElementMatcher<?> element : elements) {
            builder.component(element.getConditionalElement().getTooltip(level));
        }
        return builder.build();
    }

    private ClientTooltipComponent generateWearingAbilityHeader(ArmorTrim trim) {
        int minCount = matcher.minCount();
        List<ItemStack> trimmedItems = new ArrayList<>();
        for (Item item : List.of(Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS)) {
            ItemStack stack = item.getDefaultInstance();
            stack.set(DataComponents.TRIM, trim);
            trimmedItems.add(stack);
        }
        return switch (minCount) {
            case 4 -> CompositeContainerComponent.builder()
                .translate("bettertrims.tooltip.properties.count.all_of", Styler::normal)
                .stack(trimmedItems, 12)
                .translate("bettertrims.tooltip.properties.count.equipped", Styler::normal)
                .spaced()
                .centred()
                .build();
            case 1 -> CompositeContainerComponent.builder()
                .translate("bettertrims.tooltip.properties.count.any_of", Styler::normal)
                .cycle(builder -> trimmedItems.stream().map(ItemComponent::new).forEach(builder::component))
                .translate("bettertrims.tooltip.properties.count.equipped", Styler::normal)
                .spaced()
                .centred()
                .build();
            default -> CompositeContainerComponent.builder()
                .translate("bettertrims.tooltip.properties.count.n_of", Styler::normal, minCount)
                .stack(trimmedItems, 12)
                .translate("bettertrims.tooltip.properties.count.equipped", Styler::normal)
                .spaced()
                .centred()
                .build();
        };
    }

    private ClientTooltipComponent generateMatcherComponent(ClientLevel level) {
        List<TrimMaterial> materials = matcher.material().stream().map(Holder::value).toList();
        List<TrimPattern> patterns = matcher.pattern().stream().map(Holder::value).toList();
        if (materials.isEmpty()) {
            return generateAnyMaterialComponent(patterns, level);
        } else if (patterns.isEmpty()) {
            return generateAnyPatternComponent(materials, level);
        } else {
            return generateSpecificMaterialPatternComponent(patterns, materials, level);
        }
    }

    private ClientTooltipComponent generateAnyMaterialComponent(List<TrimPattern> patterns, ClientLevel level) {
        if (patterns.isEmpty()) {
            return generateAnyMaterialOrPatternComponent();
        }

        return generateMatcherComponentFor(getAllMaterialProviders(), getPatternProviders(patterns, level));
    }

    private ClientTooltipComponent generateAnyPatternComponent(List<TrimMaterial> materials, ClientLevel level) {
        if (materials.isEmpty()) {
            return generateAnyMaterialOrPatternComponent();
        }

        return generateMatcherComponentFor(getMaterialProviders(materials, level), getAllPatternProviders());
    }

    private ClientTooltipComponent generateAnyMaterialOrPatternComponent() {
        return generateMatcherComponentFor(getAllMaterialProviders(), getAllPatternProviders());
    }

    private ClientTooltipComponent generateSpecificMaterialPatternComponent(List<TrimPattern> patterns, List<TrimMaterial> materials, ClientLevel level) {
        return generateMatcherComponentFor(getMaterialProviders(materials, level), getPatternProviders(patterns, level));
    }

    private ClientTooltipComponent generateMatcherComponentFor(List<ItemStack> materialProviders, List<ItemStack> patternProviders) {
        return CompositeContainerComponent.builder()
            .cycle(builder -> materialProviders.stream().map(ItemComponent::new).forEach(builder::component))
            .literal("-", Styler::trim)
            .cycle(builder -> patternProviders.stream().map(ItemComponent::new).forEach(builder::component))
            .spaced()
            .centred()
            .build();
    }

    private List<ItemStack> getMaterialProviders(List<TrimMaterial> materials, ClientLevel level) {
        return getAllMaterialProviders()
            .stream()
            .filter(item -> {
                ProvidesTrimMaterial providesTrimMaterial = item.get(DataComponents.PROVIDES_TRIM_MATERIAL);
                if (providesTrimMaterial == null) return false;

                TrimMaterial material = providesTrimMaterial.material()
                    .unwrap(level.registryAccess().lookupOrThrow(Registries.TRIM_MATERIAL))
                    .orElse(null);
                return material != null && materials.contains(material);
            })
            .toList();
    }

    private List<ItemStack> getAllMaterialProviders() {
        return BuiltInRegistries.ITEM.getOrThrow(ItemTags.TRIM_MATERIALS)
            .stream()
            .map(holder -> holder.unwrap()
                .map(key -> BuiltInRegistries.ITEM.getOrThrow(key).value(), Function.identity()))
            .map(Item::getDefaultInstance)
            .toList();
    }

    private List<ItemStack> getAllPatternProviders() {
        return BetterTrimsClient.getPatternSources()
            .values()
            .stream()
            .map(HolderSet::stream)
            .flatMap(stream -> stream.map(holder -> holder.unwrap()
                .map(key -> BuiltInRegistries.ITEM.getOrThrow(key).value(), Function.identity())))
            .map(Item::getDefaultInstance)
            .toList();
    }
}