package com.bawnorton.bettertrims.client.tooltip.condition.predicate;

import com.bawnorton.bettertrims.client.tooltip.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.GameTypePredicate;
import net.minecraft.advancements.critereon.InputPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.PlayerPredicate;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.GameType;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PlayerPredicateTooltip {
    static void addToBuilder(ClientLevel level, PlayerPredicate predicate, CompositeContainerComponent.Builder builder) {
        MinMaxBounds.Ints playerLevel = predicate.level();
        addLevelToBuilder(level, playerLevel, builder);

        GameTypePredicate gameType = predicate.gameType();
        addGameTypeToBuilder(level, gameType, builder);

        List<PlayerPredicate.StatMatcher<?>> stats = predicate.stats();
        addStatsToBuilder(level, stats, builder);

        Object2BooleanMap<ResourceKey<Recipe<?>>> recipes = predicate.recipes();
        addRecipesToBuilder(level, recipes, builder);

        Map<ResourceLocation, PlayerPredicate.AdvancementPredicate> advancements = predicate.advancements();
        addAdvancementsToBuilder(level, advancements, builder);

        Optional<EntityPredicate> lookingAt = predicate.lookingAt();
        if(lookingAt.isPresent()) {
            addLookingAtToBuilder(level, lookingAt.orElseThrow(), builder);
        }

        Optional<InputPredicate> input = predicate.input();
        if(input.isPresent()) {
            addInputToBuilder(level, input.orElseThrow(), builder);
        }
    }

    static String key(String key) {
        return PredicateTooltip.key("player.%s".formatted(key));
    }

    static void addLevelToBuilder(ClientLevel level, MinMaxBounds.Ints playerLevel, CompositeContainerComponent.Builder builder) {
        if(!playerLevel.isAny()) {
            PredicateTooltip.addMinMaxToBuilder(key("level"), false, playerLevel, builder);
        }
    }

    static void addGameTypeToBuilder(ClientLevel level, GameTypePredicate gameType, CompositeContainerComponent.Builder builder) {
        List<GameType> types = gameType.types();
        if(!types.isEmpty()) {
            PredicateTooltip.addEnumListToBuilder(key("game_type"), types, GameType::getShortDisplayName, builder);
        }
    }

    static void addStatsToBuilder(ClientLevel level, List<PlayerPredicate.StatMatcher<?>> stats, CompositeContainerComponent.Builder builder) {
        if(stats.isEmpty()) return;

        CompositeContainerComponent.Builder statsBuilder = CompositeContainerComponent.builder()
            .space()
            .translate(key("stats.matches"), Styler::condition)
            .space()
            .cycle(cycleBuilder -> {
                for(PlayerPredicate.StatMatcher<?> stat : stats) {
                    CompositeContainerComponent.Builder cycledBuilder = CompositeContainerComponent.builder();
                    cycledBuilder.textComponent(Component.translatable(key("stat"), Styler.name(stat.type().getDisplayName().copy())))
                        .space()
                        .literal("[", Styler::condition);
                    PredicateTooltip.addMinMaxToBuilder(key("stat.range"), false, stat.range(), cycledBuilder);
                    cycledBuilder.literal("]", Styler::condition);
                    cycleBuilder.component(cycledBuilder.build());
                }
            });
        builder.component(statsBuilder.build());
    }

    static void addRecipesToBuilder(ClientLevel level, Object2BooleanMap<ResourceKey<Recipe<?>>> recipes, CompositeContainerComponent.Builder builder) {
        if(recipes.isEmpty()) return;

        CompositeContainerComponent.Builder recipesBuilder = CompositeContainerComponent.builder()
            .space()
            .translate(key("recipes.matches"), Styler::condition)
            .space()
            .cycle(cycleBuilder -> recipes.object2BooleanEntrySet().forEach(entry -> {
                boolean knows = entry.getBooleanValue();
                ResourceLocation id = entry.getKey().location();
                Component name = Styler.name(Component.literal(id.toString()));
                cycleBuilder.component(CompositeContainerComponent.builder()
                    .translate(key("recipe.%s".formatted(knows ? "knows" : "does_not_know")), Styler::condition, name)
                    .build());
            }));
        builder.component(recipesBuilder.build());
    }

    static void addAdvancementsToBuilder(ClientLevel level, Map<ResourceLocation, PlayerPredicate.AdvancementPredicate> advancements, CompositeContainerComponent.Builder builder) {
        if(advancements.isEmpty()) return;

        Registry<Advancement> registry = level.registryAccess().lookupOrThrow(Registries.ADVANCEMENT);
        CompositeContainerComponent.Builder advancementsBuilder = CompositeContainerComponent.builder()
            .space()
            .translate(key("advancements.matches"), Styler::condition)
            .space()
            .cycle(cycleBuilder -> advancements.forEach((id, advancementPredicate) -> {
                Advancement advancement = registry.getValue(id);
                if(advancement == null) return;

                Component name = Styler.name(advancement.name().orElse(Component.literal(id.toString())).copy());
                CompositeContainerComponent.Builder advancementBuilder = CompositeContainerComponent.builder()
                    .textComponent(name);
                switch (advancementPredicate) {
                    case PlayerPredicate.AdvancementDonePredicate donePredicate -> {
                        boolean state = donePredicate.state();
                        advancementBuilder.translate(key("advancement.%s".formatted(state ? "completed" : "not_completed")), Styler::value);
                    }
                    case PlayerPredicate.AdvancementCriterionsPredicate criterionsPredicate -> {
                        Object2BooleanMap<String> criterions = criterionsPredicate.criterions();
                        if (!criterions.isEmpty()) {
                            advancementBuilder.space().literal("[");
                            boolean useAnd = false;
                            for (Map.Entry<String, Boolean> entry : criterions.object2BooleanEntrySet()) {
                                if (useAnd) {
                                    advancementBuilder.literal(" & ");
                                }
                                useAnd = true;

                                String criterion = entry.getKey();
                                boolean achieved = entry.getValue();
                                advancementBuilder.translate(
                                    key("advancement.criterion.%s".formatted(achieved ? "achieved" : "not_achieved")),
                                    Styler::value,
                                    Styler.name(Component.literal(criterion))
                                );
                            }
                            advancementBuilder.literal("]");
                        }
                    }
                    default -> {}
                }
                cycleBuilder.component(advancementBuilder.build());
            }));
        builder.component(advancementsBuilder.build());
    }

    static void addLookingAtToBuilder(ClientLevel level, EntityPredicate entityPredicate, CompositeContainerComponent.Builder builder) {
        EntityPredicateTooltip.addEntityPredicateToBuilder(level, "looking_at", entityPredicate, builder);
    }

    static void addInputToBuilder(ClientLevel level, InputPredicate inputPredicate, CompositeContainerComponent.Builder builder) {
        Map<String, Optional<Boolean>> inputs = Map.of(
            "forward", inputPredicate.forward(),
            "backward", inputPredicate.backward(),
            "left", inputPredicate.left(),
            "right", inputPredicate.right(),
            "jump", inputPredicate.jump(),
            "sneak", inputPredicate.sneak(),
            "sprint", inputPredicate.sprint()
        );
        CompositeContainerComponent.Builder inputBuilder = CompositeContainerComponent.builder()
            .space()
            .translate(key("input.matches"), Styler::condition)
            .space()
            .cycle(cycleBuilder -> inputs.forEach((inputName, input) -> {
                if(input.isPresent()) {
                    boolean state = input.orElse(false);
                    cycleBuilder.component(CompositeContainerComponent.builder()
                        .translate(key("input.%s.%s".formatted(inputName, state ? "true" : "false")), Styler::value)
                        .build());
                }
            }));
        builder.component(inputBuilder.build());
    }
}
