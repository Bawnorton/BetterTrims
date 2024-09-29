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

@Configurable(value = "fire_charge", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image("minecraft:textures/item/fire_charge.png"), collapsed = true))
public final class FireChargeTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "fire_resistance", max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
    public static float fireResistance = 0.15f;

    public FireChargeTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(() -> TrimEntityAttributes.FIRE_ASPECT));
        adder.accept(TrimAttribute.leveled(() -> TrimEntityAttributes.FIREY_THORNS));
        adder.accept(TrimAttribute.multiplyBase(() -> TrimEntityAttributes.FIRE_RESISTANCE, fireResistance));
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }
}