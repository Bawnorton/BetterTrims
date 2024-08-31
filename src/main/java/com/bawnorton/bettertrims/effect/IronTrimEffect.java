package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.TagKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Configurable("iron")
public final class IronTrimEffect extends TrimEffect {
    public Map<UUID, Boolean> magnetEnabled;
    @Configurable
    public static boolean enabled = true;

    public IronTrimEffect(TagKey<Item> materials) {
        super(materials);
        this.magnetEnabled = new HashMap<>();
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.ITEM_MAGNET));
    }

    @Override
    protected boolean getEnabled() {
        return enabled;
    }

    public void setMagnetEnabled(LivingEntity entity, boolean enabled) {
        this.magnetEnabled.put(entity.getUuid(), enabled);
    }

    public boolean isMagnetEnabledFor(LivingEntity entity) {
        return magnetEnabled.getOrDefault(entity.getUuid(), true);
    }

    @Override
    public NbtCompound writeNbt(LivingEntity entity, NbtCompound nbt) {
        nbt.putBoolean("magnet_enabled", isMagnetEnabledFor(entity));
        return nbt;
    }

    @Override
    public void readNbt(LivingEntity entity, NbtCompound nbt) {
        if(nbt.contains("magnet_enabled")) {
            setMagnetEnabled(entity, nbt.getBoolean("magnet_enabled"));
        }
    }
}
