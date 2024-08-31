package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

@Configurable("netherite")
public final class NetheriteTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "fire_resistance", min = 0, max = 1)
    public static float fireResistance = 0.2f;
    @Configurable(min = 0, max = 1)
    public static float resistance = 0.08f;

    public NetheriteTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.FIRE_RESISTANCE, fireResistance));
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.RESISTANCE, resistance));
    }

    @Override
    protected boolean getEnabled() {
        return enabled;
    }
}
