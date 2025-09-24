package com.bawnorton.bettertrims.client.tooltip.condition.predicate;

import com.bawnorton.bettertrims.client.tooltip.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
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
    static void addToBuilder(ClientLevel level, EntityPredicate predicate, CompositeContainerComponent.Builder builder) {
        Optional<EntityTypePredicate> entityTypePredicate = predicate.entityType();
        if(entityTypePredicate.isPresent()) {
            addEntityTypePredicateToBuilder(level, entityTypePredicate.orElseThrow(), builder);
        }

        Optional<DistancePredicate> distancePredicate = predicate.distanceToPlayer();
        if(distancePredicate.isPresent()) {
            addDistancePredicateToBuilder(level, distancePredicate.orElseThrow(), builder);
        }

        Optional<MovementPredicate> movement = predicate.movement();
        if(movement.isPresent()) {
            addMovementPredicateToBuilder(level, movement.orElseThrow(), builder);
        }

        EntityPredicate.LocationWrapper location = predicate.location();
        Optional<LocationPredicate> located = location.located();
        if(located.isPresent()) {
            addLocationPredicateToBuilder(level, located.orElseThrow(), builder);
        }

        Optional<LocationPredicate> steppingOn = location.steppingOn();
        if(steppingOn.isPresent()) {
            addLocationPredicateToBuilder(level, steppingOn.orElseThrow(), builder);
        }

        Optional<LocationPredicate> affectsMovement = location.affectsMovement();
        if(affectsMovement.isPresent()) {
            addLocationPredicateToBuilder(level, affectsMovement.orElseThrow(), builder);
        }

        Optional<MobEffectsPredicate> effects = predicate.effects();
        if(effects.isPresent()) {
            addMobEffectsPredicateToBuilder(level, effects.orElseThrow(), builder);
        }

        Optional<NbtPredicate> nbt = predicate.nbt();
        if(nbt.isPresent()) {
            addNbtPredicateToBuilder(level, nbt.orElseThrow(), builder);
        }

        Optional<EntityFlagsPredicate> flags = predicate.flags();
        if(flags.isPresent()) {
            addEntityFlagsPredicateToBuilder(level, flags.orElseThrow(), builder);
        }

        Optional<EntityEquipmentPredicate> equipment = predicate.equipment();
        if(equipment.isPresent()) {
            addEntityEquipmentPredicateToBuilder(level, equipment.orElseThrow(), builder);
        }

        Optional<EntitySubPredicate> subPredicate = predicate.subPredicate();
        if(subPredicate.isPresent()) {
            addEntitySubPredicateToBuilder(level, subPredicate.orElseThrow(), builder);
        }

        Optional<Integer> periodicTick = predicate.periodicTick();
        if(periodicTick.isPresent()) {
            addPeriodicTickToBuilder(level, periodicTick.orElseThrow(), builder);
        }

        Optional<EntityPredicate> vehicle = predicate.vehicle();
        if(vehicle.isPresent()) {
            addEntityPredicateToBuilder(level, "vehicle", vehicle.orElseThrow(), builder);
        }

        Optional<EntityPredicate> passenger = predicate.passenger();
        if(passenger.isPresent()) {
            addEntityPredicateToBuilder(level, "passenger", passenger.orElseThrow(), builder);
        }

        Optional<EntityPredicate> targetedEntity = predicate.targetedEntity();
        if(targetedEntity.isPresent()) {
            addEntityPredicateToBuilder(level, "targeted_entity", targetedEntity.orElseThrow(), builder);
        }

        Optional<String> team = predicate.team();
        if(team.isPresent()) {
            addTeamToBuilder(level, team.orElseThrow(), builder);
        }

        Optional<SlotsPredicate> slots = predicate.slots();
        if(slots.isPresent()) {
            addSlotsPredicateToBuilder(level, slots.orElseThrow(), builder);
        }

        DataComponentMatchers components = predicate.components();
        if(!components.isEmpty()) {
            addDataComponentMatchersToBuilder(level, components, builder);
        }
    }

    static String key(String key) {
        return PredicateTooltip.key("entity.%s".formatted(key));
    }

    static void addEntityTypePredicateToBuilder(ClientLevel level, EntityTypePredicate predicate, CompositeContainerComponent.Builder builder) {
        PredicateTooltip.addRegisteredElementsToBuilder(level, key("matches"), Registries.ENTITY_TYPE, predicate.types(), EntityType::getDescription, builder);
    }

    static void addDistancePredicateToBuilder(ClientLevel level, DistancePredicate predicate, CompositeContainerComponent.Builder builder) {
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
        if(!x.isAny()) {
            addMinMaxToBuilder(key("distance.x"), false, x, distanceBuilder);
            useAnd = true;
        }
        if(!y.isAny()) {
            addMinMaxToBuilder(key("distance.y"), useAnd, y, distanceBuilder);
            useAnd = true;
        }
        if(!z.isAny()) {
            addMinMaxToBuilder(key("distance.z"), useAnd, z, distanceBuilder);
            useAnd = true;
        }
        if(!horizontal.isAny()) {
            addMinMaxToBuilder(key("distance.horizontal"), useAnd, horizontal, distanceBuilder);
            useAnd = true;
        }
        if(!absolute.isAny()) {
            addMinMaxToBuilder(key("distance.absolute"), useAnd, absolute, distanceBuilder);
        }
        builder.component(distanceBuilder.build());
    }

    static void addMovementPredicateToBuilder(ClientLevel level, MovementPredicate predicate, CompositeContainerComponent.Builder builder) {
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
        if(!x.isAny()) {
            addMinMaxToBuilder(key("movement.x"), false, x, movementBuilder);
            useAnd = true;
        }
        if(!y.isAny()) {
            addMinMaxToBuilder(key("movement.y"), useAnd, y, movementBuilder);
            useAnd = true;
        }
        if(!z.isAny()) {
            addMinMaxToBuilder(key("movement.z"), useAnd, z, movementBuilder);
            useAnd = true;
        }
        if(!horizontalSpeed.isAny()) {
            addMinMaxToBuilder(key("movement.horizontal_speed"), useAnd, horizontalSpeed, movementBuilder);
            useAnd = true;
        }
        if(!verticalSpeed.isAny()) {
            addMinMaxToBuilder(key("movement.vertical_speed"), useAnd, verticalSpeed, movementBuilder);
            useAnd = true;
        }
        if(!speed.isAny()) {
            addMinMaxToBuilder(key("movement.speed"), useAnd, speed, movementBuilder);
            useAnd = true;
        }
        if(!fallDistance.isAny()) {
            addMinMaxToBuilder(key("movement.fall_distance"), useAnd, fallDistance, movementBuilder);
        }
        builder.component(movementBuilder.build());
    }

    static void addLocationPredicateToBuilder(ClientLevel level, LocationPredicate predicate, CompositeContainerComponent.Builder builder) {
        LocationPredicateTooltip.addToBuilder(level, predicate, builder);
    }

    static void addMobEffectsPredicateToBuilder(ClientLevel level, MobEffectsPredicate predicate, CompositeContainerComponent.Builder builder) {
        Map<Holder<MobEffect>, MobEffectsPredicate.MobEffectInstancePredicate> effectMap = predicate.effectMap();
        if(effectMap.isEmpty()) {
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
                    if(ambient.isPresent()) {
                        conditionBuilder.translate(key("effects.ambient.%s".formatted(ambient.orElseThrow() ? "true" : "false")), Styler::value);
                        useAnd = true;
                    }
                    Optional<Boolean> visible = effectInstancePredicate.visible();
                    if(visible.isPresent()) {
                        if(useAnd) conditionBuilder.literal(", ", Styler::condition);
                        conditionBuilder.translate(key("effects.visible.%s".formatted(visible.orElseThrow() ? "true" : "false")), Styler::value);
                        useAnd = true;
                    }
                    MinMaxBounds.Ints amplifier = effectInstancePredicate.amplifier();
                    if(!amplifier.isAny()) {
                        if(useAnd) conditionBuilder.literal(", ", Styler::condition);
                        addMinMaxToBuilder(
                            key("effects.amplifier"),
                            false,
                            amplifier,
                            value -> Component.translatable("enchantment.level.%s".formatted("%.0f".formatted(value + 1))),
                            conditionBuilder
                        );
                        useAnd = true;
                    }
                    MinMaxBounds.Ints duration = effectInstancePredicate.duration();
                    if(!duration.isAny()) {
                        if(useAnd) conditionBuilder.literal(", ", Styler::condition);
                        addMinMaxToBuilder(
                            key("effects.duration"),
                            false,
                            duration,
                            i -> Component.literal("%.0fs".formatted(i * 20)),
                            conditionBuilder
                        );
                    }
                    CompositeContainerComponent effectConditions = conditionBuilder.build();
                    if(!effectConditions.isEmpty()) {
                        effectBuilder.literal(" (", Styler::condition)
                            .component(effectConditions)
                            .literal(")", Styler::condition);
                    }
                    effectCycler.component(effectBuilder.build());
                }));
            builder.component(effectsBuilder.build());
        }
    }

    static void addNbtPredicateToBuilder(ClientLevel level, NbtPredicate predicate, CompositeContainerComponent.Builder builder) {
        builder.component(CompositeContainerComponent.builder().space().translate(key("nbt")).build());
    }

    static void addEntityFlagsPredicateToBuilder(ClientLevel level, EntityFlagsPredicate predicate, CompositeContainerComponent.Builder builder) {
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
            if(entry.getValue().isPresent()) {
                if(useAnd) {
                    flagsBuilder.literal(" & ", Styler::condition);
                }
                flagsBuilder.translate(key("flags.%s.%s".formatted(entry.getKey(), entry.getValue().orElseThrow() ? "true" : "false")), Styler::value);
                useAnd = true;
            }
        }
    }

    static void addEntityEquipmentPredicateToBuilder(ClientLevel level, EntityEquipmentPredicate predicate, CompositeContainerComponent.Builder builder) {
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
                    if(itemPredicate.isPresent()) {
                        Component slotName = Styler.name(Component.translatable("slot.%s".formatted(slot.getName())));
                        CompositeContainerComponent.Builder itemBuilder = CompositeContainerComponent.builder()
                            .translate(key("equipment.slot"), Styler::condition, slotName)
                            .space();
                        ItemPredicateTooltip.addToBuilder(level, itemPredicate.orElseThrow(), itemBuilder);
                        equipmentCycler.component(itemBuilder.build());
                    }
                });
            });
        builder.component(equipmentBuilder.build());
    }

    static void addEntitySubPredicateToBuilder(ClientLevel level, EntitySubPredicate predicate, CompositeContainerComponent.Builder builder) {
        EntitySubPredicateTooltip.addToBuilder(level, predicate, builder);
    }

    static void addPeriodicTickToBuilder(ClientLevel level, int tick, CompositeContainerComponent.Builder builder) {
        builder.component(CompositeContainerComponent.builder()
            .space()
            .translate(key("periodic_tick"), Styler.number(Component.literal(String.valueOf(tick))))
            .build());
    }

    static void addEntityPredicateToBuilder(ClientLevel level, String label, EntityPredicate predicate, CompositeContainerComponent.Builder builder) {
        CompositeContainerComponent.Builder entityBuilder = CompositeContainerComponent.builder()
            .translate(key("%s.matches".formatted(label)), Styler::condition)
            .space();
        EntityPredicateTooltip.addToBuilder(level, predicate, entityBuilder);
        builder.component(entityBuilder.build());
    }

    static void addTeamToBuilder(ClientLevel level, String team, CompositeContainerComponent.Builder builder) {
        builder.component(CompositeContainerComponent.builder()
            .space()
            .translate(key("team"), Styler.name(Component.literal(team)))
            .build());
    }

    static void addSlotsPredicateToBuilder(ClientLevel level, SlotsPredicate predicate, CompositeContainerComponent.Builder builder) {
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
                        .translate(key("slots.slot"), Styler::condition, Styler.number(Component.literal(String.valueOf(slot))))
                        .space();
                    ItemPredicateTooltip.addToBuilder(level, itemPredicate, slotBuilder);
                    slotCycler.component(slotBuilder.build());
                }
            }));
        builder.component(slotsBuilder.build());
    }

    static void addDataComponentMatchersToBuilder(ClientLevel level, DataComponentMatchers components, CompositeContainerComponent.Builder builder) {
        DataComponentMatchersTooltip.addToBuilder(level, components, builder);
    }
}
