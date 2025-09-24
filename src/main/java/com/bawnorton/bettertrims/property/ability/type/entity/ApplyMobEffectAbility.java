package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.ability.type.toggle.ToggleMobEffectAbility;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

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
            MobEffectInstance effectInstance = new MobEffectInstance(effect, duration, amplifier);
            living.addEffect(effectInstance);
        }
    }

    @Override
    public ClientTooltipComponent getTooltip(ClientLevel level, boolean includeCount) {
        return ToggleMobEffectAbility.getEffectTooltip(
            level,
            includeCount,
            effect,
            amplifier,
            (builder, styler) -> builder.translate("bettertrims.tooltip.ability.apply_mob_effect.for", styler)
                .cycle(durationCycler -> this.duration.getValueComponents(4, includeCount, f -> Component.literal("%.0f".formatted(f * 20))).forEach(durationCycler::textComponent))
                .translate("bettertrims.tooltip.ability.apply_mob_effect.seconds", styler)
                .build()
        );
    }

    @Override
    public boolean usesCount() {
        return true;
    }

    @Override
    public MapCodec<? extends TrimEntityAbility> codec() {
        return CODEC;
    }
}