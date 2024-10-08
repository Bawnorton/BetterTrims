package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.OptionType;
import com.bawnorton.configurable.Yacl;
import net.minecraft.entity.EquipmentSlot;import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

@Configurable(value = "prismarine", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image("minecraft:textures/item/prismarine_shard.png"), collapsed = true))
public final class PrismarineTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "swim_speed", max = 10)
    public static float swimSpeed = 0.5f;
    @Configurable(value = "oxygen_bonus", max = 15)
    public static int oxygenBonus = 3;

    public PrismarineTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyBase(() -> TrimEntityAttributes.SWIM_SPEED, swimSpeed));
        adder.accept(TrimAttribute.leveled(() -> TrimEntityAttributes.THORNS));
        //? if >=1.21 {
        adder.accept(TrimAttribute.adding(() -> EntityAttributes.GENERIC_OXYGEN_BONUS, oxygenBonus).forSlot(EquipmentSlot.HEAD));
        //?} else {
        /*adder.accept(TrimAttribute.adding(() -> TrimEntityAttributes.GENERIC_OXYGEN_BONUS, oxygenBonus).forSlot(EquipmentSlot.HEAD));
        *///?}
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }
}
