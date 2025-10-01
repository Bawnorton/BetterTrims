package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data;

import com.bawnorton.bettertrims.client.mixin.accessor.EnchantmentsPredicateAccessor;
import com.bawnorton.bettertrims.client.tooltip.Styler;
import com.bawnorton.bettertrims.client.tooltip.TrimTooltipPage;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.ItemPredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.PredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.DataComponentMatchersTooltip.PredicateAdder;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.CollectionContentsPredicate;
import net.minecraft.advancements.critereon.CollectionCountsPredicate;
import net.minecraft.advancements.critereon.CollectionPredicate;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.predicates.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface PartialDataComponentPredicateTooltip {
    Map<DataComponentPredicate.Type<?>, PartialAdder<? extends DataComponentPredicate>> PARTIAL_ADDER_MAP = Util.make(
        new HashMap<>(), map -> {
            map.put(
                DataComponentPredicates.DAMAGE,
                (ClientLevel level, DamagePredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    MinMaxBounds.Ints damage = predicate.damage();
                    MinMaxBounds.Ints durability = predicate.durability();

                    builder.space();
                    boolean useAnd = false;
                    if (!damage.isAny()) {
                        PredicateTooltip.addMinMaxToBuilder(key("damage.max_damage"), false, damage, state, builder);
                        useAnd = true;
                    }
                    if (!durability.isAny()) {
                        PredicateTooltip.addMinMaxToBuilder(key("damage.durability"), useAnd, durability, state, builder);
                    }
                }
            );

            PartialAdder<EnchantmentsPredicate> enchantmentAdder = (level, predicate, state, builder) -> {
                EnchantmentsPredicateAccessor enchantmentsPredicate = (EnchantmentsPredicateAccessor) predicate;
                List<EnchantmentPredicate> enchantmentPredicates = enchantmentsPredicate.bettertrims$enchantments();
                if (enchantmentPredicates.isEmpty()) {
                    builder.translate(key("enchantments.any"), Styler::condition);
                    return;
                }
                boolean useAnd = false;
                for (EnchantmentPredicate enchantmentPredicate : enchantmentPredicates) {
                    Optional<HolderSet<Enchantment>> enchantments = enchantmentPredicate.enchantments();
                    MinMaxBounds.Ints enchLevel = enchantmentPredicate.level();
                    CompositeContainerComponent.Builder enchBuilder = new CompositeContainerComponent.Builder();
                    if (enchantments.isPresent()) {
                        HolderSet<Enchantment> enchantmentHolderSet = enchantments.orElseThrow();
                        PredicateTooltip.addRegisteredElementsToBuilder(
                            level,
                            useAnd ? "bettertrims.tooltip.and" : enchantmentHolderSet.size() == 1 ? key("enchantments.matches") : key("enchantments.matches_multiple"),
                            Registries.ENCHANTMENT,
                            enchantmentHolderSet,
                            (enchantment, appender) -> {
                                if (!enchLevel.isAny()) {
                                    appender.space();
                                    PredicateTooltip.addMinMaxToBuilder(
                                        key("enchantments.level"),
                                        false,
                                        enchLevel,
                                        value -> Component.translatable("enchantment.level.%s".formatted("%.0f".formatted(value))),
                                        state, appender
                                    );
                                }
                                return enchantment.description();
                            },
                            state, enchBuilder
                        );
                        useAnd = true;
                    } else {
                        enchBuilder.space()
                            .translate(key("enchantments.any"), Styler::condition)
                            .space();
                        if (!enchLevel.isAny()) {
                            PredicateTooltip.addMinMaxToBuilder(
                                key("enchantments.level"),
                                false,
                                enchLevel,
                                value -> Component.translatable("enchantment.level.%s".formatted("%.0f".formatted(value))),
                                state, enchBuilder
                            );
                        }
                    }
                    builder.component(enchBuilder.build());
                }
            };

            map.put(DataComponentPredicates.ENCHANTMENTS, enchantmentAdder);
            map.put(DataComponentPredicates.STORED_ENCHANTMENTS, enchantmentAdder);

            map.put(
                DataComponentPredicates.POTIONS,
                (ClientLevel level, PotionsPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    HolderSet<Potion> potions = predicate.potions();
                    if (potions.size() == 0) {
                        builder.translate(key("potions.any"), Styler::condition);
                    } else {
                        PredicateTooltip.addRegisteredElementsToBuilder(
                            level,
                            key("potions.matches"),
                            Registries.POTION,
                            potions,
                            (potion, appender) -> {
                                List<MobEffectInstance> effects = potion.getEffects();
                                if (effects.isEmpty()) {
                                    return Component.literal(StringUtils.capitalize(potion.name()));
                                } else {
                                    MobEffectInstance effect = effects.getFirst();
                                    ChatFormatting formatting = effect.getEffect().value().getCategory().getTooltipFormatting();
                                    appender.textComponent(PotionContents.getPotionDescription(effect.getEffect(), effect.getAmplifier()).withStyle(formatting));
                                    for (int i = 1; i < effects.size(); i++) {
                                        effect = effects.get(i);
                                        appender.literal(", ", Styler::condition)
                                            .textComponent(PotionContents.getPotionDescription(effect.getEffect(), effect.getAmplifier()).withStyle(formatting));
                                    }
                                    return Component.literal("");
                                }
                            },
                            state, builder
                        );
                    }
                }
            );

            map.put(
                DataComponentPredicates.CUSTOM_DATA,
                (ClientLevel level, CustomDataPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.component(
                    CompositeContainerComponent.builder()
                        .space()
                        .translate(key("custom_data"), Styler::condition, Styler.value(Component.literal(predicate.value().tag().toString())))
                        .build())
            );

            map.put(
                DataComponentPredicates.CONTAINER,
                (ClientLevel level, ContainerPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    Optional<CollectionPredicate<ItemStack, ItemPredicate>> itemsPredicate = predicate.items();
                    if (itemsPredicate.isPresent()) {
                        addCollectionToBuilder(
                            level,
                            itemsPredicate.orElseThrow(),
                            "container",
                            (ignoredLevel, itemPredicate, predicateState, collectionBuilder) -> ItemPredicateTooltip.addToBuilder(
                                level,
                                itemPredicate,
                                predicateState.withPrefixSpace(false),
                                collectionBuilder
                            ),
                            state, builder
                        );
                    } else {
                        builder.space().translate(key("container.any"), Styler::condition);
                    }
                }
            );

            map.put(
                DataComponentPredicates.BUNDLE_CONTENTS,
                (ClientLevel level, BundlePredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    Optional<CollectionPredicate<ItemStack, ItemPredicate>> itemsPredicate = predicate.items();
                    if (itemsPredicate.isPresent()) {
                        addCollectionToBuilder(
                            level,
                            itemsPredicate.orElseThrow(),
                            "bundle",
                            (ignoredLevel, itemPredicate, predicateState, collectionBuilder) -> ItemPredicateTooltip.addToBuilder(
                                level,
                                itemPredicate,
                                predicateState.withPrefixSpace(false),
                                collectionBuilder
                            ),
                            state, builder
                        );
                    } else {
                        builder.space().translate(key("bundle.any"), Styler::condition);
                    }
                }
            );

            PredicateAdder<FireworkExplosionPredicate.FireworkPredicate> fireworkPredicateAdder = (level, fireworkPredicate, state, builder) -> {
                Optional<FireworkExplosion.Shape> shape = fireworkPredicate.shape();
                Optional<Boolean> trail = fireworkPredicate.trail();
                Optional<Boolean> twinkle = fireworkPredicate.twinkle();

                boolean useAnd = false;
                builder.space();
                if (shape.isEmpty() && trail.isEmpty() && twinkle.isEmpty()) {
                    builder.translate(key("firework.any"), Styler::condition);
                    return;
                }

                builder.translate(key("firework.matches"), Styler::condition)
                    .space();

                if (shape.isPresent()) {
                    builder.translate(key("firework.shape"), Styler::condition, Styler.name(shape.orElseThrow().getName()));
                    useAnd = true;
                }

                if (trail.isPresent()) {
                    if (useAnd) {
                        builder.space()
                            .translate("bettertrims.tooltip.and", Styler::condition)
                            .space();
                    }
                    builder.translate(
                        key(trail.orElseThrow() ? "firework.trail" : "firework.no_trail"),
                        Styler::condition
                    );
                    useAnd = true;
                }

                if (twinkle.isPresent()) {
                    if (useAnd) {
                        builder.space()
                            .translate("bettertrims.tooltip.and", Styler::condition)
                            .space();
                    }
                    builder.translate(
                        key(twinkle.orElseThrow() ? "firework.twinkle" : "firework.no_twinkle"),
                        Styler::condition
                    );
                }
            };

            map.put(
                DataComponentPredicates.FIREWORK_EXPLOSION,
                (ClientLevel level, FireworkExplosionPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> fireworkPredicateAdder.addToBuilder(
                    level,
                    predicate.predicate(),
                    state, builder
                )
            );

            map.put(
                DataComponentPredicates.FIREWORKS,
                (ClientLevel level, FireworksPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    MinMaxBounds.Ints flightDuration = predicate.flightDuration();
                    Optional<CollectionPredicate<FireworkExplosion, FireworkExplosionPredicate.FireworkPredicate>> explosions = predicate.explosions();

                    if (!flightDuration.isAny()) {
                        builder.space()
                            .translate(key("fireworks.flight_duration"), Styler::condition);
                        PredicateTooltip.addMinMaxToBuilder(
                            key("fireworks.flight_duration"),
                            false,
                            flightDuration,
                            v -> Component.literal("%.0fs".formatted(v)),
                            state,
                            builder
                        );
                        builder.space()
                            .translate("bettertrims.tooltip.and", Styler::condition);
                    } else {
                        builder.space()
                            .translate(key("fireworks.flight_duration.any"), Styler::condition);
                    }

                    builder.cycle(cycleBuilder -> {
                        CollectionPredicate<FireworkExplosion, FireworkExplosionPredicate.FireworkPredicate> fireworkCollection = explosions.orElseThrow();
                        CompositeContainerComponent.Builder fireworkBuilder = new CompositeContainerComponent.Builder();
                        addCollectionToBuilder(level, fireworkCollection, "fireworks", fireworkPredicateAdder, state, fireworkBuilder);
                        cycleBuilder.component(fireworkBuilder.build());
                    });
                }
            );

            map.put(
                DataComponentPredicates.WRITABLE_BOOK,
                (ClientLevel level, WritableBookPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    Optional<CollectionPredicate<Filterable<String>, WritableBookPredicate.PagePredicate>> pages = predicate.pages();

                    builder.space()
                        .translate(key("writable_book"), Styler::condition);
                    if (pages.isPresent()) {
                        addCollectionToBuilder(
                            level,
                            pages.orElseThrow(),
                            "writable_book",
                            (ignoredLevel, pagePredicate, predicateState, collectionBuilder) -> {
                                String contents = pagePredicate.contents();
                                if (contents.isEmpty()) {
                                    collectionBuilder.translate(key("writable_book.page.any"), Styler::condition);
                                } else {
                                    collectionBuilder.translate(
                                        key("writable_book.page.contains"),
                                        Styler::condition,
                                        Styler.value(Component.literal("\"%s\"".formatted(contents)))
                                    );
                                }
                            },
                            state,
                            builder
                        );
                    }
                }
            );

            map.put(
                DataComponentPredicates.WRITTEN_BOOK,
                (ClientLevel level, WrittenBookPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    Optional<CollectionPredicate<Filterable<Component>, WrittenBookPredicate.PagePredicate>> pages = predicate.pages();
                    if (pages.isPresent()) {
                        addCollectionToBuilder(
                            level,
                            pages.orElseThrow(),
                            "written_book",
                            (ignoredLevel, pagePredicate, predicateState, collectionBuilder) -> {
                                Component contents = pagePredicate.contents();
                                collectionBuilder.translate(key("written_book.page.contains"), Styler::condition, Styler.value(contents.copy()));
                            },
                            state,
                            builder
                        );
                    } else {
                        builder.space().translate(key("written_book.any"), Styler::condition);
                    }
                }
            );

            map.put(
                DataComponentPredicates.ATTRIBUTE_MODIFIERS,
                (ClientLevel level, AttributeModifiersPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    Optional<CollectionPredicate<ItemAttributeModifiers.Entry, AttributeModifiersPredicate.EntryPredicate>> modifiers = predicate.modifiers();
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
                                                CompositeContainerComponent.Builder detailBuilder = new CompositeContainerComponent.Builder();
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
                                    List<EquipmentSlot> slots = slot.orElseThrow().slots();
                                    if (!slots.isEmpty()) {
                                        PredicateTooltip.addEnumListToBuilder(
                                            key("attribute_modifiers.slot"),
                                            slots,
                                            s -> Component.translatable(StringUtils.capitalize(s.getName())),
                                            predicateState,
                                            collectionBuilder
                                        );
                                    }
                                }
                            },
                            state,
                            builder
                        );
                    } else {
                        builder.space().translate(key("attribute_modifiers.any"), Styler::condition);
                    }
                }
            );

            map.put(
                DataComponentPredicates.ARMOR_TRIM,
                (ClientLevel level, TrimPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    Optional<HolderSet<TrimPattern>> pattern = predicate.pattern();
                    Optional<HolderSet<TrimMaterial>> material = predicate.material();
                    if (pattern.isEmpty() && material.isEmpty()) {
                        builder.space().translate(key("armor_trim.any"), Styler::condition);
                        return;
                    }

                    builder.space()
                        .translate(key("armor_trim.matches"), Styler::condition)
                        .space()
                        .centred()
                        .component(TrimTooltipPage.generateMatcherComponent(level, material.orElse(HolderSet.direct()), pattern.orElse(HolderSet.direct())));
                }
            );
        }
    );

    static <T, P extends Predicate<T>> void addCollectionToBuilder(ClientLevel level, CollectionPredicate<T, P> collection, String key, PredicateAdder<P> contentAdder, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        Optional<CollectionContentsPredicate<T, P>> contains = collection.contains();
        Optional<CollectionCountsPredicate<T, P>> counts = collection.counts();
        Optional<MinMaxBounds.Ints> size = collection.size();
        if ((contains.isEmpty() || contains.orElseThrow().unpack().isEmpty())
            && (counts.isEmpty() || counts.orElseThrow().unpack().isEmpty())
            && (size.isEmpty() || size.orElseThrow().isAny())) {
            builder.space().translate(key(key + ".any"), Styler::condition);
            return;
        }

        builder.cycle(cycleBuilder -> {
            if (contains.isPresent()) {
                CollectionContentsPredicate<T, P> collectionContentsPredicate = contains.get();
                List<P> contentPredicates = collectionContentsPredicate.unpack();
                BiConsumer<CompositeContainerComponent.Builder, P> contentHeader = (contentPredicateBuilder, contentPredicate) -> {
                    contentAdder.addToBuilder(level, contentPredicate, state, contentPredicateBuilder);
                    cycleBuilder.component(contentPredicateBuilder.build());
                };
                if (contentPredicates.size() == 1) {
                    contentHeader.accept(
                        CompositeContainerComponent.builder()
                            .space()
                            .translate(key(key + ".single"), Styler::condition)
                            .space(),
                        contentPredicates.getFirst()
                    );
                } else {
                    for (P contentPredicate : contentPredicates) {
                        contentHeader.accept(
                            CompositeContainerComponent.builder()
                                .space()
                                .translate(key(key + ".all_of"), Styler::condition)
                                .space(),
                            contentPredicate
                        );
                    }
                }
            }

            if (counts.isPresent()) {
                CollectionCountsPredicate<T, P> collectionCountsPredicate = counts.get();
                List<CollectionCountsPredicate.Entry<T, P>> countPredicates = collectionCountsPredicate.unpack();
                BiConsumer<CompositeContainerComponent.Builder, CollectionCountsPredicate.Entry<T, P>> countHeader = (contentPredicateBuilder, countPredicateEntry) -> {
                    MinMaxBounds.Ints count = countPredicateEntry.count();
                    P countPredicate = countPredicateEntry.test();
                    contentAdder.addToBuilder(level, countPredicate, state, contentPredicateBuilder);
                    contentPredicateBuilder.literal("\"", Styler::condition).space();
                    PredicateTooltip.addMinMaxToBuilder(
                        key(key + ".count"),
                        false,
                        count,
                        state.withPrefixSpace(false),
                        contentPredicateBuilder
                    );
                    state.withPrefixSpace(true);
                    cycleBuilder.component(contentPredicateBuilder.build());
                };
                if (countPredicates.size() == 1) {
                    countHeader.accept(
                        CompositeContainerComponent.builder()
                            .space()
                            .translate(key(key + ".single"), Styler::condition)
                            .space()
                            .literal("\"", Styler::condition),
                        countPredicates.getFirst()
                    );
                } else {
                    for (CollectionCountsPredicate.Entry<T, P> countPredicateEntry : countPredicates) {
                        countHeader.accept(
                            CompositeContainerComponent.builder()
                                .space()
                                .translate(key(key + ".all_of"), Styler::condition)
                                .space()
                                .literal("\"", Styler::condition),
                            countPredicateEntry
                        );
                    }
                }
            }

            if (size.isPresent()) {
                MinMaxBounds.Ints sizeBounds = size.orElseThrow();
                CompositeContainerComponent.Builder itemPredicateBuilder = new CompositeContainerComponent.Builder();
                itemPredicateBuilder.space()
                    .translate(key(key + ".size"), Styler::condition);
                PredicateTooltip.addMinMaxToBuilder(
                    key(key + ".size"),
                    false,
                    sizeBounds,
                    state,
                    itemPredicateBuilder
                );
                cycleBuilder.component(itemPredicateBuilder.build());
            }

        });
    }

    @SuppressWarnings("unchecked")
    static void addToBuilder(ClientLevel level, DataComponentPredicate.Type<?> type, DataComponentPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        PartialAdder<DataComponentPredicate> adder = (PartialAdder<DataComponentPredicate>) PARTIAL_ADDER_MAP.getOrDefault(type, PartialAdder.UNKNOWN.apply(type));
        adder.addToBuilder(level, predicate, state, builder);
    }

    static String key(String key) {
        return PredicateTooltip.key("data.partial.%s".formatted(key));
    }

    interface PartialAdder<T extends DataComponentPredicate> {
        Function<DataComponentPredicate.Type<?>, PartialAdder<? extends DataComponentPredicate>> UNKNOWN = type -> (level, predicate, state, builder) -> builder.translate(
            key("unknown_type"),
            Styler::property,
            type.toString()
        );

        void addToBuilder(ClientLevel level, T predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder);
    }
}
