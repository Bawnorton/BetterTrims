package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.PredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.exact.*;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.exact.*;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

public final class ExactDataComponentPredicateTooltipAdders {
	private static final Map<DataComponentType<?>, ExactAdder<?>> EXACT_ADDERS = new HashMap<>();

	static {
		register(DataComponents.CUSTOM_DATA, new CustomDataExactAdder("custom"));
		register(DataComponents.MAX_STACK_SIZE, ExactAdder.simple((maxStackSize, builder) ->
				builder.translate(key("max_stack_size"), Styler::condition, Styler.number(maxStackSize))
		));
		register(DataComponents.MAX_DAMAGE, ExactAdder.simple((maxDamage, builder) ->
				builder.translate(key("max_damage"), Styler::condition, Styler.number(maxDamage))
		));
		register(DataComponents.DAMAGE, ExactAdder.simple((damage, builder) ->
				builder.translate(key("damage"), Styler::condition, Styler.number(damage))
		));
		register(DataComponents.UNBREAKABLE, ExactAdder.simple((unit, builder) ->
				builder.translate(key("unbreakable"), Styler::condition)
		));
		register(DataComponents.CUSTOM_NAME, ExactAdder.simple((name, builder) ->
				builder.translate(key("custom_name"), Styler::condition, Styler.value(name.copy()))
		));
		register(DataComponents.ITEM_NAME, ExactAdder.simple((name, builder) ->
				builder.translate(key("item_name"), Styler::condition, Styler.value(name.copy()))
		));
		register(DataComponents.LORE, new ItemLoreExactAdder());
		register(DataComponents.RARITY, ExactAdder.simple((rarity, builder) ->
				builder.translate(key("rarity"), Styler::condition, Component.literal(rarity.name()).withStyle(rarity.color()))
		));
		register(DataComponents.ENCHANTMENTS, new EnchantmentsExactAdder("enchantments"));
		register(DataComponents.CAN_PLACE_ON, new AdventureModePredicateExactAdder("can_place_on"));
		register(DataComponents.CAN_BREAK, new AdventureModePredicateExactAdder("can_break"));
		register(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiersExactAdder());
		register(DataComponents.CUSTOM_MODEL_DATA, new CustomModelDataExactAdder());
		register(DataComponents.REPAIR_COST, ExactAdder.simple((repairCost, builder) ->
				builder.translate(key("repair_cost"), Styler::condition, Styler.number(Component.literal(repairCost.toString())))
		));
		register(DataComponents.CREATIVE_SLOT_LOCK, ExactAdder.simple((unit, builder) ->
				builder.translate(key("creative_slot_lock"), Styler::condition)
		));
		register(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, ExactAdder.simple((glint, builder) ->
				builder.translate(key("enchantment_glint_override.%s".formatted(glint.toString())), Styler::condition)
		));
		register(DataComponents.INTANGIBLE_PROJECTILE, ExactAdder.simple((unit, builder) ->
				builder.translate(key("intangible_projectile"), Styler::condition)
		));
		register(DataComponents.FOOD, new FoodPropertiesExactAdder());
		register(DataComponents.TOOL, new ToolExactAdder());
		register(DataComponents.STORED_ENCHANTMENTS, new EnchantmentsExactAdder("stored_enchantments"));
		register(DataComponents.DYED_COLOR, new DyedItemColorExactAdder());
		register(DataComponents.MAP_COLOR, new MapItemColorExactAdder());
		register(DataComponents.MAP_ID, ExactAdder.simple((mapId, builder) ->
				builder.translate(key("map_id"), Styler::condition, Styler.number(mapId.id()))
		));
		register(DataComponents.MAP_DECORATIONS, new MapDecorationsExactAdder());
		register(DataComponents.MAP_POST_PROCESSING, ExactAdder.ofEnum("map_post_processing"));
		register(DataComponents.CHARGED_PROJECTILES, new ChargedProjectilesExactAdder());
		register(DataComponents.BUNDLE_CONTENTS, new BundleContentsExactAdder());
		register(DataComponents.POTION_CONTENTS, new PotionContentsExactAdder());
		register(DataComponents.SUSPICIOUS_STEW_EFFECTS, new SuspiciousStewEffectsExactAdder());
		register(DataComponents.WRITABLE_BOOK_CONTENT, new WritableBookContentExactAdder());
		register(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContentExactAdder());
		register(DataComponents.TRIM, new ArmorTrimExactAdder());
		register(DataComponents.DEBUG_STICK_STATE, new DebugStickStateExactAdder());
		register(DataComponents.BUCKET_ENTITY_DATA, new CustomDataExactAdder("bucket_entity"));
		register(DataComponents.JUKEBOX_PLAYABLE, new JukeboxPlayableExactAdder());
		register(DataComponents.LODESTONE_TRACKER, new LodestoneTrackerExactAdder());
		register(DataComponents.FIREWORK_EXPLOSION, new FireworkExplosionExactAdder());
		register(DataComponents.FIREWORKS, new FireworksExactAdder());
		register(DataComponents.PROFILE, new ResolvableProfileExactAdder());
		register(DataComponents.NOTE_BLOCK_SOUND, ExactAdder.simple((sound, builder) ->
				builder.translate(key("note_block_sound"), Styler::condition, Styler.name(Component.literal(sound.toString())))
		));
		register(DataComponents.BANNER_PATTERNS, new BannerPatternLayersExactAdder());
		register(DataComponents.BASE_COLOR, ExactAdder.ofEnum("base_color"));
		register(DataComponents.POT_DECORATIONS, new PotDecorationsExactAdder());
		register(DataComponents.CONTAINER, new ItemContainerContentsExactAdder());
		register(DataComponents.BLOCK_STATE, new BlockItemStatePropertiesExactAdder());
		register(DataComponents.CONTAINER_LOOT, new SeededContainerLootExactAdder());
		//? if >=1.21.8 {
		//? if >=1.21.10 {
		register(DataComponents.ENTITY_DATA, new TypedEntityDataExactAdder<>("entity"));
		register(DataComponents.BLOCK_ENTITY_DATA, new TypedEntityDataExactAdder<>("block_entity"));
		//?} else {
		/*register(DataComponents.ENTITY_DATA, new CustomDataExactAdder("entity"));
		register(DataComponents.BLOCK_ENTITY_DATA, new CustomDataExactAdder("block_entity"));
		*///?}
		register(DataComponents.ITEM_MODEL, ExactAdder.simple((itemModel, builder) ->
				builder.translate(key("item_model"), Styler::condition, Styler.value(Component.literal(itemModel.toString())))
		));
		register(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplayExactAdder());
		register(DataComponents.CONSUMABLE, new ConsumableExactAdder());
		register(DataComponents.USE_REMAINDER, new UseRemainderExactAdder());
		register(DataComponents.USE_COOLDOWN, new UseCooldownExactAdder());
		register(DataComponents.DAMAGE_RESISTANT, new DamageResistantExactAdder());
		register(DataComponents.WEAPON, new WeaponExactAdder());
		register(DataComponents.ENCHANTABLE, ExactAdder.simple((enchantable, builder) ->
				builder.translate(key("enchantable"), Styler::condition, Styler.number(enchantable.value()))
		));
		register(DataComponents.EQUIPPABLE, new EquippableExactAdder());
		register(DataComponents.REPAIRABLE, new RepairableExactAdder());
		register(DataComponents.GLIDER, ExactAdder.simple((unit, builder) ->
				builder.translate(key("glider"), Styler::condition)
		));
		register(DataComponents.TOOLTIP_STYLE, ExactAdder.simple((tooltipStyle, builder) ->
				builder.translate(key("tooltip_style"), Styler::condition, Styler.value(Component.literal(tooltipStyle.toString()))
				)));
		register(DataComponents.DEATH_PROTECTION, new DeathProtectionExactAdder());
		register(DataComponents.BLOCKS_ATTACKS, new BlocksAttacksExactAdder());
		register(DataComponents.POTION_DURATION_SCALE, ExactAdder.simple((durationScale, builder) ->
				builder.translate(key("potion_duration_scale"), Styler::condition, Styler.number(durationScale))
		));
		register(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterialExactAdder());
		register(DataComponents.LOCK, new LockCodeExactAdder());
		register(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, ExactAdder.simple((amplifier, builder) ->
				builder.translate(key("ominous_bottle_amplifier"), Styler::condition, Styler.number(amplifier.value()))
		));
		register(DataComponents.INSTRUMENT, new InstrumentComponentExactAdder());
		register(DataComponents.RECIPES, new RecipeResourceKeyListExactAdder());
		register(DataComponents.BEES, new BeesExactAdder());
		register(DataComponents.BREAK_SOUND, new SoundEventHolderExactAdder());
		register(DataComponents.VILLAGER_VARIANT, new VillagerTypeHolderExactAdder());
		register(DataComponents.WOLF_VARIANT, new WolfVariantHolderExactAdder());
		register(DataComponents.WOLF_SOUND_VARIANT, new WolfSoundVariantHolderExactAdder());
		register(DataComponents.WOLF_COLLAR, ExactAdder.ofEnum("wolf_collar"));
		register(DataComponents.FOX_VARIANT, ExactAdder.ofEnum("fox_variant"));
		register(DataComponents.SALMON_SIZE, ExactAdder.ofEnum("salmon_size"));
		register(DataComponents.PARROT_VARIANT, ExactAdder.ofEnum("parrot_variant"));
		register(DataComponents.TROPICAL_FISH_PATTERN, ExactAdder.ofEnum("tropical_fish_pattern"));
		register(DataComponents.TROPICAL_FISH_BASE_COLOR, ExactAdder.ofEnum("tropical_fish_base_color"));
		register(DataComponents.TROPICAL_FISH_PATTERN_COLOR, ExactAdder.ofEnum("tropical_fish_pattern_color"));
		register(DataComponents.MOOSHROOM_VARIANT, ExactAdder.ofEnum("mooshroom_variant"));
		register(DataComponents.RABBIT_VARIANT, ExactAdder.ofEnum("rabbit_variant"));
		register(DataComponents.PIG_VARIANT, new PigVariantHolderExactAdder());
		register(DataComponents.COW_VARIANT, new CowVariantHolderExactAdder());
		register(DataComponents.CHICKEN_VARIANT, new ChickenVariantEitherHolderExactAdder());
		register(DataComponents.FROG_VARIANT, new FrogVariantHolderExactAdder());
		register(DataComponents.HORSE_VARIANT, ExactAdder.ofEnum("horse_variant"));
		register(DataComponents.PAINTING_VARIANT, new PaintingVariantHolderExactAdder());
		register(DataComponents.LLAMA_VARIANT, ExactAdder.ofEnum("llama_variant"));
		register(DataComponents.AXOLOTL_VARIANT, ExactAdder.ofEnum("axolotl_variant"));
		register(DataComponents.CAT_VARIANT, new CatVariantHolderExactAdder());
		register(DataComponents.CAT_COLLAR, ExactAdder.ofEnum("cat_collar"));
		register(DataComponents.SHEEP_COLOR, ExactAdder.ofEnum("sheep_color"));
		register(DataComponents.SHULKER_COLOR, ExactAdder.ofEnum("shulker_color"));
		//?} else {
		/*register(DataComponents.LOCK, ExactAdder.simple((lock, builder) ->
				builder.translate(key("lock"), Styler::condition, Styler.value(Component.literal(lock.key())))
		));
		register(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, ExactAdder.simple((amplifier, builder) ->
				builder.translate(key("ominous_bottle_amplifier"), Styler::condition, Styler.number(amplifier))
		));
		register(DataComponents.INSTRUMENT, new InstrumentHolderExactAdder());
		register(DataComponents.RECIPES, new IdListExactAdder());
		register(DataComponents.BEES, new BeehiveBlockEntityOccupantListExactAdder());
		register(DataComponents.FIRE_RESISTANT, ExactAdder.simple((unit, builder) ->
				builder.translate(key("fire_resistant"), Styler::condition)
		));
		*///?}
	}

	private static <T> void register(DataComponentType<T> type, ExactAdder<T> adder) {
		if (EXACT_ADDERS.containsKey(type)) {
			throw new IllegalStateException("Duplicate data component type: " + type);
		}
		EXACT_ADDERS.put(type, adder);
	}

	@SuppressWarnings("unchecked")
	public static void addToBuilder(ClientLevel level, DataComponentType<?> type, Object object, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		ExactAdder<Object> adder = (ExactAdder<Object>) EXACT_ADDERS.getOrDefault(type, ExactAdder.UNKNOWN.apply(type));
		adder.addToBuilder(level, object, state, builder);
	}

	public static String key(String key) {
		return PredicateTooltip.key("data.exact.%s".formatted(key));
	}
}
