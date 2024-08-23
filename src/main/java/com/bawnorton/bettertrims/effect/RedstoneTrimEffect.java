package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

public final class RedstoneTrimEffect extends TrimEffect<Void> {
    public RedstoneTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyBase(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1));
        adder.accept(TrimAttribute.adding(EntityAttributes.GENERIC_STEP_HEIGHT, 0.5));
    }
}
