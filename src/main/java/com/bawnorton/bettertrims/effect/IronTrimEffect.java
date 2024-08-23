package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.applicator.ConsumingTrimEffectApplicator;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributes;
import com.google.common.base.Predicates;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public final class IronTrimEffect extends TrimEffect<Void> {
    public Map<UUID, Boolean> enabled;

    public IronTrimEffect(TagKey<Item> materials) {
        super(materials);
        this.enabled = new HashMap<>();
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.ITEM_MAGNET));
    }

    public void setEnabled(LivingEntity entity, boolean enabled) {
        this.enabled.put(entity.getUuid(), enabled);
    }

    public boolean isEnabledFor(LivingEntity entity) {
        return enabled.getOrDefault(entity.getUuid(), true);
    }

    @Override
    public NbtCompound writeNbt(LivingEntity entity, NbtCompound nbt) {
        nbt.putBoolean("magnet_enabled", isEnabledFor(entity));
        return nbt;
    }

    @Override
    public void readNbt(LivingEntity entity, NbtCompound nbt) {
        if(nbt.contains("magnet_enabled")) {
            setEnabled(entity, nbt.getBoolean("magnet_enabled"));
        }
    }

    @Override
    public ConsumingTrimEffectApplicator getApplicator() {
        return (context, entity) -> {
            if (!isEnabledFor(entity))
                return;

            int itemMagnetLevel = (int) entity.getAttributeValue(TrimEntityAttributes.ITEM_MAGNET);
            World world = entity.getWorld();
            Box area = new Box(entity.getBlockPos());
            area = area.expand(itemMagnetLevel * 1.5);
            List<ItemEntity> nearbyItems = world.getEntitiesByClass(ItemEntity.class, area, Predicates.alwaysTrue());
            Vec3d entityPos = entity.getPos();
            for (ItemEntity itemEntity : nearbyItems) {
                Vec3d itemPos = itemEntity.getPos();
                if (itemPos.isInRange(entityPos, 0.5)) {
                    itemEntity.resetPickupDelay();
                    itemEntity.setPos(entityPos.x, entityPos.y + 0.02, entityPos.z);
                } else {
                    Vec3d difference = entityPos.subtract(itemPos).normalize().multiply(itemMagnetLevel / 10f);
                    itemEntity.setVelocity(difference);
                }
            }
        };
    }
}
