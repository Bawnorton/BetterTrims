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

@Configurable(value = "amethyst", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image("minecraft:textures/item/amethyst_shard.png"), collapsed = true))
public final class AmethystTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;

    public AmethystTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.BREWERS_DREAM));
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }
}
