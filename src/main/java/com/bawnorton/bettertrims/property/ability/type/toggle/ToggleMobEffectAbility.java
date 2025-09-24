package com.bawnorton.bettertrims.property.ability.type.toggle;

import com.bawnorton.bettertrims.client.tooltip.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.property.ability.type.TrimToggleAbility;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public record ToggleMobEffectAbility(Holder<MobEffect> effect, CountBasedValue amplifier) implements TrimToggleAbility {
    public static final MapCodec<ToggleMobEffectAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        MobEffect.CODEC.fieldOf("effect").forGetter(ToggleMobEffectAbility::effect),
        CountBasedValue.CODEC.fieldOf("amplifier").forGetter(ToggleMobEffectAbility::amplifier)
    ).apply(instance, ToggleMobEffectAbility::new));

    public static ClientTooltipComponent getEffectTooltip(
        ClientLevel level,
        boolean includeCount,
        Holder<MobEffect> effect,
        CountBasedValue amplifier,
        BiFunction<CompositeContainerComponent.Builder, UnaryOperator<Style>, CompositeContainerComponent> builder
    ) {
        RegistryAccess registryAccess = level.registryAccess();
        Registry<MobEffect> registry = registryAccess.lookupOrThrow(Registries.MOB_EFFECT);
        MobEffect mobEffect = effect.unwrap().map(registry::getValue, Function.identity());
        Component name = Styler.name(mobEffect.getDisplayName().copy());

        return builder.apply(
            CompositeContainerComponent.builder()
                .translate("bettertrims.tooltip.ability.apply_mob_effect.grants", Styler.sentiment(mobEffect.isBeneficial()))
                .textComponent(name)
                .cycle(amplifierCycler -> amplifier.getValueComponents(4, includeCount, f -> Component.translatable("enchantment.level.%s".formatted("%.0f".formatted(f + 1)))).forEach(amplifierCycler::textComponent))
                .spaced(),
            Styler.sentiment(mobEffect.isBeneficial())
        );
    }

    private MobEffectInstance getMobEffectInstance(int count) {
        return new MobEffectInstance(effect, -1, (int) amplifier.calculate(count));
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
    public @Nullable ClientTooltipComponent getTooltip(ClientLevel level, boolean includeCount) {
        return getEffectTooltip(level, includeCount, effect, amplifier, (builder, styler) -> builder.build());
    }

    @Override
    public boolean usesCount() {
        return true;
    }

    @Override
    public MapCodec<? extends TrimToggleAbility> codec() {
        return CODEC;
    }
}