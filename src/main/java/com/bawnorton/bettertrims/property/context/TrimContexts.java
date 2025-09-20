package com.bawnorton.bettertrims.property.context;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;

public final class TrimContexts {
    public static LootContext damage(ServerLevel level, TrimmedItems items, Entity entity, DamageSource damageSource, ItemStack damagingItem) {
        return new LootContext.Builder(new LootParams.Builder(level)
            .withParameter(LootContextParams.THIS_ENTITY, entity)
            .withParameter(TrimContextParams.ITEMS, items)
            .withParameter(LootContextParams.ORIGIN, entity.position())
            .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
            .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity())
            .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity())
            .withOptionalParameter(LootContextParams.TOOL, damagingItem)
            .create(TrimContextParamSets.TRIM_DAMAGE)
        ).create(Optional.empty());
    }

    public static LootContext damage(ServerLevel level, TrimmedItems items, Entity entity, DamageSource damageSource) {
        ItemStack damagingItem = null;
        if(damageSource.getDirectEntity() instanceof AbstractArrow abstractArrow) {
            damagingItem = abstractArrow.getPickupItemStackOrigin();
        }
        return damage(level, items, entity, damageSource, damagingItem);
    }

    public static LootContext damageItem(ServerLevel level, ItemStack item, @Nullable ItemEntity entity, DamageSource damageSource) {
        return new LootContext.Builder(new LootParams.Builder(level)
            .withParameter(LootContextParams.TOOL, item)
            .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
            .withOptionalParameter(LootContextParams.ORIGIN, entity == null ? null : entity.position())
            .withOptionalParameter(LootContextParams.THIS_ENTITY, entity)
            .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity())
            .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity())
            .create(TrimContextParamSets.TRIM_ITEM_DAMAGE)
        ).create(Optional.empty());
    }

    public static LootContext equipment(ServerLevel level, TrimmedItems items, @Nullable ItemStack heldItem) {
        return new LootContext.Builder(new LootParams.Builder(level)
            .withParameter(LootContextParams.THIS_ENTITY, items.owner())
            .withParameter(LootContextParams.ORIGIN, items.owner().position())
            .withParameter(TrimContextParams.ITEMS, items)
            .withOptionalParameter(LootContextParams.TOOL, heldItem)
            .create(TrimContextParamSets.TRIM_EQUIPMENT)
        ).create(Optional.empty());
    }

    public static LootContext equipment(ServerLevel level, TrimmedItems items) {
        return equipment(level, items, null);
    }

    public static LootContext entity(ServerLevel level, TrimmedItems items, Entity entity, Vec3 origin) {
        return new LootContext.Builder(new LootParams.Builder(level)
            .withParameter(LootContextParams.THIS_ENTITY, entity)
            .withParameter(TrimContextParams.ITEMS, items)
            .withParameter(LootContextParams.ORIGIN, origin)
            .create(TrimContextParamSets.TRIM_ENTITY)
        ).create(Optional.empty());
    }

    public static LootContext blockHitWithHeld(ServerLevel level, TrimmedItems items, Entity entity, Vec3 origin, BlockState state, ItemStack heldItem) {
        return new LootContext.Builder(new LootParams.Builder(level)
            .withParameter(LootContextParams.THIS_ENTITY, entity)
            .withParameter(TrimContextParams.ITEMS, items)
            .withParameter(LootContextParams.ORIGIN, origin)
            .withParameter(LootContextParams.BLOCK_STATE, state)
            .withParameter(LootContextParams.TOOL, heldItem)
            .create(TrimContextParamSets.HIT_BLOCK_WITH_HELD_ITEM)
        ).create(Optional.empty());
    }
}
