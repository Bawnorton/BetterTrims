package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.OptionType;
import com.bawnorton.configurable.Yacl;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Configurable(value = "copper", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image("minecraft:textures/item/copper_ingot.png"), collapsed = true))
public final class CopperTrimEffect extends TrimEffect {
    private final Object2ObjectMap<LivingEntity, Set<BlockPos>> electrified = new Object2ObjectOpenHashMap<>();
    @Configurable
    public static boolean enabled = true;

    public CopperTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.ELECTRIFYING));
    }

    @Override
    protected boolean getEnabled() {
        return enabled;
    }

    public Object2ObjectMap<LivingEntity, Set<BlockPos>> getElectrified() {
        return electrified;
    }

    public Optional<LivingEntity> whoElectrified(BlockPos pos) {
        for (Object2ObjectMap.Entry<LivingEntity, Set<BlockPos>> entry : electrified.object2ObjectEntrySet()) {
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
            electrified.remove(entity);
        }
    }

    public @Nullable CopperTrimEffect.ElectrifyingInfo electrifyingInfo(LivingEntity entity) {
        if(entity.getAttributeValue(TrimEntityAttributes.ELECTRIFYING) <= 0) {
            clearElectrified(entity);
            return null;
        }

        World world = entity.getWorld();
        Vec3d pos = entity.getPos();
        BlockPos blockPos = BlockPos.ofFloored(pos);
        if (!world.isWater(blockPos))
            return null;

        double electrifying = entity.getAttributeValue(TrimEntityAttributes.ELECTRIFYING);
        double maxDist = electrifying * AttributeSettings.Electrifying.radius;

        Predicate<Vec3d> isWall = vec3d -> {
            BlockPos wallPos = BlockPos.ofFloored(vec3d);
            BlockState state = world.getBlockState(wallPos);

            FluidState fluidState = state.getFluidState();
            boolean water = fluidState.isIn(FluidTags.WATER);
            boolean waterlogged = water && state.getOrEmpty(Properties.WATERLOGGED).orElse(false);
            if (waterlogged) {
                return !wallPos.equals(BlockPos.ofFloored(pos));
            }

            return !water;
        };
        return new ElectrifyingInfo(world, pos, maxDist, isWall);
    }

    public record ElectrifyingInfo(World world, Vec3d pos, double maxDist, Predicate<Vec3d> isWall) {
    }
}
