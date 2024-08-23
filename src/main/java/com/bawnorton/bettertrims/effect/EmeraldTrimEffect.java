package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.applicator.TrimEffectApplicator;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributes;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

public final class EmeraldTrimEffect extends TrimEffect<Integer> {
    public EmeraldTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.TRADE_DISCOUNT, 0.1));
    }

    @Override
    public TrimEffectApplicator<Integer> getApplicator() {
        return (context, entity) -> {
            int tradeCount = context.get(TrimContextParameters.COUNT);
            if (tradeCount == 1)
                return 0;

            double percentDiscount = entity.getAttributeValue(TrimEntityAttributes.TRADE_DISCOUNT) - 1;
            double result = percentDiscount * tradeCount;
            return (int) Math.ceil(result);
        };
    }
}
