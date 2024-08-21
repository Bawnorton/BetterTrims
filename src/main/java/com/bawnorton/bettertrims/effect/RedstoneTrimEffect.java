package com.bawnorton.bettertrims.effect;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

public final class RedstoneTrimEffect extends TrimEffect<Void> {
    public RedstoneTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<RegistryEntry<EntityAttribute>> adder) {
        adder.accept(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        adder.accept(EntityAttributes.GENERIC_STEP_HEIGHT);
    }
}
