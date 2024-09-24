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

@Configurable(value = "dragons_breath", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image("minecraft:textures/item/dragon_breath.png"), collapsed = true))
public final class DragonsBreathTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "share_effect_radius", min = 0, max = 5)
    public static int shareEffectRadius = 1;

    public DragonsBreathTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.adding(TrimEntityAttributes.SHARE_EFFECT_RADIUS, shareEffectRadius));
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }
}
