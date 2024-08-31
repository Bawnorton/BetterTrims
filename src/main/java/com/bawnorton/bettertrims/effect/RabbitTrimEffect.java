package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.configurable.Configurable;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

@Configurable("rabbit")
public final class RabbitTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = false;

    public RabbitTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {

    }

    @Override
    protected boolean getEnabled() {
        return enabled;
    }
}