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

@Configurable(value = "quartz", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image("minecraft:textures/item/quartz.png"), collapsed = true))
public final class QuartzTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "bonus_xp", min = 0, max = 100)
    public static double bonusXp = 0.1;

    public QuartzTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.BONUS_XP, bonusXp));
    }

    @Override
    protected boolean getEnabled() {
        return enabled;
    }
}
