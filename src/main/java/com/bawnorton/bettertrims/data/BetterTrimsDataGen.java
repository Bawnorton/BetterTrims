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

/*import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.data.provider.*;
import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = BetterTrims.MOD_ID)
public final class BetterTrimsDataGen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
			DataGenerator gen = event.getGenerator();
      ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

			event.createDatapackRegistryObjects(new RegistrySetBuilder().add(BetterTrimsRegistries.Keys.TRIM_PROPERTIES, TrimProperties::bootstrap));
	    CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

	    PackOutput mainPack = gen.getPackOutput();
			gen.addProvider(event.includeServer(), new TrimMaterialTagsProvider(mainPack, lookupProvider, existingFileHelper));
			gen.addProvider(event.includeServer(), new BetterTrimsEntityTypeTagProvider(mainPack, lookupProvider, existingFileHelper));
			gen.addProvider(event.includeServer(), new BetterTrimsDimensionTypeTagProvider(mainPack, lookupProvider, existingFileHelper));

	    DataGenerator.PackGenerator defaultPack = gen.getBuiltinDatapack(true, BetterTrims.MOD_ID, BetterTrims.DEFAULT.getPath());
			defaultPack.addProvider(output -> new BetterTrimsRegistriesDataProvider(output, lookupProvider));

	    DataGenerator.PackGenerator trimEffectsDatapack = gen.getBuiltinDatapack(true, BetterTrims.MOD_ID, BetterTrims.TRIM_EFFECTS.getPath());
			trimEffectsDatapack.addProvider(output -> new BetterTrimsTrimEffectsRegistriesDataProvider(output, lookupProvider));
    }
}
*///?}
