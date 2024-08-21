package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.applicator.TrimEffectApplicator;
import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributes;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import com.bawnorton.bettertrims.util.FloodFill;
import com.bawnorton.bettertrims.util.Plane;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.joml.Vector3d;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class CopperTrimEffect extends TrimEffect<Void> {
    private final Object2ObjectMap<LivingEntity, Set<BlockPos>> electrifed = new Object2ObjectOpenHashMap<>();

    public CopperTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<RegistryEntry<EntityAttribute>> adder) {
        adder.accept(TrimEntityAttributes.ELECTRIFYING);
    }

    @Override
    public TrimEffectApplicator<Void> getApplicator() {
        return (context) -> {
            LivingEntity entity = context.getEntity();
            World world = entity.getWorld();

            Vec3d pos = entity.getPos();
            BlockPos blockPos = BlockPos.ofFloored(pos);
            if(!world.isWater(blockPos)) return null;

            double electrifying = entity.getAttributeValue(TrimEntityAttributes.ELECTRIFYING);
            double maxDist = electrifying * 2;

            Predicate<Vec3d> isWall = vec3d -> {
                BlockPos wallPos = BlockPos.ofFloored(vec3d);
                BlockState state = world.getBlockState(wallPos);

                FluidState fluidState = state.getFluidState();
                boolean water = fluidState.isIn(FluidTags.WATER);
                boolean waterlogged = water && state.getOrEmpty(Properties.WATERLOGGED).orElse(false);
                if(waterlogged) {
                    return !wallPos.equals(BlockPos.ofFloored(pos));
                }

                return !water;
            };

            if (!world.isClient()) {
                Set<BlockPos> toElectrify = electrifed.computeIfAbsent(entity, k -> new HashSet<>());
                toElectrify.clear();
                Set<Vec3d> result = new HashSet<>();
                FloodFill.solid(pos, maxDist, result, isWall);
                result.forEach(vec3d -> toElectrify.add(BlockPos.ofFloored(vec3d)));
            } else {
                Random random = world.getRandom();
                Set<Pair<Vec3d, Direction>> walls = new HashSet<>();
                Set<Vec3d> edges = new HashSet<>();
                FloodFill.hollow(pos, maxDist, new HashSet<>(), walls, edges, isWall);
                edges.forEach(edge -> {
                    float offsetX = random.nextFloat() - 0.5f;
                    float offsetY = random.nextFloat() - 0.5f;
                    float offsetZ = random.nextFloat() - 0.5f;
                    BlockPos edgeBlockPos = BlockPos.ofFloored(edge);
                    float fluidHeight = edgeBlockPos.getY() + world.getFluidState(edgeBlockPos).getHeight();
                    spawnSpark(random, world, edge.x + offsetX, fluidHeight + offsetY, edge.z + offsetZ);
                });
                walls.forEach(pair -> {
                    Vec3d wall = pair.first();
                    Direction direction = pair.right();
                    BlockPos wallPos = BlockPos.ofFloored(wall);
                    BlockState state = world.getBlockState(wallPos);
                    VoxelShape collisionShape = state.getOutlineShape(world, wallPos)
                            .offset(wallPos.getX(), wallPos.getY(), wallPos.getZ());
                    List<Box> boundingBoxes = collisionShape.getBoundingBoxes();
                    if(boundingBoxes.isEmpty()) {
                        BlockPos underWall = wallPos.down();
                        Plane plane = Plane.fromBlockPos(underWall, direction.getOpposite());
                        Vector3d randPoint = plane.getRandPointOnPlane();
                        float offsetX = random.nextFloat() - 0.5f;
                        float offsetZ = random.nextFloat() - 0.5f;
                        float fluidHeight = world.getFluidState(underWall).getHeight();
                        spawnSpark(random, world, randPoint.x + offsetX, randPoint.y + fluidHeight, randPoint.z + offsetZ);
                    } else {
                        boundingBoxes.forEach(box -> {
                            Plane plane = Plane.fromSideOfBox(box, direction.getOpposite());
                            Vector3d randPoint = plane.getRandPointOnPlane();
                            spawnSpark(random, world, randPoint.x, randPoint.y, randPoint.z);
                        });
                    }
                });
            }
            return null;
        };
    }

    private void spawnSpark(Random random, World world, double x, double y, double z) {
        int threshold = 10 + MinecraftClient.getInstance().options.getParticles().getValue().getId() * 10;
        if(random.nextInt(threshold) != 0) return;

        float dx = random.nextFloat() / 2 - 0.25f;
        float dy = random.nextFloat() / 2 - 0.25f;
        float dz = random.nextFloat() / 2 - 0.25f;
        world.addParticle(
                ParticleTypes.ELECTRIC_SPARK,
                x, y, z,
                dx, dy, dz
        );
    }

    public Optional<LivingEntity> whoElectrified(BlockPos pos) {
        for (Object2ObjectMap.Entry<LivingEntity, Set<BlockPos>> entry : electrifed.object2ObjectEntrySet()) {
            LivingEntity entity = entry.getKey();
            Set<BlockPos> electrified = entry.getValue();
            if(electrified.contains(pos)) {
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }

    public void clearElectrified(Entity entity) {
        if(entity instanceof LivingEntity) {
            electrifed.remove(entity);
        }
    }
}
