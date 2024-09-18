package com.bawnorton.bettertrims.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

//? if >=1.21 {
/*import com.bawnorton.bettertrims.data.advancement.BetterTrimsTabAdvancementProvider;
import com.bawnorton.bettertrims.data.loot.TrimsAdvancementLootProvider;
import com.bawnorton.bettertrims.data.tag.TrimMaterialTagProvider;
*///?}

public final class BetterTrimsDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        //? if >=1.21 {
        /*FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(TrimMaterialTagProvider::new);
        pack.addProvider(BetterTrimsTabAdvancementProvider::new);
        pack.addProvider(TrimsAdvancementLootProvider::new);
        *///?}
    }
}
