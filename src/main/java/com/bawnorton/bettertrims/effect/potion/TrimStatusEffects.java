package com.bawnorton.bettertrims.effect.potion;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.mixin.accessor.StatusEffectAccessor;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Colors;

public class TrimStatusEffects {
    public static final RegistryEntry<StatusEffect> FEEL_THE_RUSH = register(
            "feel_the_rush",
            StatusEffectAccessor.createStatusEffect(StatusEffectCategory.BENEFICIAL, Colors.BLUE)
                    .addAttributeModifier(
                            EntityAttributes.PLAYER_BLOCK_BREAK_SPEED,
                            BetterTrims.id("effect.feel_the_rush"),
                            0.25f,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
    );

    private static RegistryEntry<StatusEffect> register(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, BetterTrims.id(id), statusEffect);
    }

    public static void init() {
        //no-op
    }
}
