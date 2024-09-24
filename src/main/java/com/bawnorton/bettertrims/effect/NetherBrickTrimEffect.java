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

@Configurable(value = "nether_brick", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image("minecraft:textures/item/nether_brick.png"), collapsed = true))
public final class NetherBrickTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "fire_resistance", min = 0, max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
    public static float fireResistance = 0.12f;
    @Configurable(value = "cleaving_chance", min = 0, max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
    public static float cleavingChance = 0.03f;

    public NetherBrickTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.FIRE_RESISTANCE, fireResistance));
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.CLEAVING, cleavingChance));
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }
}
