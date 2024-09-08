package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.Yacl;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

@Configurable(value = "leather", yacl = @Yacl(image = @Image("minecraft:textures/item/leather.png"), collapsed = true))
public final class LeatherTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;

    public LeatherTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.LIGHT_FOOTED));
    }

    @Override
    protected boolean getEnabled() {
        return enabled;
    }
}
