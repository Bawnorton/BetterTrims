package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.applicator.TrimEffectApplicator;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributes;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

public final class AmethystTrimEffect extends TrimEffect<Integer> {
    public AmethystTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.BREWERS_DREAM));
    }

    @Override
    public TrimEffectApplicator<Integer> getApplicator() {
        return (context, entity) -> {
            StatusEffect effect = context.get(TrimContextParameters.STATUS_EFFECT);
            int level = (int) entity.getAttributeValue(TrimEntityAttributes.BREWERS_DREAM);
            float chance = level * 0.075f;
            if (effect != null && BetterTrims.PROBABILITIES.passes(chance)) {
                return effect.isBeneficial() ? 0 : 2;
            }
            return 1;
        };
    }
}
