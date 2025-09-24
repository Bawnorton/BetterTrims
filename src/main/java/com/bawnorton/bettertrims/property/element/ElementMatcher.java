package com.bawnorton.bettertrims.property.element;

import com.bawnorton.bettertrims.property.Matcher;
import com.bawnorton.bettertrims.property.context.ContextChecker;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.google.common.base.Predicates;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.Map;
import java.util.function.Predicate;

public class ElementMatcher<T extends TrimElement> implements ContextChecker {
    private final Matcher matcher;
    private final T element;

    public ElementMatcher(Matcher matcher, T element) {
        this.matcher = matcher;
        this.element = element;
    }

    public T getElement() {
        return element;
    }

    @Override
    public Predicate<LootContext> conditionChecker() {
        return Predicates.alwaysTrue();
    }

    public boolean matches(ItemStack stack, LootContext context) {
        return matcher.matches(stack) && checkRequirement(context);
    }

    public boolean matches(LivingEntity wearer, LootContext context) {
        return matcher.getMatchingStacks(wearer).size() >= matcher.minCount() && checkRequirement(context);
    }

    public TrimmedItems getMatchingItems(LivingEntity wearer) {
        Map<EquipmentSlot, ItemStack> matchingStacks = matcher.getMatchingStacks(wearer);
        return TrimmedItems.of(matchingStacks, wearer);
    }
}
