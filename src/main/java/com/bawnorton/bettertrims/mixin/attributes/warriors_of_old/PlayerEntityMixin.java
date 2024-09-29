package com.bawnorton.bettertrims.mixin.attributes.warriors_of_old;

import com.bawnorton.bettertrims.entity.AncientSkeletonEntity;
import com.bawnorton.bettertrims.registry.content.TrimEntities;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Unique
    private final List<AncientSkeletonEntity> bettertrims$ancientSkeletons = new ArrayList<>();

    public PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    private void applyWarriorsOfOld(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        int level = (int) getAttributeValue(TrimEntityAttributes.WARRIORS_OF_OLD);
        if(level <= 0) return;
        if(source.getAttacker() == null) return;

        bettertrims$ancientSkeletons.removeIf(Entity::isRemoved);
        if(bettertrims$ancientSkeletons.size() >= level) return;

        BlockPos.Mutable pos = new BlockPos.Mutable(getX(), getY(), getZ());
        int checks = 0;
        boolean canSpawn;
        do {
            pos.set(getX() + random.nextBetween(-3, 3), getY() + random.nextBetween(-3, 3), getZ() + random.nextBetween(-3, 3));
            for(int i = 0; i < 4; i++) {
                if (!getWorld().getBlockState(pos.down()).isAir()) {
                    break;
                }
                pos.setY(pos.getY() - 1);
            }
            //? if >=1.21 {
            canSpawn = getWorld().isBlockSpaceEmpty(null, TrimEntities.ANCIENT_SKELETON.getDimensions().getBoxAt(pos.toCenterPos()));
            //?} else {
            /*canSpawn = true;
            for (VoxelShape voxelShape : getWorld().getBlockCollisions(null, TrimEntities.ANCIENT_SKELETON.getDimensions().getBoxAt(pos.toCenterPos()))) {
                if (!voxelShape.isEmpty()) {
                    canSpawn = false;
                    break;
                }
            }
            *///?}
            checks++;
        } while(!canSpawn || checks > 200);

        getWorld().syncWorldEvent(WorldEvents.SPAWNER_SPAWNS_MOB, pos, 0);

        TrimEntities.ANCIENT_SKELETON.spawn(
                (ServerWorld) getWorld(),
                //? if <1.21
                /*null,*/
                entity -> {
                    entity.setOwner((PlayerEntity) (Object) this);
                    bettertrims$ancientSkeletons.add(entity);
                },
                pos,
                SpawnReason.TRIGGERED,
                true,
                false
        );
    }
}
