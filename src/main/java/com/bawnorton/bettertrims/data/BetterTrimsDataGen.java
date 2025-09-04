package com.bawnorton.bettertrims.data;

//? if fabric {

import com.bawnorton.bettertrims.data.provider.BetterTrimsRegistriesDataProvider;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

@Entrypoint("fabric-datagen")
public final class BetterTrimsDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider((BetterTrimsRegistriesDataProvider::new));
    }
}
//?} else if neoforge {
/*import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = BetterTrims.MOD_ID)
public final class BetterTrimsDataGen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        event.createProvider((output, lookup) -> null);
    }
}
*///?}
