package com.bawnorton.bettertrims.registry.content;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class TrimLootTables {
    public static final RegistryKey<LootTable> GUIDE_BOOK = register("guide_book");

    private static RegistryKey<LootTable> register(String id) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, BetterTrims.id(id));
    }

    public static void init() {
        //no-op
    }
}
