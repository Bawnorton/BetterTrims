package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.applicator.TrimEffectApplicator;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributes;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

public final class NetheriteTrimEffect extends TrimEffect<Float> {
    public NetheriteTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.FIRE_RESISTANCE, 0.2));
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.RESISTANCE, 0.08));
    }

    @Override
    public TrimEffectApplicator<Float> getApplicator() {
        return (context, entity) -> {
            double resistance = entity.getAttributeValue(TrimEntityAttributes.RESISTANCE) - 1;
            double fireResistance = entity.getAttributeValue(TrimEntityAttributes.FIRE_RESISTANCE) - 1;
            DamageSource source = context.get(TrimContextParameters.DAMAGE_SOURCE);
            float damageAmount = context.get(TrimContextParameters.DAMAGE_AMOUNT);
            if (source.isIn(DamageTypeTags.IS_FIRE)) {
                damageAmount *= (float) (1 - fireResistance);
            }
            return (float) (damageAmount * (1 - resistance));
        };
    }
}
