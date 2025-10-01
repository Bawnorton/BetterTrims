package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data;

import com.bawnorton.bettertrims.client.mixin.accessor.AdventureModePredicateAccessor;
import com.bawnorton.bettertrims.client.tooltip.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.component.ItemComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.BlockPredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.EntityPredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.condition.predicate.PredicateTooltip;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AdventureModePredicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SequencedSet;
import java.util.function.Function;

public interface ExactDataComponentPredicateTooltip {
    Map<DataComponentType<?>, ExactAdder<?>> EXACT_ADDERS = Util.make(
        new HashMap<>(), map -> {
            map.put(
                DataComponents.CUSTOM_DATA,
                (ClientLevel level, CustomData customData, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    if (customData.isEmpty()) {
                        builder.translate(key("custom_data.any"), Styler::condition);
                    } else {
                        EntityPredicateTooltip.addNbtPredicateToBuilder(level, new NbtPredicate(customData.copyTag()), state, builder);
                    }
                }
            );

            map.put(
                DataComponents.MAX_STACK_SIZE,
                (ClientLevel level, Integer maxStackSize, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.translate(
                    key("max_stack_size"),
                    Styler::condition,
                    Styler.number(Component.literal(maxStackSize.toString()))
                )
            );

            map.put(
                DataComponents.MAX_DAMAGE,
                (ClientLevel level, Integer maxDamage, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.translate(
                    key("max_damage"),
                    Styler::condition,
                    Styler.number(Component.literal(maxDamage.toString()))
                )
            );

            map.put(
                DataComponents.DAMAGE,
                (ClientLevel level, Integer damage, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.translate(
                    key("damage"),
                    Styler::condition,
                    Styler.number(Component.literal(damage.toString()))
                )
            );

            map.put(
                DataComponents.UNBREAKABLE,
                (ClientLevel level, Unit unbreakable, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.translate(
                    key("unbreakable"),
                    Styler::condition
                )
            );

            map.put(
                DataComponents.CUSTOM_NAME,
                (ClientLevel level, Component name, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.translate(
                    key("custom_name"),
                    Styler::condition,
                    Styler.value(name.copy())
                )
            );

            map.put(
                DataComponents.ITEM_NAME,
                (ClientLevel level, Component name, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.translate(
                    key("item_name"),
                    Styler::condition,
                    Styler.value(name.copy())
                )
            );

            map.put(
                DataComponents.ITEM_MODEL,
                (ClientLevel level, ResourceLocation model, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.translate(
                    key("item_model"),
                    Styler::condition,
                    Styler.value(Component.literal(model.toString()))
                )
            );

            map.put(
                DataComponents.LORE,
                (ClientLevel level, ItemLore lore, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    if (lore.lines().isEmpty()) {
                        builder.translate(key("lore.any"), Styler::condition);
                    } else {
                        CompositeContainerComponent.Builder loreBuilder = CompositeContainerComponent.builder()
                            .space()
                            .translate(key("lore"), Styler::condition)
                            .space()
                            .cycle(cycleBuilder -> lore.styledLines().forEach(line -> cycleBuilder.textComponent(line.copy())));
                        builder.component(loreBuilder.build());
                    }
                }
            );

            map.put(
                DataComponents.RARITY,
                (ClientLevel level, Rarity rarity, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.translate(
                    key("rarity"),
                    Styler::condition,
                    Component.literal(rarity.name()).withStyle(rarity.color())
                )
            );

            map.put(
                DataComponents.ENCHANTMENTS,
                (ClientLevel level, ItemEnchantments enchantments, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    if (enchantments.isEmpty()) {
                        builder.translate(key("enchantments.any"), Styler::condition);
                    } else {
                        Registry<Enchantment> registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                        CompositeContainerComponent.Builder enchantmentsBuilder = CompositeContainerComponent.builder()
                            .space()
                            .translate(key("enchantments"), Styler::condition)
                            .space()
                            .cycle(cycleBuilder -> enchantments.entrySet().forEach(entry -> {
                                Holder<Enchantment> key = entry.getKey();
                                Enchantment enchantment = key.unwrap().map(registry::getValueOrThrow, Function.identity());
                                int enchantmentLevel = entry.getIntValue();
                                cycleBuilder.component(CompositeContainerComponent.builder()
                                    .translate(
                                        key("enchantments.entry"),
                                        Styler::condition,
                                        Styler.name(enchantment.description().copy()),
                                        Styler.number(enchantmentLevel)
                                    )
                                    .build());
                            }));
                        builder.component(enchantmentsBuilder.build());
                    }
                }
            );

            map.put(
                DataComponents.CAN_PLACE_ON,
                (ClientLevel level, AdventureModePredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    AdventureModePredicateAccessor accessor = (AdventureModePredicateAccessor) predicate;
                    List<BlockPredicate> blockPredicates = accessor.bettertrims$predicates();
                    if (blockPredicates.isEmpty()) {
                        builder.translate(key("can_place_on.any"), Styler::condition);
                    } else {
                        CompositeContainerComponent.Builder canPlaceOnBuilder = CompositeContainerComponent.builder()
                            .space()
                            .translate(key("can_place_on"), Styler::condition)
                            .space()
                            .cycle(cycleBuilder -> blockPredicates.forEach(blockPredicate -> {
                                CompositeContainerComponent.Builder blockBuilder = CompositeContainerComponent.builder();
                                BlockPredicateTooltip.addToBuilder(level, blockPredicate, state, blockBuilder);
                                cycleBuilder.component(blockBuilder.build());
                            }));
                        builder.component(canPlaceOnBuilder.build());
                    }
                }
            );

            map.put(
                DataComponents.CAN_BREAK,
                (ClientLevel level, AdventureModePredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    AdventureModePredicateAccessor accessor = (AdventureModePredicateAccessor) predicate;
                    List<BlockPredicate> blockPredicates = accessor.bettertrims$predicates();
                    if (blockPredicates.isEmpty()) {
                        builder.translate(key("can_break.any"), Styler::condition);
                    } else {
                        CompositeContainerComponent.Builder canBreakBuilder = CompositeContainerComponent.builder()
                            .space()
                            .translate(key("can_break"), Styler::condition)
                            .space()
                            .cycle(cycleBuilder -> blockPredicates.forEach(blockPredicate -> {
                                CompositeContainerComponent.Builder blockBuilder = CompositeContainerComponent.builder();
                                BlockPredicateTooltip.addToBuilder(level, blockPredicate, state, blockBuilder);
                                cycleBuilder.component(blockBuilder.build());
                            }));
                        builder.component(canBreakBuilder.build());
                    }
                }
            );

            map.put(
                DataComponents.ATTRIBUTE_MODIFIERS,
                (ClientLevel level, ItemAttributeModifiers modifiers, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    List<ItemAttributeModifiers.Entry> entries = modifiers.modifiers();
                    if (entries.isEmpty()) {
                        builder.translate(key("attribute_modifiers.any"), Styler::condition);
                        return;
                    }

                    builder.space()
                        .translate(key("attribute_modifiers"), Styler::condition)
                        .space()
                        .cycle(cycleBuilder -> entries.forEach(entry -> {
                            CompositeContainerComponent.Builder entryBuilder = CompositeContainerComponent.builder();
                            Holder<Attribute> attribute = entry.attribute();
                            EquipmentSlotGroup slot = entry.slot();
                            AttributeModifier modifier = entry.modifier();
                            ItemAttributeModifiers.Display.attributeModifiers()
                                .apply(
                                    component -> entryBuilder.textComponent(component)
                                        .space()
                                        .translate(
                                            key("attribute_modifiers.slot"),
                                            Styler::condition,
                                            Styler.name(Component.literal(StringUtils.capitalize(slot.getSerializedName())))
                                        ),
                                    Minecraft.getInstance().player,
                                    attribute,
                                    modifier
                                );
                            cycleBuilder.component(entryBuilder.build());
                        }));
                }
            );

            map.put(
                DataComponents.CUSTOM_MODEL_DATA,
                (ClientLevel level, CustomModelData customModelData, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    List<Integer> colors = customModelData.colors();
                    List<Boolean> flags = customModelData.flags();
                    List<Float> floats = customModelData.floats();
                    List<String> strings = customModelData.strings();
                    if (colors.isEmpty() && flags.isEmpty() && floats.isEmpty() && strings.isEmpty()) {
                        builder.translate(key("custom_model_data.any"), Styler::condition);
                    } else {
                        CompositeContainerComponent.Builder customModelDataBuilder = CompositeContainerComponent.builder()
                            .space()
                            .translate(key("custom_model_data"), Styler::condition)
                            .space()
                            .cycle(cycleBuilder -> {
                                int maxSize = Math.max(Math.max(colors.size(), flags.size()), Math.max(floats.size(), strings.size()));
                                for (int i = 0; i < maxSize; i++) {
                                    CompositeContainerComponent.Builder entryBuilder = CompositeContainerComponent.builder();
                                    entryBuilder.space()
                                        .translate(key("custom_model_data.index"), Styler::condition, Styler.number(i))
                                        .space();
                                    if (i < colors.size()) {
                                        entryBuilder.translate(
                                            key("custom_model_data.color"),
                                            Styler::condition,
                                            Styler.number(colors.get(i))
                                        );
                                    }
                                    if (i < flags.size()) {
                                        if (i < colors.size()) entryBuilder.literal(",", Styler::condition).space();
                                        entryBuilder.translate(
                                            key("custom_model_data.flag"),
                                            Styler::condition,
                                            Styler.value(Component.literal(String.valueOf(flags.get(i))))
                                        );
                                    }
                                    if (i < floats.size()) {
                                        if (i < colors.size() || i < flags.size()) entryBuilder.literal(",", Styler::condition).space();
                                        entryBuilder.translate(
                                            key("custom_model_data.float"),
                                            Styler::condition,
                                            Styler.number(floats.get(i))
                                        );
                                    }
                                    if (i < strings.size()) {
                                        if (i < colors.size() || i < flags.size() || i < floats.size()) entryBuilder.literal(",", Styler::condition).space();
                                        entryBuilder.translate(
                                            key("custom_model_data.string"),
                                            Styler::condition,
                                            Styler.value(Component.literal(strings.get(i)))
                                        );
                                    }
                                    cycleBuilder.component(entryBuilder.build());
                                }
                            });
                        builder.component(customModelDataBuilder.build());
                    }
                }
            );

            map.put(
                DataComponents.TOOLTIP_DISPLAY,
                (ClientLevel level, TooltipDisplay display, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    boolean hideTooltip = display.hideTooltip();
                    SequencedSet<DataComponentType<?>> hiddenComponents = display.hiddenComponents();
                    if (hideTooltip) {
                        builder.translate(key("tooltip_display.hide_tooltip"), Styler::condition);
                    }

                    if (hiddenComponents.isEmpty()) {
                        builder.space().translate(key("tooltip_display.none"), Styler::condition);
                    } else {
                        CompositeContainerComponent.Builder tooltipDisplayBuilder = CompositeContainerComponent.builder()
                            .space()
                            .translate(key("tooltip_display.hidden_components"), Styler::condition)
                            .space()
                            .cycle(cycleBuilder -> hiddenComponents.forEach(type -> {
                                ResourceLocation typeKey = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type);
                                String typeName = typeKey != null ? typeKey.toString() : "unregistered";
                                cycleBuilder.textComponent(Styler.name(Component.literal(typeName)));
                            }));
                        builder.component(tooltipDisplayBuilder.build());
                    }
                }
            );

            map.put(
                DataComponents.REPAIR_COST,
                (ClientLevel level, Integer repairCost, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.translate(
                    key("repair_cost"),
                    Styler::condition,
                    Styler.number(Component.literal(repairCost.toString()))
                )
            );

            map.put(
                DataComponents.CREATIVE_SLOT_LOCK,
                (ClientLevel level, Unit locked, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.translate(
                    key("creative_slot_lock"),
                    Styler::condition
                )
            );

            map.put(
                DataComponents.ENCHANTMENT_GLINT_OVERRIDE,
                (ClientLevel level, Boolean glint, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.translate(
                    key("enchantment_glint_override.%s".formatted(glint.toString())),
                    Styler::condition
                )
            );

            map.put(
                DataComponents.INTANGIBLE_PROJECTILE,
                (ClientLevel level, Unit intangible, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.translate(
                    key("intangible_projectile"),
                    Styler::condition
                )
            );

            map.put(
                DataComponents.FOOD,
                (ClientLevel level, FoodProperties food, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    boolean canAlwaysEat = food.canAlwaysEat();
                    int nutrition = food.nutrition();
                    float saturation = food.saturation();

                    CompositeContainerComponent.Builder foodBuilder = CompositeContainerComponent.builder()
                        .space()
                        .translate(key("food"), Styler::condition)
                        .space()
                        .translate(
                            key("food.nutrition"),
                            Styler::condition,
                            Styler.number(nutrition)
                        )
                        .space()
                        .translate(
                            key("food.saturation"),
                            Styler::condition,
                            Styler.number(saturation)
                        );
                    if (canAlwaysEat) {
                        foodBuilder.space().translate(key("food.can_always_eat"), Styler::condition);
                    }
                    builder.component(foodBuilder.build());
                }
            );

            map.put(
                DataComponents.CONSUMABLE,
                (ClientLevel level, Consumable consumable, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    Holder<SoundEvent> soundHolder = consumable.sound();
                    ItemUseAnimation animation = consumable.animation();
                    float consumeSeconds = consumable.consumeSeconds();
                    boolean hasConsumeParticles = consumable.hasConsumeParticles();

                    CompositeContainerComponent.Builder consumableBuilder = CompositeContainerComponent.builder()
                        .space()
                        .translate(key("consumable"), Styler::condition)
                        .space()
                        .translate(
                            key("consumable.animation"),
                            Styler::condition,
                            Styler.name(Component.literal(StringUtils.capitalize(animation.getSerializedName())))
                        )
                        .space()
                        .translate(
                            key("consumable.consume_seconds"),
                            Styler::condition,
                            Styler.number(consumeSeconds)
                        );

                    RegistryAccess registryAccess = level.registryAccess();
                    Registry<SoundEvent> registry = registryAccess.lookupOrThrow(Registries.SOUND_EVENT);
                    ResourceLocation sound = soundHolder.unwrap().map(ResourceKey::location, registry::getKey);
                    Component soundName = Component.literal(sound.toString());
                    WeighedSoundEvents soundEvents = Minecraft.getInstance().getSoundManager().getSoundEvent(sound);
                    if (soundEvents != null && soundEvents.getSubtitle() != null) {
                        soundName = soundEvents.getSubtitle();
                    }
                    consumableBuilder.space()
                        .translate(
                            key("consumable.sound"),
                            Styler::condition,
                            Styler.name(soundName.copy())
                        );

                    if (hasConsumeParticles) {
                        consumableBuilder.space().translate(key("consumable.has_consume_particles"), Styler::condition);
                    }
                }
            );

            map.put(
                DataComponents.USE_REMAINDER,
                (ClientLevel level, UseRemainder remainder, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    ItemStack itemStack = remainder.convertInto();
                    CompositeContainerComponent.Builder remainderBuilder = CompositeContainerComponent.builder()
                        .centred()
                        .space()
                        .translate(key("use_remainder"), Styler::condition)
                        .space()
                        .component(new ItemComponent(itemStack));
                    builder.component(remainderBuilder.build());
                }
            );

            map.put(
                DataComponents.USE_COOLDOWN,
                (ClientLevel level, UseCooldown cooldown, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    float seconds = cooldown.seconds();
                    Optional<ResourceLocation> resourceLocation = cooldown.cooldownGroup();
                    CompositeContainerComponent.Builder cooldownBuilder = CompositeContainerComponent.builder()
                        .space()
                        .translate(
                            key("use_cooldown"),
                            Styler::condition,
                            Styler.number(seconds)
                        );
                    resourceLocation.ifPresent(location -> cooldownBuilder.space().translate(
                        key("use_cooldown.group"),
                        Styler::condition,
                        Styler.name(Component.literal(location.toString()))
                    ));
                    builder.component(cooldownBuilder.build());
                }
            );

            map.put(
                DataComponents.DAMAGE_RESISTANT,
                (ClientLevel level, DamageResistant resistant, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    TagKey<DamageType> types = resistant.types();
                    CompositeContainerComponent.Builder resistantBuilder = CompositeContainerComponent.builder()
                        .space()
                        .translate(key("damage_resistant"), Styler::condition)
                        .space()
                        .cycle(cycleBuilder -> {
                            Registry<DamageType> registry = level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
                            registry.getOrThrow(types).forEach(type -> {
                                ResourceLocation damageType = type.unwrap().map(ResourceKey::location, registry::getKey);
                                cycleBuilder.literal(damageType.toString(), Styler::name);
                            });
                        });
                    builder.component(resistantBuilder.build());
                }
            );

            map.put(
                DataComponents.TOOL,
                (ClientLevel level, Tool tool, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    boolean canDestroyBlocksInCreative = tool.canDestroyBlocksInCreative();
                    int damagePerBlock = tool.damagePerBlock();
                    float defaultMiningSpeed = tool.defaultMiningSpeed();
                    List<Tool.Rule> rules = tool.rules();
                    CompositeContainerComponent.Builder toolBuilder = CompositeContainerComponent.builder()
                        .space()
                        .translate(key("tool"), Styler::condition)
                        .space()
                        .translate(
                            key("tool.damage_per_block"),
                            Styler::condition,
                            Styler.number(damagePerBlock)
                        )
                        .space()
                        .translate(
                            key("tool.default_mining_speed"),
                            Styler::condition,
                            Styler.number(defaultMiningSpeed)
                        );
                    if (canDestroyBlocksInCreative) {
                        toolBuilder.space().translate(key("tool.can_destroy_blocks_in_creative"), Styler::condition);
                    }
                    if (!rules.isEmpty()) {
                        toolBuilder.space()
                            .translate(key("tool.rules"), Styler::condition)
                            .space()
                            .cycle(cycleBuilder -> {
                                for (Tool.Rule rule : rules) {
                                    HolderSet<Block> blocks = rule.blocks();
                                    Optional<TagKey<Block>> blockTagKey = blocks.unwrapKey();
                                    Optional<Float> speed = rule.speed();
                                    Optional<Boolean> correctForDrops = rule.correctForDrops();
                                    CompositeContainerComponent.Builder ruleBuilder = CompositeContainerComponent.builder()
                                        .translate(key("tool.rule"), Styler::condition)
                                        .spaced();
                                    if (blockTagKey.isPresent()) {
                                        ResourceLocation tag = blockTagKey.orElseThrow().location();
                                        ruleBuilder.translate(
                                            key("tool.rule.tag"),
                                            Styler::condition,
                                            Styler.name(Component.literal(tag.toString()))
                                        );
                                    }
                                    if (speed.isPresent()) {
                                        ruleBuilder.space().translate(
                                            key("tool.rule.speed"),
                                            Styler::condition,
                                            Styler.number(speed.orElseThrow())
                                        );
                                    }
                                    if (correctForDrops.isPresent() && correctForDrops.orElse(false)) {
                                        ruleBuilder.space().translate(key("tool.rule.correct_for_drops"), Styler::condition);
                                    }
                                    if (!ruleBuilder.build().isEmpty()) {
                                        cycleBuilder.component(ruleBuilder.build());
                                    }
                                }
                            });
                    }
                }
            );

            map.put(
                DataComponents.WEAPON,
                (ClientLevel level, Weapon weapon, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    int itemDamagePerAttack = weapon.itemDamagePerAttack();
                    float disableBlockingForSeconds = weapon.disableBlockingForSeconds();
                    CompositeContainerComponent.Builder weaponBuilder = CompositeContainerComponent.builder()
                        .space()
                        .translate(key("weapon"), Styler::condition)
                        .space()
                        .translate(
                            key("weapon.item_damage_per_attack"),
                            Styler::condition,
                            Styler.number(itemDamagePerAttack)
                        )
                        .space()
                        .translate(
                            key("weapon.disable_blocking_for_seconds"),
                            Styler::condition,
                            Styler.number(disableBlockingForSeconds)
                        );
                    builder.component(weaponBuilder.build());
                }
            );

            map.put(
                DataComponents.ENCHANTABLE,
                (ClientLevel level, Enchantable enchantable, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.translate(
                    key("enchantable"),
                    Styler::condition,
                    Styler.number(enchantable.value())
                )
            );

            map.put(
                DataComponents.EQUIPPABLE,
                (ClientLevel level, Equippable equippable, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    // TODO
                    builder.literal("TODO");
                }
            );

            map.put(
                DataComponents.REPAIRABLE,
                (ClientLevel level, Repairable repairable, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    HolderSet<Item> items = repairable.items();
                    if (items.size() == 0) {
                        builder.translate(key("repairable.none"), Styler::condition);
                    } else {
                        PredicateTooltip.addRegisteredElementsToBuilder(level, key("repairable.items"), Registries.ITEM, items, item -> item.getName(), state, builder);
                    }
                }
            );

            map.put(
                DataComponents.GLIDER,
                (ClientLevel level, Unit glider, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.translate(
                    key("glider"),
                    Styler::condition
                )
            );

            map.put(
                DataComponents.TOOLTIP_STYLE,
                (ClientLevel level, ResourceLocation tooltipStyle, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> builder.translate(
                    key("tooltip_style"),
                    Styler::condition,
                    Styler.value(Component.literal(tooltipStyle.toString()))
                )
            );

            map.put(
                DataComponents.DEATH_PROTECTION,
                (ClientLevel level, DeathProtection protection, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) -> {
                    List<ConsumeEffect> deathEffects = protection.deathEffects();
                    builder.space()
                        .translate(key("death_protection"), Styler::condition)
                        .space();
                    if (deathEffects.isEmpty()) {
                        builder.translate(key("death_protection.no_effects"), Styler::condition);
                    } else {
                        builder.cycle(cycleBuilder -> deathEffects.forEach(effect -> {
                            CompositeContainerComponent.Builder effectBuilder = CompositeContainerComponent.builder();
                            // TODO
                            switch (effect) {
                                case ApplyStatusEffectsConsumeEffect applyStatus -> {
                                    List<MobEffectInstance> effects = applyStatus.effects();
                                }
                                default -> {}
                            }
                            cycleBuilder.component(effectBuilder.build());
                        }));
                    }
                }
            );

            // TODO
        }
    );

    @SuppressWarnings("unchecked")
    static void addToBuilder(ClientLevel level, DataComponentType<?> type, Object object, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
        ExactAdder<Object> adder = (ExactAdder<Object>) EXACT_ADDERS.getOrDefault(type, ExactAdder.UNKNOWN.apply(type));
        adder.addToBuilder(level, object, state, builder);
    }

    static String key(String key) {
        return PredicateTooltip.key("data.exact.%s".formatted(key));
    }

    interface ExactAdder<T> {
        Function<DataComponentType<?>, ExactAdder<?>> UNKNOWN = type -> (level, object, state, builder) -> builder.translate(
            key("unknown_type"),
            Styler::property,
            Objects.requireNonNullElse(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type), "[unregistred]").toString()
        );

        void addToBuilder(ClientLevel level, T object, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder);
    }
}
