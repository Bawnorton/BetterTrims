package com.bawnorton.bettertrims.client.tooltip.condition;

import com.bawnorton.bettertrims.client.mixin.accessor.CompositeLootItemConditionAccessor;
import com.bawnorton.bettertrims.client.mixin.accessor.IntRangeAccessor;
import com.bawnorton.bettertrims.client.tooltip.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.BlockPredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.EntityPredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.ItemPredicateTooltip;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.EnchantmentLevelProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.ScoreboardValue;
import net.minecraft.world.level.storage.loot.providers.number.StorageValue;
import net.minecraft.world.level.storage.loot.providers.score.ContextScoreboardNameProvider;
import net.minecraft.world.level.storage.loot.providers.score.FixedScoreboardNameProvider;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface LootConditionTooltips {
    Map<LootItemConditionType, LootTooltipFactory> TOOLTIPS = Util.make(
        new HashMap<>(), map -> {
            map.put(
                LootItemConditions.INVERTED, (level, condition, parentBuilder, state) -> {
                    InvertedLootItemCondition invertedCondition = (InvertedLootItemCondition) condition;
                    LootItemCondition term = invertedCondition.term();
                    CompositeContainerComponent.Builder builder = new CompositeContainerComponent.Builder();
                    addConditionToTooltip(level, builder, term, state.invert());
                    parentBuilder.component(builder.build());
                }
            );

            LootTooltipFactory compositeFactory = (level, condition, parentBuilder, state) -> {
                CompositeLootItemConditionAccessor composite = (CompositeLootItemConditionAccessor) condition;
                CompositeContainerComponent.Builder builder = new CompositeContainerComponent.Builder()
                    .translate("bettertrims.tooltip.condition.%s.%s".formatted(condition instanceof AllOfCondition ? "all_of" : "any_of", state.key()), Styler::condition)
                    .vertical();
                for (LootItemCondition term : composite.bettertrims$terms()) {
                    CompositeContainerComponent.Builder termBuilder = new CompositeContainerComponent.Builder()
                        .space()
                        .literal("• ", Styler::condition);
                    addConditionToTooltip(level, termBuilder, term, state.withInverted(false));
                    builder.component(termBuilder.build());
                }
                parentBuilder.component(builder.build());
            };

            map.put(LootItemConditions.ANY_OF, compositeFactory);
            map.put(LootItemConditions.ALL_OF, compositeFactory);

            map.put(
                LootItemConditions.RANDOM_CHANCE, (level, condition, parentBuilder, state) -> {
                    LootItemRandomChanceCondition chanceCondition = (LootItemRandomChanceCondition) condition;
                    parentBuilder.cycle(chanceCycler -> {
                        List<Component> numberComp = numberCompFromNumberProvider(chanceCondition.chance(), value -> {
                            float percentage = value * 100;
                            if (state.isInverted()) {
                                percentage = 100 - percentage;
                            }
                            if(percentage % 1 == 0) {
                                return "%.0f%%".formatted(percentage);
                            } else if(percentage * 10 % 1 == 0) {
                                return "%.1f%%".formatted(percentage);
                            } else {
                                return "%.2f%%".formatted(percentage);
                            }
                        }, comp -> comp.copy().append("%"));
                        Component tooltip = Styler.condition(Component.translatable("bettertrims.tooltip.condition.random_chance", numberComp));
                        chanceCycler.textComponent(tooltip);
                    });
                }
            );

            map.put(
                LootItemConditions.RANDOM_CHANCE_WITH_ENCHANTED_BONUS, (level, condition, parentBuilder, state) -> {
                    LootItemRandomChanceWithEnchantedBonusCondition chanceCondition = (LootItemRandomChanceWithEnchantedBonusCondition) condition;
                    Registry<Enchantment> registry = level.registryAccess().
                        lookupOrThrow(Registries.ENCHANTMENT);
                    Enchantment enchantment = chanceCondition.enchantment()
                        .unwrap()
                        .map(registry::getValueOrThrow, Function.identity());
                    Map<Integer, Float> chanceMap = new HashMap<>();
                    for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                        chanceMap.put(i, chanceCondition.enchantedChance().calculate(i));
                    }
                    Component enchantmentName = Styler.name(enchantment.description().copy());
                    parentBuilder.component(CompositeContainerComponent.builder()
                        .translate("bettertrims.tooltip.condition.random_chance_with_enchanted_bonus.enchanted.%s".formatted(state.key()), Styler::condition, enchantmentName)
                        .space()
                        .cycle(levelCycler -> chanceMap.forEach((enchantLevel, chance) -> {
                            MutableComponent levelComp = Styler.value(Component.translatable("enchantment.level." + enchantLevel)).append(": ");
                            MutableComponent chanceComp = Styler.number(Component.literal("%.0f".formatted(chance * 100) + "%"));
                            levelCycler.textComponent(levelComp.append(chanceComp));
                        }))
                        .literal(",")
                        .space()
                        .translate(
                            "bettertrims.tooltip.condition.random_chance_with_enchanted_bonus.else",
                            Styler::condition,
                            Styler.number(Component.literal("%.0f".formatted(chanceCondition.unenchantedChance() * 100) + "%"))
                        )
                        .build());
                }
            );

            map.put(LootItemConditions.ENTITY_PROPERTIES, (level, condition, parentBuilder, state) -> {
                LootItemEntityPropertyCondition entityCondition = (LootItemEntityPropertyCondition) condition;
                Optional<EntityPredicate> predicate = entityCondition.predicate();
                if(predicate.isEmpty()) return;

                Component target = Styler.target(Component.translatable("bettertrims.tooltip.condition.entity_properties.%s.%s".formatted(state.key(), entityCondition.entityTarget().getSerializedName())));
                CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder().vertical();
                EntityPredicateTooltip.addToBuilder(level, predicate.orElseThrow(), builder);
                parentBuilder.textComponent(target).component(builder.build());
            });

            map.put(LootItemConditions.KILLED_BY_PLAYER, (level, condition, parentBuilder, state) -> {
                parentBuilder.textComponent(Styler.condition(Component.translatable("bettertrims.tooltip.condition.killed_by_player.%s".formatted(state.key()))));
            });

            map.put(LootItemConditions.ENTITY_SCORES, (level, condition, parentBuilder, state) -> {
                EntityHasScoreCondition entityHasScoreCondition = (EntityHasScoreCondition) condition;
                Component target = Styler.target(Component.translatable("bettertrims.tooltip.condition.entity_properties.%s.%s".formatted(state.key(), entityHasScoreCondition.entityTarget().getSerializedName())));
                Map<String, IntRange> scores = entityHasScoreCondition.scores();
                CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
                builder.cycle(scoreCycler -> scores.forEach((score, range) -> {
                    IntRangeAccessor accessor = (IntRangeAccessor) range;
                    NumberProvider min = accessor.bettertrims$min();
                    NumberProvider max = accessor.bettertrims$max();

                    Function<Float, String> formatter = value -> {
                        if (value % 1 == 0) {
                            return "%.0f".formatted(value);
                        } else if (value * 10 % 1 == 0) {
                            return "%.1f".formatted(value);
                        } else {
                            return "%.2f".formatted(value);
                        }
                    };

                    List<Component> minComps = min == null ? null : numberCompFromNumberProvider(min, formatter, comp -> comp);
                    List<Component> maxComps = max == null ? null : numberCompFromNumberProvider(max, formatter, comp -> comp);
                    Component scoreComp = Styler.property(Component.literal(score));
                    if (minComps != null && maxComps != null) {
                        scoreCycler.textComponent(Styler.condition(Component.translatable("bettertrims.tooltip.condition.entity_scores.between.%s".formatted(state.key()), target, scoreComp, minComps, maxComps)));
                    } else if (minComps != null) {
                        scoreCycler.textComponent(Styler.condition(Component.translatable("bettertrims.tooltip.condition.entity_scores.at_least.%s".formatted(state.key()), target, scoreComp, minComps)));
                    } else if (maxComps != null) {
                        scoreCycler.textComponent(Styler.condition(Component.translatable("bettertrims.tooltip.condition.entity_scores.at_most.%s".formatted(state.key()), target, scoreComp, maxComps)));
                    } else {
                        scoreCycler.textComponent(Styler.condition(Component.translatable("bettertrims.tooltip.condition.entity_scores.any_value.%s".formatted(state.key()), target, scoreComp)));
                    }
                }));
            });

            map.put(LootItemConditions.BLOCK_STATE_PROPERTY, (level, condition, parentBuilder, state) -> {
                LootItemBlockStatePropertyCondition blockStatePropertyCondition = (LootItemBlockStatePropertyCondition) condition;
                Registry<Block> registry = level.registryAccess().lookupOrThrow(Registries.BLOCK);
                Holder<Block> block = blockStatePropertyCondition.block();
                Component blockName = Styler.name(block.unwrap().map(registry::getValueOrThrow, Function.identity()).getName());
                Optional<StatePropertiesPredicate> properties = blockStatePropertyCondition.properties();
                Component target;
                CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
                if(properties.isPresent()) {
                    target = Styler.condition(Component.translatable("bettertrims.tooltip.condition.block_state_property.with_properties.%s".formatted(state.key()), blockName));
                    builder.textComponent(target);
                    BlockPredicateTooltip.addStatePropertiesPredicateToBuilder(level, properties.orElseThrow(), builder);
                } else {
                    target = Styler.condition(Component.translatable("bettertrims.tooltip.condition.block_state_property.%s".formatted(state.key()), blockName));
                    builder.textComponent(target);
                }
                parentBuilder.component(builder.build());
            });

            map.put(LootItemConditions.MATCH_TOOL, (level, condition, parentBuilder, state) -> {
                MatchTool matchTool = (MatchTool) condition;
                Optional<ItemPredicate> predicate = matchTool.predicate();
                if(predicate.isEmpty()) return;

                CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
                builder.translate("bettertrims.tooltip.condition.match_tool.%s".formatted(state.key()), Styler::condition);
                ItemPredicateTooltip.addToBuilder(level, predicate.orElseThrow(), builder);
                parentBuilder.component(builder.build());
            });
        }
    );

    static CompositeContainerComponent getTooltip(ClientLevel level, LootItemCondition condition) {
        CompositeContainerComponent.Builder builder = new CompositeContainerComponent.Builder()
            .translate("bettertrims.tooltip.condition.if", Styler::condition)
            .space();
        addConditionToTooltip(level, builder, condition, new State());
        return builder.build();
    }

    private static void addConditionToTooltip(ClientLevel level, CompositeContainerComponent.Builder builder, LootItemCondition condition, State state) {
        TOOLTIPS.getOrDefault(condition.getType(), LootTooltipFactory.EMPTY).accept(level, condition, builder, state);
    }

    private static @NotNull List<Component> numberCompFromNumberProvider(
        NumberProvider numberProvider,
        Function<Float, String> valueFormatter,
        UnaryOperator<Component> literalFormatter
    ) {
        return switch (numberProvider) {
            case ConstantValue constant -> List.of(Styler.number(Component.literal(valueFormatter.apply(constant.value()))));
            case ScoreboardValue scoreboardValue -> {
                Component score = Styler.property(Component.literal(scoreboardValue.score()));
                Component targetComp = Styler.name(switch (scoreboardValue.target()) {
                    case FixedScoreboardNameProvider fixed -> Component.literal(fixed.name());
                    case ContextScoreboardNameProvider context -> Component.translatable("bettertrims.tooltip.condition.context_scoreboard_name.%s".formatted(context.target().getSerializedName()));
                    default -> Component.literal("[?]");
                });
                yield List.of(Styler.condition(literalFormatter.apply(Component.translatable("bettertrims.tooltip.condition.number_provider.scoreboard", targetComp, score)).copy()));
            }
            case EnchantmentLevelProvider levelProvider -> {
                List<Component> components = new ArrayList<>();
                for (int i = 1; i <= 5; i++) {
                    float value = levelProvider.amount().calculate(i);
                    String formattedValue = valueFormatter.apply(value);
                    MutableComponent enchantmentLevelComp = Styler.value(Component.translatable("enchantment.level." + i));
                    components.add(enchantmentLevelComp.append(": ").append(Styler.number(Component.literal(formattedValue))));
                }
                yield components;
            }
            case StorageValue storageValue -> {
                Component pathComp = Styler.value(Component.literal(storageValue.path().asString()));
                Component storageComp = Styler.name(Component.literal(storageValue.storage().toString()));
                yield List.of(literalFormatter.apply(Component.translatable("bettertrims.tooltip.condition.number_provider.storage", pathComp, storageComp)));
            }
            default -> List.of(Component.translatable("bettertrims.tooltip.condition.number_provider.complex"));
        };
    }

    interface LootTooltipFactory {
        LootTooltipFactory EMPTY = (level, condition, builder, state) -> {};

        void accept(ClientLevel level, LootItemCondition condition, CompositeContainerComponent.Builder builder, State state);
    }

    class State {
        private boolean inverted = false;

        public State withInverted(boolean inverted) {
            this.inverted = inverted;
            return this;
        }

        public State invert() {
            inverted = !inverted;
            return this;
        }

        public boolean isInverted() {
            return inverted;
        }

        public String key() {
            return inverted ? "inverted" : "normal";
        }
    }
}
