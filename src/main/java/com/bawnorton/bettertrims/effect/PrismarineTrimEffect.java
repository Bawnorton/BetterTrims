package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

@Configurable("prismarine")
public final class PrismarineTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "swim_speed", min = 0, max = 10)
    public static float swimSpeed = 0.5f;

    public PrismarineTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.SWIM_SPEED, swimSpeed));
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.THORNS));
        adder.accept(TrimAttribute.leveled(EntityAttributes.GENERIC_OXYGEN_BONUS));
    }

    @Override
    protected boolean getEnabled() {
        return enabled;
    }
}
