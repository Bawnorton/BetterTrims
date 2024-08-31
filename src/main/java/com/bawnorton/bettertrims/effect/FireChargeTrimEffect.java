package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

@Configurable("fire_charge")
public final class FireChargeTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "fire_resistance", min = 0, max = 1)
    public static float fireResistance = 0.15f;

    public FireChargeTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.FIRE_ASPECT));
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.FIREY_THORNS));
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.FIRE_RESISTANCE, fireResistance));
    }

    @Override
    protected boolean getEnabled() {
        return enabled;
    }
}