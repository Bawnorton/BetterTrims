package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.element.TrimElementTooltipProvider;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.bawnorton.bettertrims.version.VRegistry;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public record ApplyMobEffectAbility(Holder<MobEffect> effect, CountBasedValue amplifier, CountBasedValue duration) implements TrimEntityAbility {
	public static final MapCodec<ApplyMobEffectAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			MobEffect.CODEC.fieldOf("effect").forGetter(ApplyMobEffectAbility::effect),
			CountBasedValue.CODEC.fieldOf("amplifier").forGetter(ApplyMobEffectAbility::amplifier),
			CountBasedValue.CODEC.fieldOf("duration").forGetter(ApplyMobEffectAbility::duration)
	).apply(instance, ApplyMobEffectAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		if (target instanceof LivingEntity living) {
			int count = items.size();
			int amplifier = (int) this.amplifier.calculate(count);
			int duration = (int) this.duration.calculate(count) * 20;
			if (amplifier < 0 || duration <= 0) return;

			MobEffectInstance effectInstance = new MobEffectInstance(effect, duration, amplifier);
			living.addEffect(effectInstance);
		}
	}

	@Override
	public boolean usesCount() {
		return true;
	}

	@Override
	public MapCodec<? extends TrimEntityAbility> codec() {
		return CODEC;
	}

	public static class TooltipProvider implements TrimElementTooltipProvider<ApplyMobEffectAbility> {
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, ApplyMobEffectAbility element, boolean includeCount) {
			return getEffectTooltip(
					level,
					includeCount,
					element.effect(),
					element.amplifier(),
					(builder, styler) -> builder.translate("bettertrims.tooltip.ability.apply_mob_effect.for", styler)
							.cycle(durationCycler -> element.duration().getValueComponents(4, includeCount, f -> Component.literal("%.0f".formatted(f)), f -> f > 0)
									.forEach(durationCycler::textComponent))
							.translate("bettertrims.tooltip.ability.apply_mob_effect.seconds", styler)
							.build()
			);
		}

		public static ClientTooltipComponent getEffectTooltip(
				ClientLevel level,
				boolean includeCount,
				Holder<MobEffect> effect,
				CountBasedValue amplifier,
				BiFunction<CompositeContainerComponent.Builder, UnaryOperator<Style>, CompositeContainerComponent> builder
		) {
			Registry<MobEffect> registry = VRegistry.get(level, Registries.MOB_EFFECT);
			MobEffect mobEffect = effect.unwrap().map(registry::getValueOrThrow, Function.identity());
			Component name = Styler.name(mobEffect.getDisplayName().copy());

			List<Component> amplifierValues = amplifier.getValueComponents(
					4,
					includeCount,
					f -> Component.translatable("enchantment.level.%s".formatted("%.0f".formatted(f + 1))),
					f -> f >= 0
			);
			return builder.apply(
					CompositeContainerComponent.builder()
							.translate("bettertrims.tooltip.ability.apply_mob_effect.grants", Styler.sentiment(mobEffect.isBeneficial()))
							.textComponent(name)
							.cycle(amplifierCycler -> amplifierValues.forEach(amplifierCycler::textComponent))
							.spaced(),
					Styler.sentiment(mobEffect.isBeneficial())
			);
		}
	}
}