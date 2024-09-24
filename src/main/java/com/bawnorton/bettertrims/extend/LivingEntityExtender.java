package com.bawnorton.bettertrims.extend;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.TrimEffect;
import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import java.util.List;

//? if >=1.21 {
import net.minecraft.component.DataComponentTypes;
//?} else {
/*import com.bawnorton.bettertrims.mixin.accessor.ItemAccessor;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
*///?}

public interface LivingEntityExtender {
    int bettertrims$applyCelestialToAttackCooldown(int original);

    boolean bettertrims$didAvoidDamage();

    void bettertrims$setAvoidedDamage(boolean avoidDamage);

    default boolean dodge(double chance) {
        if(!(this instanceof LivingEntity self)) return false;

        if(BetterTrims.PROBABILITIES.passes(chance)) {
            ChorusFruitItem chorusFruit = (ChorusFruitItem) Items.CHORUS_FRUIT;
            ItemStack chorusStack = chorusFruit.getDefaultStack();
            //? if >=1.21 {
            chorusStack.remove(DataComponentTypes.FOOD);
            chorusFruit.finishUsing(chorusStack, self.getWorld(), self);
            //?} else {
            /*((ItemAccessor) chorusFruit).setFoodComponent(new FoodComponent.Builder().build());
            chorusFruit.finishUsing(chorusStack, self.getWorld(), self);
            ((ItemAccessor) chorusFruit).setFoodComponent(FoodComponents.CHORUS_FRUIT);
            *///?}
            if((Object) this instanceof PlayerEntity player) {
                player.getItemCooldownManager().remove(chorusFruit);
                if(player instanceof ServerPlayerEntity serverPlayer) {
                    TrimCriteria.DODGED.trigger(serverPlayer);
                }
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
