package com.bawnorton.bettertrims.property.ability.type.toggle;

import com.bawnorton.bettertrims.client.tooltip.vanilla.element.TrimElementTooltipProvider;
import com.bawnorton.bettertrims.property.ability.type.TrimToggleAbility;
import com.bawnorton.bettertrims.property.ability.type.entity.ApplyMobEffectAbility;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public record ToggleMobEffectAbility(Holder<MobEffect> effect, CountBasedValue amplifier, boolean visible) implements TrimToggleAbility {
	public static final MapCodec<ToggleMobEffectAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			MobEffect.CODEC.fieldOf("effect").forGetter(ToggleMobEffectAbility::effect),
			CountBasedValue.CODEC.fieldOf("amplifier").forGetter(ToggleMobEffectAbility::amplifier),
			Codec.BOOL.optionalFieldOf("visible", true).forGetter(ToggleMobEffectAbility::visible)
	).apply(instance, ToggleMobEffectAbility::new));

	public ToggleMobEffectAbility(Holder<MobEffect> effect, CountBasedValue amplifier) {
		this(effect, amplifier, true);
	}

	private MobEffectInstance getMobEffectInstance(int count) {
		return new MobEffectInstance(effect, -1, (int) amplifier.calculate(count), false, visible, true);
	}

	@Override
	public void start(ServerLevel level, LivingEntity wearer, TrimmedItems items) {
		wearer.addEffect(getMobEffectInstance(items.size()));
	}

	@Override
	public void stop(ServerLevel level, LivingEntity wearer, TrimmedItems items) {
		wearer.removeEffect(effect);
	}

	@Override
	public boolean usesCount() {
		return true;
	}

	@Override
	public MapCodec<? extends TrimToggleAbility> codec() {
		return CODEC;
	}

	public static class TooltipProvider implements TrimElementTooltipProvider<ToggleMobEffectAbility> {
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, ToggleMobEffectAbility element, boolean includeCount) {
			return ApplyMobEffectAbility.TooltipProvider.getEffectTooltip(level,
					includeCount,
					element.effect(),
					element.amplifier(),
					(builder, styler) -> builder.build());
		}
	}
}