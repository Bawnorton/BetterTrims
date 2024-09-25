package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.OptionType;
import com.bawnorton.configurable.Yacl;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

//@Configurable(value = "rabbit", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image("minecraft:textures/item/rabbit_hide.png"), collapsed = true))
public final class RabbitTrimEffect extends TrimEffect {
//    @Configurable
    public static boolean enabled = false;

    public RabbitTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }
}