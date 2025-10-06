package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.version.VRegistry;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.effects.SpawnParticlesEffect;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record SpawnParticlesAbility(
		ParticleOptions particle,
		SpawnParticlesEffect.PositionSource horizontalPosition,
		SpawnParticlesEffect.PositionSource verticalPosition,
		SpawnParticlesEffect.VelocitySource horizontalVelocity,
		SpawnParticlesEffect.VelocitySource verticalVelocity,
		FloatProvider speed
) implements TrimEntityAbility {
	public static final MapCodec<SpawnParticlesAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ParticleTypes.CODEC.fieldOf("particle").forGetter(SpawnParticlesAbility::particle),
			SpawnParticlesEffect.PositionSource.CODEC.fieldOf("horizontal_position").forGetter(SpawnParticlesAbility::horizontalPosition),
			SpawnParticlesEffect.PositionSource.CODEC.fieldOf("vertical_position").forGetter(SpawnParticlesAbility::verticalPosition),
			SpawnParticlesEffect.VelocitySource.CODEC.fieldOf("horizontal_velocity").forGetter(SpawnParticlesAbility::horizontalVelocity),
			SpawnParticlesEffect.VelocitySource.CODEC.fieldOf("vertical_velocity").forGetter(SpawnParticlesAbility::verticalVelocity),
			FloatProvider.CODEC.optionalFieldOf("speed", ConstantFloat.ZERO).forGetter(SpawnParticlesAbility::speed)
	).apply(instance, SpawnParticlesAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		RandomSource random = target.getRandom();
		Vec3 targetVelocity = target.getKnownMovement();
		float width = target.getBbWidth();
		float height = target.getBbHeight();
		level.sendParticles(
				this.particle,
				this.horizontalPosition.getCoordinate(origin.x(), origin.x(), width, random),
				this.verticalPosition.getCoordinate(origin.y(), origin.y() + height / 2.0F, height, random),
				this.horizontalPosition.getCoordinate(origin.z(), origin.z(), width, random),
				0,
				this.horizontalVelocity.getVelocity(targetVelocity.x(), random),
				this.verticalVelocity.getVelocity(targetVelocity.y(), random),
				this.horizontalVelocity.getVelocity(targetVelocity.z(), random),
				this.speed.sample(random)
		);
	}

	@Override
	public @Nullable ClientTooltipComponent getTooltip(ClientLevel level, boolean includeCount) {
		Registry<ParticleType<?>> registry = VRegistry.get(level, Registries.PARTICLE_TYPE);
		ResourceLocation particleType = registry.getKey(particle.getType());
		if (particleType == null) return null;

		Component particleName = Styler.name(Component.literal(particleType.toString()));
		return CompositeContainerComponent.builder()
				.translate("bettertrims.tooltip.ability.spawn_particles.spawns", Styler::positive)
				.textComponent(particleName)
				.translate("bettertrims.tooltip.ability.spawn_particles.particles", Styler::positive)
				.spaced()
				.build();
	}

	@Override
	public MapCodec<? extends TrimEntityAbility> codec() {
		return CODEC;
	}
}
