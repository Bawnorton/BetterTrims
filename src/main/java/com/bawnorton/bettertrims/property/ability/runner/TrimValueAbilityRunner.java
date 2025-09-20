package com.bawnorton.bettertrims.property.ability.runner;

import com.bawnorton.bettertrims.property.Matcher;
import com.bawnorton.bettertrims.property.context.ContextChecker;
import com.bawnorton.bettertrims.property.ability.type.TrimValueAbility;
import com.bawnorton.bettertrims.property.context.TrimContexts;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.Map;
import java.util.function.Predicate;

public record TrimValueAbilityRunner<T extends TrimValueAbility>(T ability, Predicate<LootContext> conditionChecker, Matcher matcher) implements ContextChecker {
    public float runEquipment(ServerLevel level, LivingEntity wearer, float value) {
        return runEquipment(level, wearer, null, value);
    }

    public float runEquipment(ServerLevel level, LivingEntity wearer, ItemStack stack, float value) {
        Map<EquipmentSlot, ItemStack> matchingStacks = matcher.getMatchingStacks(wearer);
        TrimmedItems items = TrimmedItems.of(matchingStacks, wearer);
        if (items.size() >= matcher.minCount() && checkRequirement(TrimContexts.equipment(level, items, stack))) {
            return ability.process(items.size(), wearer.getRandom(), value);
        }
        return value;
    }

    public float runDamage(ServerLevel level, LivingEntity wearer, DamageSource damageSource, float value) {
        Map<EquipmentSlot, ItemStack> matchingStacks = matcher.getMatchingStacks(wearer);
        TrimmedItems items = TrimmedItems.of(matchingStacks, wearer);
        if (items.size() >= matcher.minCount() && checkRequirement(TrimContexts.damage(level, items, wearer, damageSource))) {
            return ability.process(items.size(), wearer.getRandom(), value);
        }
        return value;
    }
}
