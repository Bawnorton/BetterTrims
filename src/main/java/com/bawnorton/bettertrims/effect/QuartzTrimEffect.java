package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.applicator.TrimEffectApplicator;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributes;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

public final class QuartzTrimEffect extends TrimEffect<Integer> {
    public QuartzTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.BONUS_XP, 0.1));
    }

    @Override
    public TrimEffectApplicator<Integer> getApplicator() {
        return (context, entity) -> {
            double bonusXpPercentage = entity.getAttributeValue(TrimEntityAttributes.BONUS_XP) - 1;
            int experience = context.get(TrimContextParameters.EXPERIENCE);
            return (int) (experience + experience * bonusXpPercentage);
        };
    }
}
