package com.bawnorton.bettertrims.data;

//? if fabric {

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.data.provider.BetterTrimsDimensionTypeTagProvider;
import com.bawnorton.bettertrims.data.provider.BetterTrimsEntityTypeTagProvider;
import com.bawnorton.bettertrims.data.provider.BetterTrimsRegistriesDataProvider;
import com.bawnorton.bettertrims.data.provider.BetterTrimsTrimEffectsRegistriesDataProvider;
import com.bawnorton.bettertrims.data.provider.TrimMaterialTagsProvider;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

@Entrypoint("fabric-datagen")
public final class BetterTrimsDataGen implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack mainPack = fabricDataGenerator.createPack();
		mainPack.addProvider(TrimMaterialTagsProvider::new);
		mainPack.addProvider(BetterTrimsEntityTypeTagProvider::new);
		mainPack.addProvider(BetterTrimsDimensionTypeTagProvider::new);

		FabricDataGenerator.Pack defaultPack = fabricDataGenerator.createBuiltinResourcePack(BetterTrims.DEFAULT);
		defaultPack.addProvider(BetterTrimsRegistriesDataProvider::new);

		FabricDataGenerator.Pack trimEffectsDatapack = fabricDataGenerator.createBuiltinResourcePack(BetterTrims.TRIM_EFFECTS);
		trimEffectsDatapack.addProvider(BetterTrimsTrimEffectsRegistriesDataProvider::new);
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
