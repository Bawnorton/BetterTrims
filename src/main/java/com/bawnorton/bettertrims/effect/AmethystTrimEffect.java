package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.applicator.TrimEffectApplicator;
import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributes;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

public final class AmethystTrimEffect extends TrimEffect<Integer> {
    public AmethystTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<RegistryEntry<EntityAttribute>> adder) {
        adder.accept(TrimEntityAttributes.BREWERS_DREAM);
    }

    @Override
    public TrimEffectApplicator<Integer> getApplicator() {
        return (context) -> {
            LivingEntity entity = context.getEntity();
            StatusEffect effect = context.get(TrimContextParameters.STATUS_EFFECT);
            double chance = entity.getAttributeValue(TrimEntityAttributes.BREWERS_DREAM) / 4;
            if (BetterTrims.PROBABILITIES.passes(chance)) {
                if (effect != null) {
                    return effect.isBeneficial() ? 0 : 2;
                }
            }
            return 1;
        };
    }
}
