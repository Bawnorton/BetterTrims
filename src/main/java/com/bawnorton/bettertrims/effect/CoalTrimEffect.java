package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.OptionType;
import com.bawnorton.configurable.Yacl;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

@Configurable(value = "coal", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image("minecraft:textures/item/coal.png"), collapsed = true))
public final class CoalTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;

    public CoalTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(() -> TrimEntityAttributes.WALKING_FURNACE));
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }
}
