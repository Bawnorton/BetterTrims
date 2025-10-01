package com.bawnorton.bettertrims.client.tooltip.condition.predicate;

import com.bawnorton.bettertrims.client.tooltip.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.DataComponentMatchersTooltip;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.advancements.critereon.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.SlotRange;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.bawnorton.bettertrims.client.tooltip.condition.predicate.PredicateTooltip.addMinMaxToBuilder;

public interface EntityPredicateTooltip {
    static void addToBuilder(ClientLevel level, EntityPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        Optional<EntityTypePredicate> entityTypePredicate = predicate.entityType();
        if (entityTypePredicate.isPresent()) {
            addEntityTypePredicateToBuilder(level, entityTypePredicate.orElseThrow(), state, builder);
        }

        Optional<DistancePredicate> distancePredicate = predicate.distanceToPlayer();
        if (distancePredicate.isPresent()) {
            addDistancePredicateToBuilder(level, distancePredicate.orElseThrow(), state, builder);
        }

        Optional<MovementPredicate> movement = predicate.movement();
        if (movement.isPresent()) {
            addMovementPredicateToBuilder(level, movement.orElseThrow(), state, builder);
        }

        EntityPredicate.LocationWrapper location = predicate.location();
        Optional<LocationPredicate> located = location.located();
        if (located.isPresent()) {
            addLocationPredicateToBuilder(level, located.orElseThrow(), state, builder);
        }

        Optional<LocationPredicate> steppingOn = location.steppingOn();
        if (steppingOn.isPresent()) {
            addLocationPredicateToBuilder(level, steppingOn.orElseThrow(), state, builder);
        }

        Optional<LocationPredicate> affectsMovement = location.affectsMovement();
        if (affectsMovement.isPresent()) {
            addLocationPredicateToBuilder(level, affectsMovement.orElseThrow(), state, builder);
        }

        Optional<MobEffectsPredicate> effects = predicate.effects();
        if (effects.isPresent()) {
            addMobEffectsPredicateToBuilder(level, effects.orElseThrow(), state, builder);
        }

        Optional<NbtPredicate> nbt = predicate.nbt();
        if (nbt.isPresent()) {
            addNbtPredicateToBuilder(level, nbt.orElseThrow(), state, builder);
        }

        Optional<EntityFlagsPredicate> flags = predicate.flags();
        if (flags.isPresent()) {
            addEntityFlagsPredicateToBuilder(level, flags.orElseThrow(), state, builder);
        }

        Optional<EntityEquipmentPredicate> equipment = predicate.equipment();
        if (equipment.isPresent()) {
            addEntityEquipmentPredicateToBuilder(level, equipment.orElseThrow(), state, builder);
        }

        Optional<EntitySubPredicate> subPredicate = predicate.subPredicate();
        if (subPredicate.isPresent()) {
            addEntitySubPredicateToBuilder(level, subPredicate.orElseThrow(), state, builder);
        }

        Optional<Integer> periodicTick = predicate.periodicTick();
        if (periodicTick.isPresent()) {
            addPeriodicTickToBuilder(level, periodicTick.orElseThrow(), state, builder);
        }

        Optional<EntityPredicate> vehicle = predicate.vehicle();
        if (vehicle.isPresent()) {
            addEntityPredicateToBuilder(level, "vehicle", vehicle.orElseThrow(), state, builder);
        }

        Optional<EntityPredicate> passenger = predicate.passenger();
        if (passenger.isPresent()) {
            addEntityPredicateToBuilder(level, "passenger", passenger.orElseThrow(), state, builder);
        }

        Optional<EntityPredicate> targetedEntity = predicate.targetedEntity();
        if (targetedEntity.isPresent()) {
            addEntityPredicateToBuilder(level, "targeted_entity", targetedEntity.orElseThrow(), state, builder);
        }

        Optional<String> team = predicate.team();
        if (team.isPresent()) {
            addTeamToBuilder(level, team.orElseThrow(), state, builder);
        }

        Optional<SlotsPredicate> slots = predicate.slots();
        if (slots.isPresent()) {
            addSlotsPredicateToBuilder(level, slots.orElseThrow(), state, builder);
        }

        DataComponentMatchers components = predicate.components();
        if (!components.isEmpty()) {
            addDataComponentMatchersToBuilder(level, components, state, builder);
        }
    }

    static String key(String key) {
        return PredicateTooltip.key("entity.%s".formatted(key));
    }

    static void addEntityTypePredicateToBuilder(ClientLevel level, EntityTypePredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        PredicateTooltip.addRegisteredElementsToBuilder(level, key("matches"), Registries.ENTITY_TYPE, predicate.types(), EntityType::getDescription, state, builder);
    }

    static void addDistancePredicateToBuilder(ClientLevel level, DistancePredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        MinMaxBounds.Doubles x = predicate.x();
        MinMaxBounds.Doubles y = predicate.y();
        MinMaxBounds.Doubles z = predicate.z();
        MinMaxBounds.Doubles horizontal = predicate.horizontal();
        MinMaxBounds.Doubles absolute = predicate.absolute();

        CompositeContainerComponent.Builder distanceBuilder = CompositeContainerComponent.builder()
            .space()
            .translate(key("distance"))
            .space();
        boolean useAnd = false;
        if (!x.isAny()) {
            addMinMaxToBuilder(key("distance.x"), false, x, state, distanceBuilder);
            useAnd = true;
        }
        if (!y.isAny()) {
            addMinMaxToBuilder(key("distance.y"), useAnd, y, state, distanceBuilder);
            useAnd = true;
        }
        if (!z.isAny()) {
            addMinMaxToBuilder(key("distance.z"), useAnd, z, state, distanceBuilder);
            useAnd = true;
        }
        if (!horizontal.isAny()) {
            addMinMaxToBuilder(key("distance.horizontal"), useAnd, horizontal, state, distanceBuilder);
            useAnd = true;
        }
        if (!absolute.isAny()) {
            addMinMaxToBuilder(key("distance.absolute"), useAnd, absolute, state, distanceBuilder);
        }
        builder.component(distanceBuilder.build());
    }

    static void addMovementPredicateToBuilder(ClientLevel level, MovementPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        MinMaxBounds.Doubles x = predicate.x();
        MinMaxBounds.Doubles y = predicate.y();
        MinMaxBounds.Doubles z = predicate.z();
        MinMaxBounds.Doubles horizontalSpeed = predicate.horizontalSpeed();
        MinMaxBounds.Doubles verticalSpeed = predicate.verticalSpeed();
        MinMaxBounds.Doubles speed = predicate.speed();
        MinMaxBounds.Doubles fallDistance = predicate.fallDistance();

        CompositeContainerComponent.Builder movementBuilder = CompositeContainerComponent.builder()
            .space()
            .translate(key("movement"))
            .space();
        boolean useAnd = false;
        if (!x.isAny()) {
            addMinMaxToBuilder(key("movement.x"), false, x, state, movementBuilder);
            useAnd = true;
        }
        if (!y.isAny()) {
            addMinMaxToBuilder(key("movement.y"), useAnd, y, state, movementBuilder);
            useAnd = true;
        }
        if (!z.isAny()) {
            addMinMaxToBuilder(key("movement.z"), useAnd, z, state, movementBuilder);
            useAnd = true;
        }
        if (!horizontalSpeed.isAny()) {
            addMinMaxToBuilder(key("movement.horizontal_speed"), useAnd, horizontalSpeed, state, movementBuilder);
            useAnd = true;
        }
        if (!verticalSpeed.isAny()) {
            addMinMaxToBuilder(key("movement.vertical_speed"), useAnd, verticalSpeed, state, movementBuilder);
            useAnd = true;
        }
        if (!speed.isAny()) {
            addMinMaxToBuilder(key("movement.speed"), useAnd, speed, state, movementBuilder);
            useAnd = true;
        }
        if (!fallDistance.isAny()) {
            addMinMaxToBuilder(key("movement.fall_distance"), useAnd, fallDistance, state, movementBuilder);
        }
        builder.component(movementBuilder.build());
    }

    static void addLocationPredicateToBuilder(ClientLevel level, LocationPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        LocationPredicateTooltip.addToBuilder(level, predicate, state, builder);
    }

    static void addMobEffectsPredicateToBuilder(ClientLevel level, MobEffectsPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        Map<Holder<MobEffect>, MobEffectsPredicate.MobEffectInstancePredicate> effectMap = predicate.effectMap();
        if (effectMap.isEmpty()) {
            builder.space().translate(key("effects.any"), Styler::value);
        } else {
            Registry<MobEffect> effectRegistry = level.registryAccess().lookupOrThrow(Registries.MOB_EFFECT);
            CompositeContainerComponent.Builder effectsBuilder = CompositeContainerComponent.builder()
                .space()
                .translate(key("effects.matches"), Styler::condition)
                .space()
                .cycle(effectCycler -> effectMap.forEach((effectHolder, effectInstancePredicate) -> {
                    MobEffect effect = effectHolder.unwrap().map(effectRegistry::getValueOrThrow, Function.identity());

                    CompositeContainerComponent.Builder effectBuilder = CompositeContainerComponent.builder()
                        .translate(key("effects.effect"), Styler.name(effect.getDisplayName().copy()));
                    CompositeContainerComponent.Builder conditionBuilder = CompositeContainerComponent.builder();
                    boolean useAnd = false;
                    Optional<Boolean> ambient = effectInstancePredicate.ambient();
                    if (ambient.isPresent()) {
                        conditionBuilder.translate(key("effects.ambient.%s".formatted(ambient.orElseThrow() ? "true" : "false")), Styler::value);
                        useAnd = true;
                    }
                    Optional<Boolean> visible = effectInstancePredicate.visible();
                    if (visible.isPresent()) {
                        if (useAnd) conditionBuilder.literal(", ", Styler::condition);
                        conditionBuilder.translate(key("effects.visible.%s".formatted(visible.orElseThrow() ? "true" : "false")), Styler::value);
                        useAnd = true;
                    }
                    MinMaxBounds.Ints amplifier = effectInstancePredicate.amplifier();
                    if (!amplifier.isAny()) {
                        if (useAnd) conditionBuilder.literal(", ", Styler::condition);
                        addMinMaxToBuilder(
                            key("effects.amplifier"),
                            false,
                            amplifier,
                            value -> Component.translatable("enchantment.level.%s".formatted("%.0f".formatted(value + 1))),
                            state, conditionBuilder
                        );
                        useAnd = true;
                    }
                    MinMaxBounds.Ints duration = effectInstancePredicate.duration();
                    if (!duration.isAny()) {
                        if (useAnd) conditionBuilder.literal(", ", Styler::condition);
                        addMinMaxToBuilder(
                            key("effects.duration"),
                            false,
                            duration,
                            i -> Component.literal("%.0fs".formatted(i * 20)),
                            state, conditionBuilder
                        );
                    }
                    CompositeContainerComponent effectConditions = conditionBuilder.build();
                    if (!effectConditions.isEmpty()) {
                        effectBuilder.literal(" (", Styler::condition)
                            .component(effectConditions)
                            .literal(")", Styler::condition);
                    }
                    effectCycler.component(effectBuilder.build());
                }));
            builder.component(effectsBuilder.build());
        }
    }

    static void addNbtPredicateToBuilder(ClientLevel level, NbtPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        builder.component(CompositeContainerComponent.builder()
            .space()
            .translate(key("nbt"), Styler::condition, Styler.value(Component.literal(predicate.tag().toString())))
            .build());
    }

    static void addEntityFlagsPredicateToBuilder(ClientLevel level, EntityFlagsPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        Map<String, Optional<Boolean>> flagMap = Map.of(
            "on_ground", predicate.isOnGround(),
            "on_fire", predicate.isOnFire(),
            "crouching", predicate.isCrouching(),
            "sprinting", predicate.isSprinting(),
            "swimming", predicate.isSwimming(),
            "flying", predicate.isFlying(),
            "baby", predicate.isBaby()
        );
        CompositeContainerComponent.Builder flagsBuilder = CompositeContainerComponent.builder().space();
        boolean useAnd = false;
        for (Map.Entry<String, Optional<Boolean>> entry : flagMap.entrySet()) {
            if (entry.getValue().isPresent()) {
                if (useAnd) {
                    flagsBuilder.literal(" & ", Styler::condition);
                }
                flagsBuilder.translate(key("flags.%s.%s".formatted(entry.getKey(), entry.getValue().orElseThrow() ? "true" : "false")), Styler::value);
                useAnd = true;
            }
        }
    }

    static void addEntityEquipmentPredicateToBuilder(ClientLevel level, EntityEquipmentPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        Map<EquipmentSlot, Optional<ItemPredicate>> equipmentMap = Map.of(
            EquipmentSlot.HEAD, predicate.head(),
            EquipmentSlot.CHEST, predicate.chest(),
            EquipmentSlot.LEGS, predicate.legs(),
            EquipmentSlot.FEET, predicate.feet(),
            EquipmentSlot.MAINHAND, predicate.mainhand(),
            EquipmentSlot.OFFHAND, predicate.offhand()
        );
        CompositeContainerComponent.Builder equipmentBuilder = CompositeContainerComponent.builder()
            .space()
            .translate(key("equipment.matches"), Styler::condition)
            .space()
            .cycle(equipmentCycler -> {
                equipmentMap.forEach((slot, itemPredicate) -> {
                    if (itemPredicate.isPresent()) {
                        Component slotName = Styler.name(Component.translatable("slot.%s".formatted(slot.getName())));
                        CompositeContainerComponent.Builder itemBuilder = CompositeContainerComponent.builder()
                            .translate(key("equipment.slot"), Styler::condition, slotName)
                            .space();
                        ItemPredicateTooltip.addToBuilder(level, itemPredicate.orElseThrow(), new LootConditionTooltips.State(), itemBuilder);
                        equipmentCycler.component(itemBuilder.build());
                    }
                });
            });
        builder.component(equipmentBuilder.build());
    }

    static void addEntitySubPredicateToBuilder(ClientLevel level, EntitySubPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        EntitySubPredicateTooltip.addToBuilder(level, predicate, state, builder);
    }

    static void addPeriodicTickToBuilder(ClientLevel level, int tick, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        builder.component(CompositeContainerComponent.builder()
            .space()
            .translate(key("periodic_tick"), Styler.number(tick))
            .build());
    }

    static void addEntityPredicateToBuilder(ClientLevel level, String label, EntityPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        CompositeContainerComponent.Builder entityBuilder = CompositeContainerComponent.builder()
            .translate(key("%s.matches".formatted(label)), Styler::condition)
            .space();
        EntityPredicateTooltip.addToBuilder(level, predicate, state, entityBuilder);
        builder.component(entityBuilder.build());
    }

    static void addTeamToBuilder(ClientLevel level, String team, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        builder.component(CompositeContainerComponent.builder()
            .space()
            .translate(key("team"), Styler.name(Component.literal(team)))
            .build());
    }

    static void addSlotsPredicateToBuilder(ClientLevel level, SlotsPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        Map<SlotRange, ItemPredicate> slots = predicate.slots();
        CompositeContainerComponent.Builder slotsBuilder = CompositeContainerComponent.builder()
            .space()
            .translate(key("slots.matches"), Styler::condition)
            .space()
            .cycle(slotCycler -> slots.forEach((slotRange, itemPredicate) -> {
                IntList slotsList = slotRange.slots();
                if (slotsList.isEmpty()) return;

                for (int i = 0; i < slotsList.size(); i++) {
                    int slot = slotsList.getInt(i);
                    CompositeContainerComponent.Builder slotBuilder = CompositeContainerComponent.builder()
                        .translate(key("slots.slot"), Styler::condition, Styler.number(slot))
                        .space();
                    ItemPredicateTooltip.addToBuilder(level, itemPredicate, new LootConditionTooltips.State(), slotBuilder);
                    slotCycler.component(slotBuilder.build());
                }
            }));
        builder.component(slotsBuilder.build());
    }

    static void addDataComponentMatchersToBuilder(ClientLevel level, DataComponentMatchers components, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        DataComponentMatchersTooltip.addToBuilder(level, components, state, builder);
    }
}
