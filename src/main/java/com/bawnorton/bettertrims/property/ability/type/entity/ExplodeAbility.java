package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.element.TrimElementTooltipProvider;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ExplodeAbility(
		boolean attributeToWearer,
		Optional<Holder<DamageType>> damageType,
		Optional<CountBasedValue> knockbackMultiplier,
		Optional<HolderSet<Block>> immuneBlocks,
		Vec3 offset,
		CountBasedValue radius,
		boolean createFire,
		Level.ExplosionInteraction blockInteraction,
		ParticleOptions smallParticle,
		ParticleOptions largeParticle,
		Holder<SoundEvent> sound
) implements TrimEntityAbility {
	public static final MapCodec<ExplodeAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.BOOL.optionalFieldOf("attribute_to_wearer", false).forGetter(ExplodeAbility::attributeToWearer),
			DamageType.CODEC.optionalFieldOf("damage_type").forGetter(ExplodeAbility::damageType),
			CountBasedValue.CODEC.optionalFieldOf("knockback_multiplier").forGetter(ExplodeAbility::knockbackMultiplier),
			RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("immune_blocks").forGetter(ExplodeAbility::immuneBlocks),
			Vec3.CODEC.optionalFieldOf("offset", Vec3.ZERO).forGetter(ExplodeAbility::offset),
			CountBasedValue.CODEC.fieldOf("radius").forGetter(ExplodeAbility::radius),
			Codec.BOOL.optionalFieldOf("create_fire", false).forGetter(ExplodeAbility::createFire),
			Level.ExplosionInteraction.CODEC.fieldOf("block_interaction").forGetter(ExplodeAbility::blockInteraction),
			ParticleTypes.CODEC.fieldOf("small_particle").forGetter(ExplodeAbility::smallParticle),
			ParticleTypes.CODEC.fieldOf("large_particle").forGetter(ExplodeAbility::largeParticle),
			SoundEvent.CODEC.fieldOf("sound").forGetter(ExplodeAbility::sound)
	).apply(instance, ExplodeAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		int count = items.size();
		level.explode(
				attributeToWearer ? wearer : null,
				getDamageSource(wearer, origin),
				new SimpleExplosionDamageCalculator(
						blockInteraction != Level.ExplosionInteraction.NONE,
						damageType.isPresent(),
						knockbackMultiplier.map(countBasedValue -> countBasedValue.calculate(count)),
						immuneBlocks
				),
				origin.x(),
				origin.y(),
				origin.z(),
				Math.max(radius.calculate(count), 0F),
				createFire,
				blockInteraction,
				smallParticle,
				largeParticle,
				sound
		);
	}

	private DamageSource getDamageSource(Entity entity, Vec3 pos) {
		return this.damageType.map(damageTypeHolder -> this.attributeToWearer ? new DamageSource(damageTypeHolder, entity) : new DamageSource(damageTypeHolder, pos)).orElse(null);
	}

	@Override
	public boolean usesCount() {
		return true;
	}

	@Override
	public MapCodec<? extends TrimEntityAbility> codec() {
		return CODEC;
	}

	public static class TooltipProvider implements TrimElementTooltipProvider<ExplodeAbility> {
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, ExplodeAbility element, boolean includeCount) {
			return CompositeContainerComponent.builder()
					.translate("bettertrims.tooltip.ability.explode.explodes_with_radius", style -> style.withColor(ChatFormatting.DARK_RED))
					.cycle(builder -> element.radius().getValueComponents(4, includeCount).forEach(builder::textComponent))
					.spaced()
					.build();
		}
	}
}
