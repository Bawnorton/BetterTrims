package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;

public record ReplaceBlockAbility(
    Vec3i offset,
    Optional<BlockPredicate> predicate,
    BlockStateProvider blockState,
    Optional<Holder<GameEvent>> triggerGameEvent
) implements TrimEntityAbility {
    public static final MapCodec<ReplaceBlockAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(ReplaceBlockAbility::offset),
        BlockPredicate.CODEC.optionalFieldOf("predicate").forGetter(ReplaceBlockAbility::predicate),
        BlockStateProvider.CODEC.fieldOf("block_state").forGetter(ReplaceBlockAbility::blockState),
        GameEvent.CODEC.optionalFieldOf("trigger_game_event").forGetter(ReplaceBlockAbility::triggerGameEvent)
    ).apply(instance, ReplaceBlockAbility::new));
    
    @Override
    public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
        BlockPos blockPos = BlockPos.containing(origin).offset(this.offset);
        if (this.predicate.map(blockPredicate -> blockPredicate.test(level, blockPos)).orElse(true) && level.setBlockAndUpdate(blockPos, this.blockState.getState(wearer.getRandom(), blockPos))) {
            this.triggerGameEvent.ifPresent(holder -> level.gameEvent(wearer, holder, blockPos));
        }
    }

    @Override
    public MapCodec<? extends TrimEntityAbility> codec() {
        return null;
    }
}
