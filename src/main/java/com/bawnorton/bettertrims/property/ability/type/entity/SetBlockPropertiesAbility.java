package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record SetBlockPropertiesAbility(
		BlockItemStateProperties properties,
		Vec3i offset,
		Optional<Holder<GameEvent>> triggerGameEvent,
		String propertiesTranslationKey,
		String offsetTranslationKey
) implements TrimEntityAbility {
	public static final MapCodec<SetBlockPropertiesAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			BlockItemStateProperties.CODEC.fieldOf("properties").forGetter(SetBlockPropertiesAbility::properties),
			Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(SetBlockPropertiesAbility::offset),
			GameEvent.CODEC.optionalFieldOf("trigger_game_event").forGetter(SetBlockPropertiesAbility::triggerGameEvent),
			Codec.STRING.fieldOf("properties_translation_key").forGetter(SetBlockPropertiesAbility::propertiesTranslationKey),
			Codec.STRING.fieldOf("offset_translation_key").forGetter(SetBlockPropertiesAbility::offsetTranslationKey)
	).apply(instance, SetBlockPropertiesAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		BlockPos blockPos = BlockPos.containing(origin).offset(this.offset);
		BlockState currentState = level.getBlockState(blockPos);
		BlockState newState = this.properties.apply(currentState);
		if (currentState != newState && level.setBlock(blockPos, newState, Block.UPDATE_ALL)) {
			this.triggerGameEvent.ifPresent(holder -> level.gameEvent(wearer, holder, blockPos));
		}
	}

	@Override
	public @Nullable ClientTooltipComponent getTooltip(ClientLevel level, boolean includeCount) {
		Component properties = Styler.property(Component.translatable(propertiesTranslationKey));
		Component offset = Styler.positive(Component.translatable(offsetTranslationKey));
		return CompositeContainerComponent.builder()
				.translate("bettertrims.tooltip.ability.set_block_properties.set", Styler::positive)
				.textComponent(properties)
				.textComponent(offset)
				.spaced()
				.build();
	}

	@Override
	public MapCodec<? extends TrimEntityAbility> codec() {
		return CODEC;
	}
}
