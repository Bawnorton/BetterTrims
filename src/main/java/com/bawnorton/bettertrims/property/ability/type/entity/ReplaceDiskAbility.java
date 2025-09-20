package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;

public record ReplaceDiskAbility(
    CountBasedValue radius,
    CountBasedValue height,
    Vec3i offset,
    Optional<BlockPredicate> predicate,
    BlockStateProvider blockState,
    Optional<Holder<GameEvent>> triggerGameEvent
) implements TrimEntityAbility {
    public static final MapCodec<ReplaceDiskAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        CountBasedValue.CODEC.fieldOf("radius").forGetter(ReplaceDiskAbility::radius),
        CountBasedValue.CODEC.fieldOf("height").forGetter(ReplaceDiskAbility::height),
        Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(ReplaceDiskAbility::offset),
        BlockPredicate.CODEC.optionalFieldOf("predicate").forGetter(ReplaceDiskAbility::predicate),
        BlockStateProvider.CODEC.fieldOf("block_state").forGetter(ReplaceDiskAbility::blockState),
        GameEvent.CODEC.optionalFieldOf("trigger_game_event").forGetter(ReplaceDiskAbility::triggerGameEvent)
    ).apply(instance, ReplaceDiskAbility::new));
    
    @Override
    public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
        int count = items.size();
        BlockPos blockPos = BlockPos.containing(origin).offset(this.offset);
        RandomSource randomSource = wearer.getRandom();
        int radius = (int) this.radius.calculate(count);
        int hieght = (int) this.height.calculate(count);

        for (BlockPos pos : BlockPos.betweenClosed(blockPos.offset(-radius, 0, -radius), blockPos.offset(radius, Math.min(hieght - 1, 0), radius))) {
            if (pos.distToCenterSqr(origin.x(), pos.getY() + 0.5, origin.z()) < Mth.square(radius)
                && this.predicate.map(blockPredicate -> blockPredicate.test(level, pos)).orElse(true)
                && level.setBlockAndUpdate(pos, this.blockState.getState(randomSource, pos))) {
                this.triggerGameEvent.ifPresent(holder -> level.gameEvent(wearer, holder, pos));
            }
        }
    }

    @Override
    public MapCodec<? extends TrimEntityAbility> codec() {
        return CODEC;
    }
}
