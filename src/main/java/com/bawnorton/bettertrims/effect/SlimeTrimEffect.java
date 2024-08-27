package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

public final class SlimeTrimEffect extends TrimEffect {
    public SlimeTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.BOUNCY).forSlot(AttributeModifierSlot.FEET));
        adder.accept(TrimAttribute.adding(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, -1));
        adder.accept(TrimAttribute.leveled(EntityAttributes.GENERIC_ATTACK_KNOCKBACK));
    }
}