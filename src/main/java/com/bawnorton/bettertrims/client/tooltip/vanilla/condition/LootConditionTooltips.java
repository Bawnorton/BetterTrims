package com.bawnorton.bettertrims.client.tooltip.vanilla.condition;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.mixin.accessor.CompositeLootItemConditionAccessor;
import com.bawnorton.bettertrims.client.mixin.accessor.IntRangeAccessor;
import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.component.ConditionalComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.BlockPredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.DamageSourcePredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.EntityPredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.ItemPredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.LocationPredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.PredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Formatter;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import com.bawnorton.bettertrims.property.condition.DimensionCheck;
import com.bawnorton.bettertrims.util.TriState;
import com.bawnorton.bettertrims.version.VRegistry;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
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

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface LootConditionTooltips {
	static CompositeContainerComponent getTooltip(ClientLevel level, Font font, LootItemCondition condition) {

		State state = new State();
		CompositeContainerComponent.Builder conditionBuilder = CompositeContainerComponent.builder();
		addConditionToTooltip(level, condition, conditionBuilder, state);
		CompositeContainerComponent conditionComponent = conditionBuilder.build();

		CompositeContainerComponent.Builder root = CompositeContainerComponent.builder();
		if (state.doUseIf()) {
			root.translate("bettertrims.tooltip.condition.if", Styler::condition)
					.space();
		}

		if (conditionComponent.isOneLine()) {
			return root.component(conditionComponent).build();
		} else {
			return root.component(CompositeContainerComponent.builder().component(new ConditionalComponent(
					new ClientTextTooltip(Styler.condition(Component.translatable(
							"bettertrims.tooltip.condition.hold_shift",
							Styler.trim(Component.literal("[LSHFT]"))
					)).getVisualOrderText()),
					conditionComponent,
					//? if >=1.21.10 {
					() -> !Minecraft.getInstance().hasShiftDown() && !BetterTrims.debug
					//?} else {
					/*() -> !Screen.hasShiftDown() && !BetterTrims.debug
					*///?}
			)).build()).build();
		}
	}

	@SuppressWarnings("unchecked")
	private static void addConditionToTooltip(ClientLevel level, LootItemCondition condition, CompositeContainerComponent.Builder builder, State state) {
		LootTooltipAdder<LootItemCondition> factory = (LootTooltipAdder<LootItemCondition>) TOOLTIPS.getOrDefault(condition.getType(), LootTooltipAdder.EMPTY);
		factory.addToBuilder(level, condition, builder, state);
	}

	private static @NotNull List<Component> numberCompFromNumberProvider(NumberProvider numberProvider, Function<Float, MutableComponent> valueFormatter, UnaryOperator<Component> literalFormatter) {
		return switch (numberProvider) {
			case ConstantValue constant -> List.of(Styler.number(valueFormatter.apply(constant.value())));
			case ScoreboardValue scoreboardValue -> {
				Component score = Styler.property(Component.literal(scoreboardValue.score()));
				Component targetComp = Styler.name(switch (scoreboardValue.target()) {
					case FixedScoreboardNameProvider fixed -> Component.literal(fixed.name());
					case ContextScoreboardNameProvider context ->
							Component.translatable("bettertrims.tooltip.condition.context_scoreboard_name.%s".formatted(context.target().getSerializedName()));
					default -> Component.literal("[?]");
				});
				yield List.of(Styler.condition(literalFormatter.apply(Component.translatable("bettertrims.tooltip.condition.number_provider.scoreboard", targetComp, score))
						.copy()));
			}
			case EnchantmentLevelProvider levelProvider -> {
				List<Component> components = new ArrayList<>();
				for (int i = 1; i <= 5; i++) {
					float value = levelProvider.amount().calculate(i);
					MutableComponent formattedValue = valueFormatter.apply(value);
					MutableComponent enchantmentLevelComp = Styler.value(Component.translatable("enchantment.level." + i));
					components.add(enchantmentLevelComp.append(": ").append(Styler.number(formattedValue)));
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

	interface LootTooltipAdder<T extends LootItemCondition> {
		LootTooltipAdder<?> EMPTY = (level, condition, builder, state) -> builder.translate(
				"bettertrims.tooltip.condition.unknown",
				Styler::condition,
				Objects.requireNonNullElse(BuiltInRegistries.LOOT_CONDITION_TYPE.getKey(condition.getType()), "[unregistered]").toString()
		);

		void addToBuilder(ClientLevel level, T condition, CompositeContainerComponent.Builder builder, State state);
	}

	Map<LootItemConditionType, LootTooltipAdder<?>> TOOLTIPS = Util.make(
			new HashMap<>(), map -> {
				map.put(
						LootItemConditions.INVERTED, (ClientLevel level, InvertedLootItemCondition condition, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							LootItemCondition term = condition.term();
							CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
							addConditionToTooltip(level, term, builder, state.withInverted(!state.doInvert()));
							parentBuilder.component(builder.build());
						}
				);

				LootTooltipAdder<CompositeLootItemCondition> compositeFactory = (ClientLevel level, CompositeLootItemCondition condition, CompositeContainerComponent.Builder parentBuilder, State state) -> {
					CompositeLootItemConditionAccessor composite = (CompositeLootItemConditionAccessor) condition;
					CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder().translate(
							"bettertrims.tooltip.condition.%s.%s".formatted(
									condition instanceof AllOfCondition ? "all_of" : "any_of",
									state.key()
							), Styler::condition
					).vertical();
					for (LootItemCondition term : composite.bettertrims$terms()) {
						CompositeContainerComponent.Builder termBuilder = CompositeContainerComponent.builder().literal("• ", Styler::condition);
						addConditionToTooltip(level, term, termBuilder, state.withInverted(false));
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
								numberCompFromNumberProvider(
										chanceCondition.chance(), value -> {
											if (state.doInvert()) {
												value = 1 - value;
											}
											return Formatter.percentage(value);
										}, comp -> comp.copy().append("%")
								).forEach(numberComp -> {
									Component tooltip = Styler.condition(Component.translatable("bettertrims.tooltip.condition.random_chance", numberComp));
									chanceCycler.textComponent(tooltip);
								});
							});
							state.withUseIf(false);
						}
				);

				map.put(
						LootItemConditions.RANDOM_CHANCE_WITH_ENCHANTED_BONUS,
						(ClientLevel level, LootItemRandomChanceWithEnchantedBonusCondition condition, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							Registry<Enchantment> registry = VRegistry.get(level, Registries.ENCHANTMENT);
							Enchantment enchantment = condition.enchantment().unwrap().map(registry::getValueOrThrow, Function.identity());
							Map<Integer, Float> chanceMap = new HashMap<>();
							for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
								chanceMap.put(i, condition.enchantedChance().calculate(i));
							}
							Component enchantmentName = Styler.name(enchantment.description().copy());
							parentBuilder.component(CompositeContainerComponent.builder()
									.translate("bettertrims.tooltip.condition.random_chance_with_enchanted_bonus.enchanted.%s".formatted(state.key()), Styler::condition, enchantmentName)
									.space()
									.cycle(levelCycler -> chanceMap.forEach((enchantLevel, chance) -> {
										MutableComponent levelComp = Styler.value(Component.translatable("enchantment.level." + enchantLevel)).append(": ");
										MutableComponent chanceComp = Styler.number(Formatter.percentage(chance));
										levelCycler.textComponent(levelComp.append(chanceComp));
									}))
									.literal(",")
									.space()
									.translate(
											"bettertrims.tooltip.condition.random_chance_with_enchanted_bonus.else",
											Styler::condition,
											Styler.number(Formatter.percentage(condition.unenchantedChance()))
									)
									.build());
						}
				);

				map.put(
						LootItemConditions.ENTITY_PROPERTIES,
						(ClientLevel level, LootItemEntityPropertyCondition condition, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							Optional<EntityPredicate> predicate = condition.predicate();
							if (predicate.isEmpty()) return;

							Component target = Styler.target(Component.translatable("bettertrims.tooltip.condition.entity_properties.%s.%s".formatted(
									state.key(),
									condition.entityTarget().getSerializedName()
							)));
							CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder().vertical();
							EntityPredicateTooltip.addToBuilder(level, predicate.orElseThrow(), state, builder);
							parentBuilder.textComponent(target).component(builder.build());
						}
				);

				map.put(
						LootItemConditions.KILLED_BY_PLAYER,
						(ClientLevel level, LootItemKilledByPlayerCondition condition, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							parentBuilder.textComponent(Styler.condition(Component.translatable("bettertrims.tooltip.condition.killed_by_player.%s".formatted(state.key()))));
						}
				);

				map.put(
						LootItemConditions.ENTITY_SCORES, (ClientLevel level, EntityHasScoreCondition condition, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							Component target = Styler.target(Component.translatable("bettertrims.tooltip.condition.entity_properties.%s.%s".formatted(
									state.key(),
									condition.entityTarget().getSerializedName()
							)));
							Map<String, IntRange> scores = condition.scores();
							CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
							builder.cycle(scoreCycler -> scores.forEach((score, range) -> {
								IntRangeAccessor accessor = (IntRangeAccessor) range;
								NumberProvider min = accessor.bettertrims$min();
								NumberProvider max = accessor.bettertrims$max();

								List<Component> minComps = min == null ? null : numberCompFromNumberProvider(min, Formatter::decimal, UnaryOperator.identity());
								List<Component> maxComps = max == null ? null : numberCompFromNumberProvider(max, Formatter::decimal, UnaryOperator.identity());
								Component scoreComp = Styler.property(Component.literal(score));
								if (minComps != null && maxComps != null) {
									scoreCycler.textComponent(Styler.condition(Component.translatable(
											"bettertrims.tooltip.condition.entity_scores.between.%s".formatted(state.key()),
											target,
											scoreComp,
											minComps,
											maxComps
									)));
								} else if (minComps != null) {
									scoreCycler.textComponent(Styler.condition(Component.translatable(
											"bettertrims.tooltip.condition.entity_scores.at_least.%s".formatted(state.key()),
											target,
											scoreComp,
											minComps
									)));
								} else if (maxComps != null) {
									scoreCycler.textComponent(Styler.condition(Component.translatable(
											"bettertrims.tooltip.condition.entity_scores.at_most.%s".formatted(state.key()),
											target,
											scoreComp,
											maxComps
									)));
								} else {
									scoreCycler.textComponent(Styler.condition(Component.translatable(
											"bettertrims.tooltip.condition.entity_scores.any_value.%s".formatted(state.key()),
											target,
											scoreComp
									)));
								}
							}));
						}
				);

				map.put(
						LootItemConditions.BLOCK_STATE_PROPERTY,
						(ClientLevel level, LootItemBlockStatePropertyCondition condition, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							Registry<Block> registry = VRegistry.get(level, Registries.BLOCK);
							Holder<Block> block = condition.block();
							Component blockName = Styler.name(block.unwrap().map(registry::getValueOrThrow, Function.identity()).getName());
							Optional<StatePropertiesPredicate> properties = condition.properties();
							Component target;
							CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
							if (properties.isPresent()) {
								target = Styler.condition(Component.translatable(
										"bettertrims.tooltip.condition.block_state_property.with_properties.%s".formatted(state.key()),
										blockName
								));
								builder.textComponent(target);
								BlockPredicateTooltip.addStatePropertiesPredicateToBuilder(level, properties.orElseThrow(), state, builder);
							} else {
								target = Styler.condition(Component.translatable("bettertrims.tooltip.condition.block_state_property.%s".formatted(state.key()), blockName));
								builder.textComponent(target);
							}
							parentBuilder.component(builder.build());
						}
				);

				map.put(
						LootItemConditions.MATCH_TOOL, (ClientLevel level, MatchTool matchTool, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							Optional<ItemPredicate> predicate = matchTool.predicate();
							if (predicate.isEmpty()) return;

							CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
							builder.translate("bettertrims.tooltip.condition.match_tool", Styler::target);
							ItemPredicateTooltip.addToBuilder(level, predicate.orElseThrow(), state, builder);
							parentBuilder.component(builder.build());
						}
				);

				map.put(
						LootItemConditions.TABLE_BONUS, (ClientLevel level, BonusLevelTableCondition condition, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							Holder<Enchantment> enchantment = condition.enchantment();
							List<Float> values = condition.values();
							Registry<Enchantment> registry = VRegistry.get(level, Registries.ENCHANTMENT);
							Component enchantmentName = Styler.name(enchantment.unwrap().map(registry::getValueOrThrow, Function.identity()).description().copy());
							CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
							builder.translate("bettertrims.tooltip.condition.table_bonus.%s".formatted(state.key()), Styler::condition, enchantmentName).space().cycle(cycler -> {
								for (int i = 0; i < values.size(); i++) {
									float value = values.get(i);
									MutableComponent levelComp = Styler.value(Component.translatable("enchantment.level." + (i + 1))).append(": ");
									MutableComponent valueComp = Styler.number(Formatter.decimal(value));
									cycler.textComponent(levelComp.append(valueComp));
								}
							});
							parentBuilder.component(builder.build());
						}
				);

				map.put(
						LootItemConditions.SURVIVES_EXPLOSION, (ClientLevel level, ExplosionCondition condition, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							parentBuilder.textComponent(Styler.condition(Component.translatable("bettertrims.tooltip.condition.survives_explosion.%s".formatted(state.key()))));
						}
				);

				map.put(
						LootItemConditions.DAMAGE_SOURCE_PROPERTIES,
						(ClientLevel level, DamageSourceCondition condition, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							Optional<DamageSourcePredicate> predicate = condition.predicate();
							if (predicate.isEmpty()) return;

							CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
							builder.translate("bettertrims.tooltip.condition.damage_source_properties.%s".formatted(state.key()), Styler::condition);
							DamageSourcePredicateTooltip.addToBuilder(level, predicate.orElseThrow(), state, builder);
							parentBuilder.component(builder.build());
						}
				);

				map.put(
						LootItemConditions.LOCATION_CHECK, (ClientLevel level, LocationCheck locationCheck, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							Optional<LocationPredicate> predicate = locationCheck.predicate();
							if (predicate.isEmpty()) return;

							BlockPos offset = locationCheck.offset();
							CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
							LocationPredicateTooltip.addToBuilder(level, predicate.orElseThrow(), state, builder);
							if (!offset.equals(BlockPos.ZERO)) {
								MutableComponent offsetComp = Styler.value(Component.literal("(%d, %d, %d)".formatted(offset.getX(), offset.getY(), offset.getZ())));
								builder.space().translate("bettertrims.tooltip.condition.location_check.offset", Styler::condition, offsetComp);
							}
							parentBuilder.component(builder.build());
						}
				);

				map.put(
						LootItemConditions.WEATHER_CHECK, (ClientLevel level, WeatherCheck weatherCheck, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							Optional<Boolean> raining = weatherCheck.isRaining();
							Optional<Boolean> thundering = weatherCheck.isThundering();
							if (raining.isEmpty() && thundering.isEmpty()) return;

							CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
							if (raining.isPresent() && thundering.isPresent()) {
								builder.translate(
										"bettertrims.tooltip.condition.weather_check.both.%s".formatted(state.key()),
										Styler::condition,
										Styler.value(Component.translatable("bettertrims.tooltip.condition.weather_check.raining.%s".formatted(raining.get() ? "true" : "false"))),
										Styler.value(Component.translatable("bettertrims.tooltip.condition.weather_check.thundering.%s".formatted(thundering.get() ? "true" : "false")))
								);
							} else if (raining.isPresent()) {
								builder.translate(
										"bettertrims.tooltip.condition.weather_check.raining.%s".formatted(state.key()),
										Styler::condition,
										Styler.value(Component.translatable("bettertrims.tooltip.condition.weather_check.raining.%s".formatted(raining.get() ? "true" : "false")))
								);
							} else {
								builder.translate(
										"bettertrims.tooltip.condition.weather_check.thundering.%s".formatted(state.key()),
										Styler::condition,
										Styler.value(Component.translatable("bettertrims.tooltip.condition.weather_check.thundering.%s".formatted(thundering.get() ? "true" : "false")))
								);
							}
							parentBuilder.component(builder.build());
						}
				);

				map.put(
						LootItemConditions.REFERENCE, (ClientLevel level, ConditionReference conditionReference, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							ResourceKey<LootItemCondition> name = conditionReference.name();
							ResourceLocation id = name.location();
							parentBuilder.textComponent(Styler.condition(Component.translatable(
									"bettertrims.tooltip.condition.reference.%s".formatted(state.key()),
									Styler.name(Component.literal(id.toString()))
							)));
						}
				);

				map.put(
						LootItemConditions.TIME_CHECK, (ClientLevel level, TimeCheck timeCheck, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							IntRange value = timeCheck.value();
							Optional<Long> period = timeCheck.period();
							IntRangeAccessor accessor = (IntRangeAccessor) value;
							NumberProvider min = accessor.bettertrims$min();
							NumberProvider max = accessor.bettertrims$max();

							Function<Float, MutableComponent> formatter = ticks -> {
								long totalSeconds = Math.round(ticks / 20) * 72L;
								long totalMinutes = TimeUnit.SECONDS.toMinutes(totalSeconds);
								long totalHours = TimeUnit.MINUTES.toHours(totalMinutes);
								long days = TimeUnit.HOURS.toDays(totalHours);
								long minutes = totalMinutes % 60;
								long hours = (totalHours + 6) % 24;
								String ampm = hours >= 12 ? "pm" : "am";
								hours = hours % 12;
								if (hours == 0) hours = 12;
								if (days > 0) {
									return Component.literal("%d:%02d%s + %d Days".formatted(hours, minutes, ampm, days));
								} else {
									return Component.literal("%d:%02d%s".formatted(hours, minutes, ampm));
								}
							};

							List<Component> minComps = min == null ? numberCompFromNumberProvider(new ConstantValue(0), formatter, UnaryOperator.identity()) : numberCompFromNumberProvider(
									min,
									formatter,
									UnaryOperator.identity()
							);
							List<Component> maxComps = max == null ? numberCompFromNumberProvider(
									new ConstantValue(24000),
									formatter,
									UnaryOperator.identity()
							) : numberCompFromNumberProvider(max, formatter, UnaryOperator.identity());
							CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
							builder.translate("bettertrims.tooltip.condition.time_check.%s".formatted(state.key()), Styler::condition)
									.space()
									.translate("bettertrims.tooltip.condition.time_check.between", Styler::condition)
									.space()
									.cycle(cycleBuilder -> minComps.forEach(cycleBuilder::textComponent))
									.literal(" & ", Styler::condition)
									.cycle(cycleBuilder -> maxComps.forEach(cycleBuilder::textComponent));
							if (period.isPresent()) {
								float periodTicks = period.orElseThrow();
								long totalSeconds = Math.round(periodTicks / 20) * 72L;
								long totalMinutes = TimeUnit.SECONDS.toMinutes(totalSeconds);
								long totalHours = TimeUnit.MINUTES.toHours(totalMinutes);
								long days = TimeUnit.HOURS.toDays(totalHours);
								long minutes = totalMinutes % 60;
								long hours = totalHours % 24;
								StringBuilder formattedPeriod = new StringBuilder();
								if (days > 0) {
									if (days == 1) {
										formattedPeriod.append("Day");
									} else {
										formattedPeriod.append("%d Days".formatted(days));
									}
								}
								if (hours > 0) {
									if (!formattedPeriod.isEmpty()) {
										formattedPeriod.append(", %d Hours".formatted(hours));
									} else if (hours == 1) {
										formattedPeriod.append("Hour");
									} else {
										formattedPeriod.append("%d Hours".formatted(hours));
									}
								}
								if (minutes > 0) {
									if (!formattedPeriod.isEmpty()) {
										formattedPeriod.append(", %d Minutes".formatted(minutes));
									} else if (minutes == 1) {
										formattedPeriod.append("Minute");
									} else {
										formattedPeriod.append("%d Minutes".formatted(minutes));
									}
								}
								if (formattedPeriod.isEmpty()) {
									formattedPeriod.append("%d Seconds".formatted(totalSeconds));
								}
								builder.space()
										.translate("bettertrims.tooltip.condition.time_check.period", Styler::condition, Styler.value(Component.literal(formattedPeriod.toString())));
							}
							parentBuilder.component(builder.build());
						}
				);

				map.put(
						LootItemConditions.VALUE_CHECK, (ClientLevel level, ValueCheckCondition condition, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							NumberProvider provider = condition.provider();
							IntRange range = condition.range();
							IntRangeAccessor accessor = (IntRangeAccessor) range;
							NumberProvider min = accessor.bettertrims$min();
							NumberProvider max = accessor.bettertrims$max();
							List<Component> providerComps = numberCompFromNumberProvider(provider, Formatter::decimal, UnaryOperator.identity());
							List<Component> minComps = min == null ? null : numberCompFromNumberProvider(min, Formatter::decimal, UnaryOperator.identity());
							List<Component> maxComps = max == null ? null : numberCompFromNumberProvider(max, Formatter::decimal, UnaryOperator.identity());
							CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
							builder.translate("bettertrims.tooltip.condition.value_check.%s".formatted(state.key()), Styler::condition, providerComps).space();
							if (minComps != null && maxComps != null) {
								builder.translate("bettertrims.tooltip.condition.value_check.between", Styler::condition, minComps, maxComps);
							} else if (minComps != null) {
								builder.translate("bettertrims.tooltip.condition.value_check.at_least", Styler::condition, minComps);
							} else if (maxComps != null) {
								builder.translate("bettertrims.tooltip.condition.value_check.at_most", Styler::condition, maxComps);
							} else {
								builder.translate("bettertrims.tooltip.condition.value_check.any_value", Styler::condition);
							}
							parentBuilder.component(builder.build());
						}
				);

				map.put(
						LootItemConditions.ENCHANTMENT_ACTIVE_CHECK,
						(ClientLevel level, EnchantmentActiveCheck condition, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							parentBuilder.translate("bettertrims.tooltip.condition.enchantment_active_check.%s.%S".formatted(state.key(), condition.active()), Styler::condition);
						}
				);

				map.put(
						DimensionCheck.TYPE, (ClientLevel level, DimensionCheck condition, CompositeContainerComponent.Builder parentBuilder, State state) -> {
							HolderSet<DimensionType> dimensions = condition.dimensions();
							PredicateTooltip.addRegisteredElementsToBuilder(
									level,
									"bettertrims.tooltip.condition.dimension_check.%s".formatted(state.key()),
									Registries.DIMENSION_TYPE,
									dimensions,
									state.withPrefixSpace(false),
									parentBuilder
							);
						}
				);
			}
	);

	class State {
		private final Set<StateEntry> entries = new HashSet<>();
		private final StateEntry inverted = entry("inverted", false, true);
		private final StateEntry useWith = entry("with", false, true);
		private final StateEntry prefixSpace = entry("space", true, false);
		private final StateEntry useIf = entry("if", true, false);

		public static String normalKey() {
			return "normal";
		}

		private StateEntry entry(String key, boolean defaultValue, boolean includedInKey) {
			StateEntry stateEntry = new StateEntry(key, defaultValue, includedInKey);
			entries.add(stateEntry);
			return stateEntry;
		}

		public State withInverted(boolean inverted) {
			this.inverted.switchTo(inverted);
			return this;
		}

		public State withPrefixSpace(boolean prefixSpace) {
			this.prefixSpace.switchTo(prefixSpace);
			return this;
		}

		public State withUseWith(boolean useWith) {
			this.useWith.switchTo(useWith);
			return this;
		}

		public State withUseIf(boolean useIf) {
			this.useIf.switchTo(useIf);
			return this;
		}

		public boolean doInvert() {
			return inverted.value();
		}

		public boolean doPrefixSpace() {
			return prefixSpace.value();
		}

		public boolean doUseWith() {
			return useWith.value();
		}

		public boolean doUseIf() {
			return useIf.value();
		}

		public String key() {
			StringBuilder keyBuilder = new StringBuilder();
			for (StateEntry entry : entries) {
				if (entry.includedInKey() && entry.value()) {
					if (!keyBuilder.isEmpty()) {
						keyBuilder.append(".");
					}
					keyBuilder.append(entry.key());
				}
			}
			if (keyBuilder.isEmpty()) {
				return normalKey();
			}
			return keyBuilder.toString();
		}

		private static final class StateEntry {
			private final String key;
			private final boolean defaultValue;
			private final boolean includedInKey;
			private TriState state;

			private StateEntry(String key, boolean defaultValue, boolean includedInKey) {
				this.key = key;
				this.state = TriState.DEFAULT;
				this.defaultValue = defaultValue;
				this.includedInKey = includedInKey;
			}

			public void switchTo(boolean value) {
				state = value ? TriState.TRUE : TriState.FALSE;
			}

			public boolean value() {
				return state == TriState.DEFAULT ? defaultValue : state == TriState.TRUE;
			}

			public boolean includedInKey() {
				return includedInKey;
			}

			public String key() {
				return key;
			}
		}
	}


}
