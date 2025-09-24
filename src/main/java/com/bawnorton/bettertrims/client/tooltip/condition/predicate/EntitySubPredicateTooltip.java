package com.bawnorton.bettertrims.client.tooltip.condition.predicate;

import com.bawnorton.bettertrims.client.tooltip.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import net.minecraft.advancements.critereon.*;
import net.minecraft.client.multiplayer.ClientLevel;
import java.util.Optional;

public interface EntitySubPredicateTooltip {
    static void addToBuilder(ClientLevel level, EntitySubPredicate predicate, CompositeContainerComponent.Builder builder) {
        switch (predicate) {
            case FishingHookPredicate fishingHookPredicate -> addFishingHookPredicateToBuilder(level, fishingHookPredicate, builder);
            case LightningBoltPredicate lightningBoltPredicate -> addLightningBoltPredicateToBuilder(level, lightningBoltPredicate, builder);
            case PlayerPredicate playerPredicate -> addPlayerPredicateToBuilder(level, playerPredicate, builder);
            case SheepPredicate sheepPredicate -> addSheepPredicateToBuilder(level, sheepPredicate, builder);
            case SlimePredicate slimePredicate -> addSlimePredicateToBuilder(level, slimePredicate, builder);
            case RaiderPredicate raiderPredicate -> addRaiderPredicateToBuilder(level, raiderPredicate, builder);
            default -> builder.translate(key("unknown"), Styler::negative);
        }
    }

    static String key(String key) {
        return PredicateTooltip.key("sub_entity.%s".formatted(key));
    }

    static void addFishingHookPredicateToBuilder(ClientLevel level, FishingHookPredicate predicate, CompositeContainerComponent.Builder builder) {
        Optional<Boolean> inOpenWater = predicate.inOpenWater();
        if(inOpenWater.isPresent()) {
            builder.component(CompositeContainerComponent.builder()
                .space()
                .translate(key("fishing_hook.in_open_water.%s".formatted(inOpenWater.orElse(false) ? "true" : "false")), Styler::value)
                .build());
        }
    }

    static void addLightningBoltPredicateToBuilder(ClientLevel level, LightningBoltPredicate predicate, CompositeContainerComponent.Builder builder) {
        CompositeContainerComponent.Builder boltBuilder = CompositeContainerComponent.builder();

        boolean useAnd = false;
        Optional<EntityPredicate> struck = predicate.entityStruck();
        if(struck.isPresent()) {
            CompositeContainerComponent.Builder entityBuilder = CompositeContainerComponent.builder();
            EntityPredicateTooltip.addToBuilder(level, struck.orElseThrow(), entityBuilder);
            boltBuilder.component(CompositeContainerComponent.builder()
                .space()
                .translate(key("lightning_bolt.entity_struck"), Styler::condition)
                .space()
                .component(entityBuilder.build())
                .build());
            useAnd = true;
        }

        MinMaxBounds.Ints blocksSetOnFire = predicate.blocksSetOnFire();
        if(!blocksSetOnFire.isAny()) {
            PredicateTooltip.addMinMaxToBuilder(key("lightning_bolt.blocks_set_on_fire"), useAnd, blocksSetOnFire, boltBuilder);
        }
        CompositeContainerComponent boltComponent = boltBuilder.build();
        if(!boltComponent.isEmpty()) {
            builder.component(CompositeContainerComponent.builder()
                .space()
                .translate(key("lightning_bolt.matches"), Styler::condition)
                .space()
                .component(boltComponent)
                .build());
        }
    }

    static void addPlayerPredicateToBuilder(ClientLevel level, PlayerPredicate predicate, CompositeContainerComponent.Builder builder) {
        PlayerPredicateTooltip.addToBuilder(level, predicate, builder);
    }

    static void addSheepPredicateToBuilder(ClientLevel level, SheepPredicate predicate, CompositeContainerComponent.Builder builder) {
        Optional<Boolean> sheared = predicate.sheared();
        if(sheared.isPresent()) {
            builder.component(CompositeContainerComponent.builder()
                .space()
                .translate(key("sheep.sheared.%s".formatted(sheared.orElse(false) ? "true" : "false")), Styler::value)
                .build());
        }
    }

    static void addSlimePredicateToBuilder(ClientLevel level, SlimePredicate predicate, CompositeContainerComponent.Builder builder) {
        MinMaxBounds.Ints size = predicate.size();
        if(!size.isAny()) {
            CompositeContainerComponent.Builder sizeBuilder = CompositeContainerComponent.builder().space();
            PredicateTooltip.addMinMaxToBuilder(key("slime.size"), false, size, sizeBuilder);
            builder.component(sizeBuilder.build());
        }
    }

    static void addRaiderPredicateToBuilder(ClientLevel level, RaiderPredicate predicate, CompositeContainerComponent.Builder builder) {
        boolean hasRaid = predicate.hasRaid();
        boolean captain = predicate.isCaptain();
        if(hasRaid || captain) {
            String key = "raider.";
            if(hasRaid) key += "in_raid";
            if(captain) {
                if(hasRaid) key += "_and_";
                key += "is_captain";
            }
            builder.component(CompositeContainerComponent.builder()
                .space()
                .translate(key(key), Styler::value)
                .build());
        }
    }
}
