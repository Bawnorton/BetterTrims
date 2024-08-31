package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

@Configurable("glowstone")
public final class GlowstoneTrimEffect extends CelestialEffect {
    @Configurable
    public static boolean enabled = true;

    public GlowstoneTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.GLOWING));
    }

    @Override
    public RegistryEntry<EntityAttribute> getEntityAttribute() {
        return TrimEntityAttributes.HELLS_BLESSING;
    }

    @Override
    public float getMovementSpeed() {
        return AttributeSettings.HellsBlessing.movementSpeed;
    }

    @Override
    public float getDamageResistance() {
        return AttributeSettings.HellsBlessing.damageResistance;
    }

    @Override
    public float getAttackDamage() {
        return AttributeSettings.HellsBlessing.attackDamage;
    }

    @Override
    public float getAttackSpeed() {
        return AttributeSettings.HellsBlessing.attackSpeed;
    }

    @Override
    protected boolean getEnabled() {
        return enabled;
    }
}