package com.bawnorton.bettertrims.client.tooltip;

import com.bawnorton.bettertrims.client.BetterTrimsClient;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeTooltipComponent;
import com.bawnorton.bettertrims.property.Matcher;
import com.bawnorton.bettertrims.property.TrimProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
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

/**
 * Trim Properties - [1/n]
 * {[Material Provider Icon] [&] [Pattern Provider Icon]}
 * Any of [Slots]
 * {Ability Icon} {Name} - [Conditions]
 * ...
 * All of [Slots]
 * {Ability Icon} {Name} - [Conditions]
 * ...
 * As Item
 * {Property Icon} {Name} - [Conditions]
 * ...
 */
public class TrimPropertiesTooltip {
    private final ArmorTrim trim;
    private final Map<Matcher, TrimProperty> properties;
    private final List<TooltipPage> pages;

    private int frameTracker;
    private int counter;

    private int index;

    public TrimPropertiesTooltip(ArmorTrim trim, Map<Matcher, TrimProperty> properties) {
        this.trim = trim;
        this.properties = properties;
        this.pages = new ArrayList<>();
        generateEntries();
    }

    private void generateEntries() {
        for (Map.Entry<Matcher, TrimProperty> entry : properties.entrySet()) {
            Matcher matcher = entry.getKey();
            TrimProperty property = entry.getValue();
            pages.add(new TooltipPage(matcher, property));
        }
    }

    public void render(GuiGraphics graphics, ClientLevel level, Font font, Rect2i parentTooltipBounds, boolean horizontallyFlipped, ResourceLocation background) {
        List<ClientTooltipComponent> components = new ArrayList<>();

        TooltipPage page = pages.get(index);
        page.addComponents(components::add, level, index, pages.size());

        if (++frameTracker >= Minecraft.getInstance().getFps()) {
            frameTracker = 0;
            counter++;
        }

        int offsetX = parentTooltipBounds.getX() + parentTooltipBounds.getWidth() + (horizontallyFlipped ? 14 : 0);
        int offsetY = parentTooltipBounds.getY();

        graphics.renderTooltip(
            font,
            components,
            offsetX,
            offsetY,
            new ExpandedTooltipPositioner(parentTooltipBounds.getWidth()),
            background
        );
    }

    private static class TooltipPage {
        private final Matcher matcher;
        private final TrimProperty property;
        private final List<ClientTooltipComponent> components;

        public TooltipPage(Matcher matcher, TrimProperty property) {
            this.matcher = matcher;
            this.property = property;
            this.components = new ArrayList<>();
        }

        public void addComponents(Consumer<ClientTooltipComponent> componentAdder, ClientLevel level, int index, int total) {
            generateComponents(level, index, total);
            components.forEach(componentAdder);
        }

        public void generateComponents(ClientLevel level, int index, int total) {
            if(!components.isEmpty()) return;

            components.add(generateTitle(level, index, total));
        }

        private ClientTooltipComponent generateTitle(ClientLevel level, int index, int total) {
            CompositeTooltipComponent.Builder builder = CompositeTooltipComponent.builder()
                .translate("bettertrims.tooltip.properties.title")
                .literal("(")
                .component(generateMatcherComponent(level))
                .literal(")");
            if (total > 1) {
                builder.literal(" - [" + (index + 1) + "/" + total + "]");
            }
            return builder.build();
        }

        private ClientTooltipComponent generateMatcherComponent(ClientLevel level) {
            List<TrimMaterial> materials = matcher.material().stream().map(Holder::value).toList();
            List<TrimPattern> patterns = matcher.pattern().stream().map(Holder::value).toList();
            if(materials.isEmpty()) {
                return generateAnyMaterialComponent(patterns, level);
            } else if (patterns.isEmpty()) {
                return generateAnyPatternComponent(materials, level);
            } else {
                return generateSpecificMaterialPatternComponent(patterns, materials, level);
            }
        }

        private ClientTooltipComponent generateAnyMaterialComponent(List<TrimPattern> patterns, ClientLevel level) {
            if(patterns.isEmpty()) {
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

        private ClientTooltipComponent generateMatcherComponentFor(List<Item> materialProviders, List<Item> patternProviders) {
            return CompositeTooltipComponent.builder()
                .cycle(materialProviders)
                .literal("-")
                .cycle(patternProviders)
                .build();
        }

        private List<Item> getMaterialProviders(List<TrimMaterial> materials, ClientLevel level) {
            return getAllMaterialProviders()
                .stream()
                .filter(item -> {
                    ProvidesTrimMaterial providesTrimMaterial = item.components().get(DataComponents.PROVIDES_TRIM_MATERIAL);
                    if (providesTrimMaterial == null) return false;

                    TrimMaterial material = providesTrimMaterial.material()
                        .unwrap(level.registryAccess().lookupOrThrow(Registries.TRIM_MATERIAL))
                        .orElse(null);
                    return material != null && materials.contains(material);
                })
                .toList();
        }

        private static @NotNull List<Item> getPatternProviders(List<TrimPattern> patterns, ClientLevel level) {
            List<Item> patternProviders = new ArrayList<>();
            for (Map.Entry<Holder<TrimPattern>, HolderSet<Item>> entry : BetterTrimsClient.getPatternSources().entrySet()) {
                Holder<TrimPattern> patternHolder = entry.getKey();
                TrimPattern pattern = patternHolder.unwrap()
                    .map(key -> level.registryAccess().lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(key).value(), Function.identity());
                if(patterns.contains(pattern)) {
                    HolderSet<Item> itemHolders = entry.getValue();
                    patternProviders.addAll(itemHolders.stream()
                        .map(holder -> holder.unwrap().map(key -> BuiltInRegistries.ITEM.getOrThrow(key).value(), Function.identity()))
                        .toList());
                }
            }
            return patternProviders;
        }

        private List<Item> getAllMaterialProviders() {
            return BuiltInRegistries.ITEM.getOrThrow(ItemTags.TRIM_MATERIALS)
                .stream()
                .map(holder -> holder.unwrap()
                    .map(key -> BuiltInRegistries.ITEM.getOrThrow(key).value(), Function.identity()))
                .toList();
        }

        private List<Item> getAllPatternProviders() {
            return BetterTrimsClient.getPatternSources()
                .values()
                .stream()
                .map(HolderSet::stream)
                .flatMap(stream -> stream.map(holder -> holder.unwrap()
                    .map(key -> BuiltInRegistries.ITEM.getOrThrow(key).value(), Function.identity())))
                .toList();
        }
    }
}
