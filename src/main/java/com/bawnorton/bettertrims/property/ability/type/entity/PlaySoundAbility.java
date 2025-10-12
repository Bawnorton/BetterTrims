package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.client.tooltip.element.TrimElementTooltipProvider;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.version.VRegistry;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public record PlaySoundAbility(Holder<SoundEvent> soundEvent, FloatProvider volume, FloatProvider pitch) implements TrimEntityAbility {
	public static final MapCodec<PlaySoundAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			SoundEvent.CODEC.fieldOf("sound").forGetter(PlaySoundAbility::soundEvent),
			FloatProvider.codec(1.0E-5F, 10.0F).fieldOf("volume").forGetter(PlaySoundAbility::volume),
			FloatProvider.codec(1.0E-5F, 2.0F).fieldOf("pitch").forGetter(PlaySoundAbility::pitch)
	).apply(instance, PlaySoundAbility::new));

	public static Component getSoundName(Registry<SoundEvent> registry, Holder<SoundEvent> soundHolder) {
		ResourceLocation sound = soundHolder.unwrap().map(ResourceKey::location, registry::getKey);
		if (sound == null) {
			SoundEvent soundEvent = soundHolder.unwrap().map(registry::getValueOrThrow, Function.identity());
			//? if >=1.21.8 {
			sound = soundEvent.location();
			//?} else {
			/*sound = soundEvent.getLocation();
			 *///?}
		}
		Component soundName = Component.literal(sound.toString());
		WeighedSoundEvents soundEvents = Minecraft.getInstance().getSoundManager().getSoundEvent(sound);
		if (soundEvents != null && soundEvents.getSubtitle() != null) {
			soundName = soundEvents.getSubtitle();
		}
		return soundName;
	}

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		RandomSource random = target.getRandom();
		if (target.isSilent()) return;

		level.playSound(
				null,
				origin.x(),
				origin.y(),
				origin.z(),
				soundEvent,
				target.getSoundSource(),
				volume.sample(random),
				pitch.sample(random)
		);
	}

	@Override
	public MapCodec<? extends TrimEntityAbility> codec() {
		return CODEC;
	}

	public static class TooltipProvider implements TrimElementTooltipProvider<PlaySoundAbility> {
		@Nullable
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, PlaySoundAbility element, boolean includeCount) {
			Registry<SoundEvent> registry = VRegistry.get(level, Registries.SOUND_EVENT);
			Component soundName = getSoundName(registry, element.soundEvent());

			return CompositeContainerComponent.builder()
					.translate("bettertrims.tooltip.ability.play_sound.play", Styler::positive)
					.textComponent(Styler.name(soundName.copy()))
					.spaced()
					.build();
		}
	}
}
