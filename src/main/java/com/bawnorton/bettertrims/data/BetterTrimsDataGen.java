package com.bawnorton.bettertrims.data;

import com.bawnorton.bettertrims.data.tag.TrimEntityAttributeTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import com.bawnorton.bettertrims.data.advancement.BetterTrimsTabAdvancementProvider;
import com.bawnorton.bettertrims.data.loot.TrimsAdvancementLootProvider;
import com.bawnorton.bettertrims.data.tag.TrimMaterialTagProvider;

public final class BetterTrimsDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(TrimMaterialTagProvider::new);
        pack.addProvider(TrimEntityAttributeTagProvider::new);
        pack.addProvider(BetterTrimsTabAdvancementProvider::new);
        pack.addProvider(TrimsAdvancementLootProvider::new);
    }
}
