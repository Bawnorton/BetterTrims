package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.applicator.TrimEffectApplicator;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributes;
import com.bawnorton.bettertrims.effect.component.TrimComponentTypes;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

public final class LapisTrimEffect extends TrimEffect<ItemStack> {
    public LapisTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.ENCHANTERS_BLESSING));
    }

    @Override
    public TrimEffectApplicator<ItemStack> getApplicator() {
        return (context, entity) -> {
            ItemStack itemStack = context.get(TrimContextParameters.ITEM_STACK);
            int usedBlessings = itemStack.getOrDefault(TrimComponentTypes.USED_BLESSINGS, 0);
            usedBlessings++;
            if (usedBlessings > entity.getAttributeValue(TrimEntityAttributes.ENCHANTERS_BLESSING)) {
                return null;
            }

            itemStack.set(TrimComponentTypes.USED_BLESSINGS, usedBlessings);
            return itemStack;
        };
    }
}
