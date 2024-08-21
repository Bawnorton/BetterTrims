package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.applicator.TrimEffectApplicator;
import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributes;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public final class EmeraldTrimEffect extends TrimEffect<Integer> {
    public EmeraldTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<RegistryEntry<EntityAttribute>> adder) {
        adder.accept(TrimEntityAttributes.TRADE_DISCOUNT);
    }

    @Override
    public TrimEffectApplicator<Integer> getApplicator() {
        return context -> {
            LivingEntity entity = context.getEntity();
            int tradeCount = context.get(TrimContextParameters.COUNT);
            if (tradeCount == 1) return 0;

            double percentDiscount = entity.getAttributeValue(TrimEntityAttributes.TRADE_DISCOUNT) - 1;
            double result = percentDiscount * tradeCount;
            return (int) Math.ceil(result);
        };
    }

    @Override
    protected @NotNull EntityAttributeModifier getAttributeModifier(RegistryEntry<EntityAttribute> entry, AttributeModifierSlot slot) {
        return new EntityAttributeModifier(getSlotId(entry.getIdAsString(), slot.asString()), 0.1, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }
}
