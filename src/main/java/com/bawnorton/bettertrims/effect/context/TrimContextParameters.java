package com.bawnorton.bettertrims.effect.context;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;

public class TrimContextParameters {
    public static final TrimContextParameter<DamageSource> DAMAGE_SOURCE = of("damage_source");
    public static final TrimContextParameter<Float> DAMAGE_AMOUNT = of("damage_amount");
    public static final TrimContextParameter<StatusEffect> STATUS_EFFECT = of("status_effect");
    public static final TrimContextParameter<BlockState> MINED_BLOCK = of("mined_block");
    public static final TrimContextParameter<Integer> ENCHANTMENT_LEVEL = of("enchantment_level");
    public static final TrimContextParameter<Integer> COUNT = of("count");
    public static final TrimContextParameter<Float> MOVEMENT_SPEED = of("movement_speed");
    public static final TrimContextParameter<Float> ATTACK_DAMAGE = of("attack_damage");
    public static final TrimContextParameter<Float> ATTACK_SPEED = of("attack_speed");
    public static final TrimContextParameter<Integer> ATTACK_COOLDOWN = of("attack_cooldown");

    private static <T> TrimContextParameter<T> of(String name) {
        return new TrimContextParameter<>(BetterTrims.id(name));
    }
}
