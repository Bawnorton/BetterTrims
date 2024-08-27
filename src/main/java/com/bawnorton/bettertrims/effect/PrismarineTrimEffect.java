package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

public final class PrismarineTrimEffect extends TrimEffect {
    public PrismarineTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.SWIM_SPEED, 0.5));
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.THORNS));
        adder.accept(TrimAttribute.leveled(EntityAttributes.GENERIC_OXYGEN_BONUS));
    }
}
