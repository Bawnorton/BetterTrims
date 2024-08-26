package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

public final class EnchantedGoldenAppleTrimEffect extends TrimEffect {
    public EnchantedGoldenAppleTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.adding(EntityAttributes.GENERIC_MAX_HEALTH, 2));
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.RESISTANCE, 0.05));
        adder.accept(TrimAttribute.adding(TrimEntityAttributes.REGENERATION, 0.7));
    }

}