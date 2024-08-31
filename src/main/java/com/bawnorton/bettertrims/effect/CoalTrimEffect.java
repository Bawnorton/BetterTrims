package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

@Configurable("coal")
public final class CoalTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;

    public CoalTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.WALKING_FURNACE));
    }

    @Override
    protected boolean getEnabled() {
        return enabled;
    }
}
