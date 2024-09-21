package com.bawnorton.bettertrims.data.tag;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public final class TrimEntityAttributeTags {
    public static final TagKey<EntityAttribute> ALL = register("all");

    private static TagKey<EntityAttribute> register(String id) {
        return TagKey.of(RegistryKeys.ATTRIBUTE, BetterTrims.id("%s".formatted(id)));
    }
}
