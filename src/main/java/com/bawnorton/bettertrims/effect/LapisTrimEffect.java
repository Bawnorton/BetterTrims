package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributes;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import java.util.function.Consumer;

public final class LapisTrimEffect extends TrimEffect<Void> {
    public LapisTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<RegistryEntry<EntityAttribute>> adder) {
        adder.accept(TrimEntityAttributes.ENCHANTERS_BLESSING);
    }
}
