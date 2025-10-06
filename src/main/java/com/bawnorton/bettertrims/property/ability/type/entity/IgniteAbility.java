package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.client.tooltip.element.TrimElementTooltipProvider;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record IgniteAbility(CountBasedValue duration) implements TrimEntityAbility {
	public static final MapCodec<IgniteAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			CountBasedValue.CODEC.fieldOf("duration").forGetter(IgniteAbility::duration)
	).apply(instance, IgniteAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		target.igniteForSeconds(duration.calculate(items.size()));
	}

	@Override
	public boolean usesCount() {
		return true;
	}

	@Override
	public MapCodec<? extends TrimEntityAbility> codec() {
		return CODEC;
	}

	public static class TooltipProvider implements TrimElementTooltipProvider<IgniteAbility> {
		@Nullable
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, IgniteAbility element, boolean includeCount) {
			return CompositeContainerComponent.builder()
					.translate("bettertrims.tooltip.ability.ignite.ignite_for", Styler::positive)
					.cycle(builder -> element.duration().getValueComponents(4, includeCount).forEach(builder::textComponent))
					.translate("bettertrims.tooltip.ability.ignite.seconds", Styler::positive)
					.spaced()
					.build();
		}
	}
}
