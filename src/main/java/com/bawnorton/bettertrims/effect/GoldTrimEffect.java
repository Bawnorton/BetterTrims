package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.applicator.TrimEffectApplicator;
import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributes;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

public final class GoldTrimEffect extends TrimEffect<Float> {
    public GoldTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<RegistryEntry<EntityAttribute>> adder) {
        adder.accept(TrimEntityAttributes.SUNS_BLESSING);
    }

    @Override
    public TrimEffectApplicator<Float> getApplicator() {
        return context -> {
            LivingEntity entity = context.getEntity();
            int sunsBlessingLevel = (int) entity.getAttributeValue(TrimEntityAttributes.SUNS_BLESSING);
            if (context.has(TrimContextParameters.MOVEMENT_SPEED)) {
                float movementSpeed = context.get(TrimContextParameters.MOVEMENT_SPEED);
                return movementSpeed + 0.05f * sunsBlessingLevel;
            } else if (context.has(TrimContextParameters.ATTACK_DAMAGE)) {
                float attackDamage = context.get(TrimContextParameters.ATTACK_DAMAGE);
                return attackDamage + 0.5f * sunsBlessingLevel;
            } else if (context.has(TrimContextParameters.ATTACK_SPEED)) {
                float attackSpeed = context.get(TrimContextParameters.ATTACK_SPEED);
                return attackSpeed + 0.3f * sunsBlessingLevel;
            } else if (context.has(TrimContextParameters.ATTACK_COOLDOWN)) {
                int attackCooldown = context.get(TrimContextParameters.ATTACK_COOLDOWN);
                return attackCooldown - sunsBlessingLevel / 0.3f;
            } else if (context.has(TrimContextParameters.DAMAGE_AMOUNT)) {
                float damageAmount = context.get(TrimContextParameters.DAMAGE_AMOUNT);
                return damageAmount * (1 - sunsBlessingLevel * 0.03f);
            }
            return 0f;
        };
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) && obj instanceof LivingEntity livingEntity && livingEntity.getWorld().isDay();
    }
}
