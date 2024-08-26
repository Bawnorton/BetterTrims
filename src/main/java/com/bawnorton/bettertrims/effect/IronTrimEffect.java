package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.TagKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public final class IronTrimEffect extends TrimEffect {
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

}
