package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.OptionType;
import com.bawnorton.configurable.Yacl;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

@Configurable(value = "redstone", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image("minecraft:textures/item/redstone.png"), collapsed = true))
public final class RedstoneTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "movement_speed", max = 16, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
    public static float movementSpeed = 0.1f;
    @Configurable(value = "step_height", max = 16, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
    public static float stepHeight = 0.5f;

    public RedstoneTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyBase(() -> EntityAttributes.GENERIC_MOVEMENT_SPEED, movementSpeed));
        //? if >=1.21 {
        /*adder.accept(TrimAttribute.adding(() -> EntityAttributes.GENERIC_STEP_HEIGHT, stepHeight));
        *///?} else {
        adder.accept(TrimAttribute.adding(() -> TrimEntityAttributes.GENERIC_STEP_HEIGHT, stepHeight));
        //?}
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }
}
