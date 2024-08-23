package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.applicator.TrimEffectApplicator;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;
import java.util.function.Consumer;

public abstract class CelestialTrimEffect extends TrimEffect<Float> {
    public CelestialTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(getCelestialAttribute()));
    }

    protected abstract RegistryEntry<EntityAttribute> getCelestialAttribute();

    protected abstract boolean canApply(World world);

    @Override
    public TrimEffectApplicator<Float> getApplicator() {
        return (context, entity) -> {
            int celestialLevel = (int) entity.getAttributeValue(getCelestialAttribute());
            if (context.has(TrimContextParameters.MOVEMENT_SPEED)) {
                float movementSpeed = context.get(TrimContextParameters.MOVEMENT_SPEED);
                return movementSpeed + 0.05f * celestialLevel;
            } else if (context.has(TrimContextParameters.ATTACK_DAMAGE)) {
                float attackDamage = context.get(TrimContextParameters.ATTACK_DAMAGE);
                return attackDamage + 0.5f * celestialLevel;
            } else if (context.has(TrimContextParameters.ATTACK_SPEED)) {
                float attackSpeed = context.get(TrimContextParameters.ATTACK_SPEED);
                return attackSpeed + 0.3f * celestialLevel;
            } else if (context.has(TrimContextParameters.ATTACK_COOLDOWN)) {
                int attackCooldown = context.get(TrimContextParameters.ATTACK_COOLDOWN);
                return attackCooldown - celestialLevel / 0.3f;
            } else if (context.has(TrimContextParameters.DAMAGE_AMOUNT)) {
                float damageAmount = context.get(TrimContextParameters.DAMAGE_AMOUNT);
                return damageAmount * (1 - celestialLevel * 0.03f);
            }
            return 0f;
        };
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) && obj instanceof LivingEntity livingEntity && canApply(livingEntity.getWorld());
    }
}
