package com.bawnorton.bettertrims.extend;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.TrimEffect;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import java.util.List;

public interface LivingEntityExtender {
    int bettertrims$applyCelestialToAttackCooldown(int original);

    boolean bettertrims$didAvoidDamage();

    void bettertrims$setAvoidedDamage(boolean avoidDamage);

    default boolean dodge(double chance) {
        if(!(this instanceof LivingEntity self)) return false;

        if(BetterTrims.PROBABILITIES.passes(chance)) {
            ChorusFruitItem chorusFruit = (ChorusFruitItem) Items.CHORUS_FRUIT;
            ItemStack chorusStack = chorusFruit.getDefaultStack();
            chorusStack.remove(DataComponentTypes.FOOD);
            chorusFruit.finishUsing(chorusStack, self.getWorld(), self);
            if((Object) this instanceof PlayerEntity player) {
                player.getItemCooldownManager().remove(chorusFruit);
            }
            bettertrims$setAvoidedDamage(true);
        } else {
            bettertrims$setAvoidedDamage(false);
        }
        return bettertrims$didAvoidDamage();
    }

    List<RegistryEntry<ArmorTrimMaterial>> bettertrims$getWornMaterials();

    default boolean bettertrims$isWearing(TrimEffect effect) {
        return bettertrims$getWornMaterials().stream().anyMatch(effect::matchesMaterial);
    }
}
