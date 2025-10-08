package com.bawnorton.bettertrims.property.ability.runner;

import com.bawnorton.bettertrims.property.Matcher;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.context.ContextChecker;
import com.bawnorton.bettertrims.property.context.TrimContexts;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Predicate;

public record TrimEntityAbilityRunner<T extends TrimEntityAbility>(T ability, Predicate<LootContext> conditionChecker, Matcher matcher) implements ContextChecker {
	public void runTick(ServerLevel level, LivingEntity wearer, Entity target, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		Map<EquipmentSlot, ItemStack> matchingStacks = matcher.getMatchingStacks(wearer);
		TrimmedItems items = TrimmedItems.of(matchingStacks, wearer);
		if (matchingStacks.size() >= matcher.minCount() && checkRequirement(TrimContexts.entity(level, items, target, origin))) {
			ability.apply(level, wearer, target, items, targetSlot, origin);
		}
	}

	public void runTick(ServerLevel level, LivingEntity wearer, Entity target, Vec3 origin) {
		runTick(level, wearer, target, null, origin);
	}

	public void runDamage(ServerLevel level, LivingEntity wearer, Entity target, DamageSource damageSource, ItemStack stack) {
		Map<EquipmentSlot, ItemStack> matchingStacks = matcher.getMatchingStacks(wearer);
		TrimmedItems items = TrimmedItems.of(matchingStacks, wearer);
		if (matchingStacks.size() >= matcher.minCount() && checkRequirement(TrimContexts.damage(level, items, target, damageSource, stack))) {
			ability.apply(level, wearer, target, items, null, target.position());
		}
	}

	public void runHitBlock(ServerLevel level, LivingEntity wearer, Entity target, Vec3 origin, BlockState state, ItemStack heldItem) {
		Map<EquipmentSlot, ItemStack> matchingStacks = matcher.getMatchingStacks(wearer);
		TrimmedItems items = TrimmedItems.of(matchingStacks, wearer);
		if (matchingStacks.size() >= matcher.minCount() && checkRequirement(TrimContexts.blockHitWithHeld(level, items, target, origin, state, heldItem))) {
			ability.apply(level, wearer, wearer, items, null, origin);
		}
	}
}
