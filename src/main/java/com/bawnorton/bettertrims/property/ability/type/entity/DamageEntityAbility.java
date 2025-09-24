package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.client.tooltip.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public record DamageEntityAbility(CountBasedValue minDamage, CountBasedValue maxDamage, Holder<DamageType> damageType) implements TrimEntityAbility {
    public static final MapCodec<DamageEntityAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        CountBasedValue.CODEC.fieldOf("min_damage").forGetter(DamageEntityAbility::minDamage),
        CountBasedValue.CODEC.fieldOf("max_damage").forGetter(DamageEntityAbility::maxDamage),
        DamageType.CODEC.fieldOf("damage_type").forGetter(DamageEntityAbility::damageType)
    ).apply(instance, DamageEntityAbility::new));

    @Override
    public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
        int count = items.size();
        float damage = Mth.randomBetween(target.getRandom(), minDamage.calculate(count), maxDamage.calculate(count));
        //? if 1.21.8 {
        target.hurtServer(level, new DamageSource(damageType, wearer), damage);
        //?} elif 1.21.1 {
        /*target.hurt(new DamageSource(damageType, wearer), damage);
         *///?}
    }

    @Override
    public @Nullable ClientTooltipComponent getTooltip(ClientLevel level, boolean includeCount) {
        RegistryAccess registryAccess = level.registryAccess();
        Registry<DamageType> registry = registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE);
        ResourceLocation type = damageType.unwrap().map(ResourceKey::location, registry::getKey);

        List<Float> minValues = this.minDamage().getValues(4);
        List<Float> maxValues = this.maxDamage().getValues(4);
        List<Component> components = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            components.add(Styler.trim(Component.literal("[%s]".formatted(i + 1)))
                .append(": ")
                .append(Styler.number(Component.literal("%.1f - %.1f".formatted(minValues.get(i), maxValues.get(i))))));
        }

        return CompositeContainerComponent.builder()
            .translate("bettertrims.tooltip.ability.damage_entity.deals", Styler::positive)
            .cycle(builder -> components.forEach(builder::textComponent))
            .literal(type.toString(), Styler::name)
            .translate("bettertrims.tooltip.ability.damage_entity.damage", Styler::positive)
            .spaced()
            .build();
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
