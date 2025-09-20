package com.bawnorton.bettertrims.property.ability.runner;

import com.bawnorton.bettertrims.property.Matcher;
import com.bawnorton.bettertrims.property.context.ContextChecker;
import com.bawnorton.bettertrims.property.ability.type.TrimToggleAbility;
import com.bawnorton.bettertrims.property.context.TrimContexts;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public final class TrimToggleAbilityRunner<T extends TrimToggleAbility> implements ContextChecker {
    private final T ability;
    private final Predicate<LootContext> conditionChecker;
    private final Matcher matcher;

    private final Set<UUID> active = new HashSet<>();

    public TrimToggleAbilityRunner(T ability, Predicate<LootContext> conditionChecker, Matcher matcher) {
        this.ability = ability;
        this.conditionChecker = conditionChecker;
        this.matcher = matcher;
    }

    public void runStart(ServerLevel level, LivingEntity wearer) {
        Map<EquipmentSlot, ItemStack> matchingStacks = matcher.getMatchingStacks(wearer);
        TrimmedItems items = TrimmedItems.of(matchingStacks, wearer);
        if (items.size() >= matcher.minCount() && checkRequirement(TrimContexts.equipment(level, items))) {
            ability.start(level, wearer, items);
            active.add(wearer.getUUID());
        }
    }

    public void runStop(ServerLevel level, LivingEntity wearer, ItemStack leftStack, EquipmentSlot leftSlot) {
        Map<EquipmentSlot, ItemStack> matchingStacks = matcher.getMatchingStacks(wearer);
        int count = matchingStacks.size();
        boolean wasContributing = matcher.matches(wearer, leftStack, leftSlot);
        int contributingCount = wasContributing ? count + 1 : count;
        if (contributingCount <= 0) return;

        TrimmedItems items = TrimmedItems.of(matchingStacks, wearer);
        ability.stop(level, wearer, items);
        active.remove(wearer.getUUID());
        if (count >= matcher.minCount() && checkRequirement(TrimContexts.equipment(level, items))) {
            ability.start(level, wearer, items);
            active.add(wearer.getUUID());
        }
    }

    public void update(ServerLevel level, LivingEntity wearer) {
        Map<EquipmentSlot, ItemStack> matchingStacks = matcher.getMatchingStacks(wearer);
        TrimmedItems items = TrimmedItems.of(matchingStacks, wearer);
        boolean passes = matchingStacks.size() >= matcher.minCount() && checkRequirement(TrimContexts.equipment(level, items));
        boolean active = this.active.contains(wearer.getUUID());
        if (active && !passes) {
            ability.stop(level, wearer, items);
            this.active.remove(wearer.getUUID());
        } else if (!active && passes) {
            ability.start(level, wearer, items);
            this.active.add(wearer.getUUID());
        }
    }

    @Override
    public Predicate<LootContext> conditionChecker() {
        return conditionChecker;
    }
}
