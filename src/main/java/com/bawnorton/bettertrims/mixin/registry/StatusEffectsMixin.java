package com.bawnorton.bettertrims.mixin.registry;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.mixin.accessor.StatusEffectAccessor;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.bettertrims.registry.content.TrimStatusEffects;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Colors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import java.util.Random;
import java.util.UUID;

@Mixin(StatusEffects.class)
public abstract class StatusEffectsMixin {
    static {
        TrimStatusEffects.DAMPENED = bettertrims$register(
                "dampened",
                StatusEffectAccessor.createStatusEffect(StatusEffectCategory.HARMFUL, 0xFF0B5365)
        );
        TrimStatusEffects.FEEL_THE_RUSH = bettertrims$register(
                "feel_the_rush",
                StatusEffectAccessor.createStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFF0000FF)
                        .addAttributeModifier(
                                //? if >=1.21 {
                                /*EntityAttributes.PLAYER_BLOCK_BREAK_SPEED,
                                BetterTrims.id("effect.feel_the_rush"),
                                AttributeSettings.MinersRush.bonusMineSpeed,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                *///?} else {
                                TrimEntityAttributes.PLAYER_BLOCK_BREAK_SPEED,
                                bettertrims$randomUuid(BetterTrims.sid("effect.feel_the_rush")),
                                AttributeSettings.MinersRush.bonusMineSpeed,
                                EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                                //?}
                        )
        );
    }

    //? if >=1.21 {
    /*@Unique
    private static RegistryEntry<StatusEffect> bettertrims$register(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, BetterTrims.id(id), statusEffect);
    }
    *///?} else {
    @Unique
    private static StatusEffect bettertrims$register(String id, StatusEffect statusEffect) {
        return Registry.register(Registries.STATUS_EFFECT, BetterTrims.id(id), statusEffect);
    }

    @Unique
    private static String bettertrims$randomUuid(String seed) {
        Random random = new Random(seed.hashCode());
        UUID uuid = new UUID(random.nextLong(), random.nextLong());
        return uuid.toString();
    }
    //?}
}
