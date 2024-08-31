package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

@Configurable("diamond")
public final class DiamondTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;

    public DiamondTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.MINERS_RUSH));
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.FORTUNE));
    }

    @Override
    protected boolean getEnabled() {
        return enabled;
    }
}
