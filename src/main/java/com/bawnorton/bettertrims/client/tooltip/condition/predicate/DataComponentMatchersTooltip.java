package com.bawnorton.bettertrims.client.tooltip.condition.predicate;

import com.bawnorton.bettertrims.client.mixin.accessor.EnchantmentsPredicateAccessor;
import com.bawnorton.bettertrims.client.tooltip.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.DataComponentMatchers;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.predicates.DamagePredicate;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.PotionsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public interface DataComponentMatchersTooltip {
    Map<DataComponentPredicate.Type<?>, PartialAdder> PARTIAL_ADDER_MAP = Util.make(new HashMap<>(), map -> {
        map.put(DataComponentPredicates.DAMAGE, (level, predicate, builder) -> {
            DamagePredicate damagePredicate = (DamagePredicate) predicate;
            MinMaxBounds.Ints damage = damagePredicate.damage();
            MinMaxBounds.Ints durability = damagePredicate.durability();

            builder.space();
            boolean useAnd = false;
            if(!damage.isAny()) {
                PredicateTooltip.addMinMaxToBuilder(key("damage.max_damage"), false, damage, builder);
                useAnd = true;
            }
            if(!durability.isAny()) {
                PredicateTooltip.addMinMaxToBuilder(key("damage.durability"), useAnd, durability, builder);
            }
        });

        PartialAdder enchantmentAdder = (level, predicate, builder) -> {
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
                                    appender
                                );
                            }
                            return enchantment.description();
                        },
                        enchBuilder
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
                            enchBuilder
                        );
                    }
                }
                builder.component(enchBuilder.build());
            }
        };

        map.put(DataComponentPredicates.ENCHANTMENTS, enchantmentAdder);
        map.put(DataComponentPredicates.STORED_ENCHANTMENTS, enchantmentAdder);

        map.put(DataComponentPredicates.POTIONS, (level, predicate, builder) -> {
            PotionsPredicate potionsPredicate = (PotionsPredicate) predicate;
            HolderSet<Potion> potions = potionsPredicate.potions();
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
                    builder
                );
            }
        });
    });

    static void addToBuilder(ClientLevel level, DataComponentMatchers components, CompositeContainerComponent.Builder builder) {
        if(components.isEmpty()) {
            builder.translate(key("any"), Styler::condition);
            return;
        }

        DataComponentExactPredicate exact = components.exact();
        if(!exact.isEmpty()) {
            addDataComponentExactPredicateToBuilder(level, exact, builder);
        }

        Map<DataComponentPredicate.Type<?>, DataComponentPredicate> partial = components.partial();
        if(!partial.isEmpty()) {
            addPartialDataComponentPredicatesToBuilder(level, partial, builder);
        }
    }

    static String key(String key) {
        return PredicateTooltip.key("data.%s".formatted(key));
    }

    // the tool is / is not
    static void addDataComponentExactPredicateToBuilder(ClientLevel level, DataComponentExactPredicate exact, CompositeContainerComponent.Builder builder) {

    }

    // the tool is / is not
    static void addPartialDataComponentPredicatesToBuilder(ClientLevel level, Map<DataComponentPredicate.Type<?>, DataComponentPredicate> partial, CompositeContainerComponent.Builder builder) {
        if(partial.isEmpty()) {
            builder.translate(key("unknown"));
        } else if (partial.size() == 1) {
            Map.Entry<DataComponentPredicate.Type<?>, DataComponentPredicate> entry = partial.entrySet().iterator().next();
            DataComponentPredicate.Type<?> type = entry.getKey();
            DataComponentPredicate predicate = entry.getValue();
            PartialAdder adder = PARTIAL_ADDER_MAP.getOrDefault(type, PartialAdder.UNKNOWN.apply(type));
            adder.addToBuilder(level, predicate, builder);
        } else {
            CompositeContainerComponent.Builder listBuilder = new CompositeContainerComponent.Builder()
                .vertical()
                .literal(":", Styler::condition);
            for (Map.Entry<DataComponentPredicate.Type<?>, DataComponentPredicate> entry : partial.entrySet()) {
                CompositeContainerComponent.Builder termBuilder = new CompositeContainerComponent.Builder()
                    .space()
                    .literal("• ", Styler::condition);
                DataComponentPredicate.Type<?> type = entry.getKey();
                DataComponentPredicate predicate = entry.getValue();
                PartialAdder adder = PARTIAL_ADDER_MAP.getOrDefault(type, PartialAdder.UNKNOWN.apply(type));
                adder.addToBuilder(level, predicate, termBuilder);
                listBuilder.component(termBuilder.build());
            }
            builder.component(listBuilder.build());
        }
    }

    interface PartialAdder {
        Function<DataComponentPredicate.Type<?>, PartialAdder> UNKNOWN = type -> (level, predicate, builder) -> builder.translate(key("unknown_type"), Styler::property, type.toString());

        void addToBuilder(ClientLevel level, DataComponentPredicate predicate, CompositeContainerComponent.Builder builder);
    }
}
