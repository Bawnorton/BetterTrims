package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

@Configurable("emerald")
public final class EmeraldTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "trade_discount", min = 0, max = 1)
    public static float tradeDiscount = 0.1f;

    public EmeraldTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.TRADE_DISCOUNT, tradeDiscount));
    }

    @Override
    protected boolean getEnabled() {
        return enabled;
    }
}
