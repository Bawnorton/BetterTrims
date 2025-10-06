package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record SummonEntityAbility(EntityType<?> entityType) implements TrimEntityAbility {
	public static final MapCodec<SummonEntityAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(SummonEntityAbility::entityType)
	).apply(instance, SummonEntityAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		BlockPos blockPos = BlockPos.containing(origin);
		if (!Level.isInSpawnableBounds(blockPos)) return;

		Entity entity = entityType.spawn(level, blockPos, EntitySpawnReason.TRIGGERED);
		if (entity == null) return;

		if (entity instanceof LightningBolt lightningBolt && wearer instanceof ServerPlayer player) {
			lightningBolt.setCause(player);
		}
		entity.snapTo(origin.x(), origin.y(), origin.z(), entity.getYRot(), entity.getXRot());
	}

	@Override
	public @Nullable ClientTooltipComponent getTooltip(ClientLevel level, boolean includeCount) {
		return CompositeContainerComponent.builder()
				.translate(
						"bettertrims.tooltip.ability.summon_entity",
						Styler::positive,
						Styler.name(entityType.getDescription().copy())
				)
				.build();
	}

	@Override
	public MapCodec<? extends TrimEntityAbility> codec() {
		return CODEC;
	}
}
