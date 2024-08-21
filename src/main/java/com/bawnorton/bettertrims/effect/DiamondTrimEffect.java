package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.applicator.TrimEffectApplicator;
import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributes;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import com.bawnorton.bettertrims.effect.potion.TrimStatusEffects;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

public final class DiamondTrimEffect extends TrimEffect<Double> {
    public DiamondTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<RegistryEntry<EntityAttribute>> adder) {
        adder.accept(TrimEntityAttributes.MINERS_RUSH);
        adder.accept(TrimEntityAttributes.FORTUNE);
    }

    @Override
    public TrimEffectApplicator<Double> getApplicator() {
        return context -> {
            LivingEntity entity = context.getEntity();
            if (context.has(TrimContextParameters.ENCHANTMENT_LEVEL)) {
                int level = context.get(TrimContextParameters.ENCHANTMENT_LEVEL);
                return level + entity.getAttributeValue(TrimEntityAttributes.FORTUNE);
            } else if (context.has(TrimContextParameters.MINED_BLOCK)) {
                BlockState blockState = context.get(TrimContextParameters.MINED_BLOCK);
                int minersRushLevel = (int) entity.getAttributeValue(TrimEntityAttributes.MINERS_RUSH);
                if (blockState.isIn(ConventionalBlockTags.ORES)) {
                    StatusEffectInstance existing = entity.getStatusEffect(TrimStatusEffects.FEEL_THE_RUSH);
                    int duration = (int) (20 * 2.5 * minersRushLevel);
                    int maxAmplifier = (int) Math.pow(2, minersRushLevel);
                    if (existing == null) {
                        existing = new StatusEffectInstance(TrimStatusEffects.FEEL_THE_RUSH, duration, 0);
                    } else {
                        existing = new StatusEffectInstance(TrimStatusEffects.FEEL_THE_RUSH, duration, Math.min(maxAmplifier, existing.getAmplifier() + 1));
                    }
                    entity.addStatusEffect(existing);
                }
            }
            return 0D;
        };
    }
}
