package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

public abstract class CelestialEffect extends TrimEffect {
    protected CelestialEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(getEntityAttribute()));
    }

    //? if >=1.21 {
    public abstract RegistryEntry<EntityAttribute> getEntityAttribute();
    //?} else {
    /*public abstract EntityAttribute getEntityAttribute();
    *///?}

    public abstract float getMovementSpeed();

    public abstract float getDamageResistance();

    public abstract float getAttackDamage();

    public abstract float getAttackSpeed();
}
