package com.bawnorton.bettertrims.data.loot;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public final class TrimLootTables {
    //? if <1.21 {
    public static final Identifier GUIDE_BOOK = register("guide_book");

    private static Identifier register(String id) {
        return BetterTrims.id(id);
    }
    //?} else {
    /*public static final RegistryKey<LootTable> GUIDE_BOOK = register("guide_book");

    private static RegistryKey<LootTable> register(String id) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, BetterTrims.id(id));
    }
    *///?}

    public static void init() {
        //no-op
    }
}
