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
import net.minecraft.DetectedVersion;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;

import java.util.Optional;

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
		defaultPack.addProvider((FabricDataGenerator.Pack.Factory<PackMetadataGenerator>) output -> new PackMetadataGenerator(output)
				.add(
						PackMetadataSection.TYPE,
						new PackMetadataSection(
								Component.literal("Default Better Trims Datapack"),
								//? if >=1.21.8 {
								DetectedVersion.BUILT_IN.packVersion(PackType.SERVER_DATA),
								//?} else {
								/*DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
								*///?}
								Optional.empty()
						)
				)
		);

		FabricDataGenerator.Pack trimEffectsDatapack = fabricDataGenerator.createBuiltinResourcePack(BetterTrims.TRIM_EFFECTS);
		trimEffectsDatapack.addProvider(BetterTrimsTrimEffectsRegistriesDataProvider::new);
		trimEffectsDatapack.addProvider((FabricDataGenerator.Pack.Factory<PackMetadataGenerator>) output -> new PackMetadataGenerator(output)
				.add(
						PackMetadataSection.TYPE,
						new PackMetadataSection(
								Component.literal("Better Trims Trim Effects Datapack"),
								//? if >=1.21.8 {
								DetectedVersion.BUILT_IN.packVersion(PackType.SERVER_DATA),
								//?} else {
								/*DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
								*///?}
								Optional.empty()
						)
				)
		);
	}
}
//?} else if neoforge {

/*import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.data.provider.*;
import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

//? if <1.21.8 {
/^import net.neoforged.neoforge.common.data.ExistingFileHelper;
^///?}

@EventBusSubscriber(modid = BetterTrims.MOD_ID)
public final class BetterTrimsDataGen {
    @SubscribeEvent
    //? if >=1.21.8 {
    public static void gatherData(GatherDataEvent.Server event) {
    //?} else {
    /^public static void gatherData(GatherDataEvent event) {
		^///?}
			DataGenerator gen = event.getGenerator();
	    CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
			//? if <1.21.8 {
      /^ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
			^///?}

	    PackOutput mainPack = gen.getPackOutput();
			//? if >=1.21.8 {
	    gen.addProvider(true, new TrimMaterialTagsProvider(mainPack, lookupProvider));
	    gen.addProvider(true, new BetterTrimsEntityTypeTagProvider(mainPack, lookupProvider));
	    gen.addProvider(true, new BetterTrimsDimensionTypeTagProvider(mainPack, lookupProvider));
	    //?} else {
			/^gen.addProvider(true, new TrimMaterialTagsProvider(mainPack, lookupProvider, existingFileHelper));
			gen.addProvider(true, new BetterTrimsEntityTypeTagProvider(mainPack, lookupProvider, existingFileHelper));
			gen.addProvider(true, new BetterTrimsDimensionTypeTagProvider(mainPack, lookupProvider, existingFileHelper));
	    ^///?}

	    DataGenerator.PackGenerator defaultPack = gen.getBuiltinDatapack(true, BetterTrims.MOD_ID, BetterTrims.DEFAULT.getPath());
			defaultPack.addProvider(output -> new BetterTrimsRegistriesDataProvider(output, lookupProvider));
			defaultPack.addProvider(output -> new PackMetadataGenerator(output)
					.add(
							PackMetadataSection.TYPE,
							new PackMetadataSection(
									Component.literal("Default Better Trims Datapack"),
									//? if >=1.21.8 {
									DetectedVersion.BUILT_IN.packVersion(PackType.SERVER_DATA),
									//?} else {
									/^DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
									^///?}
									Optional.empty()
							)
					)
			);

	    DataGenerator.PackGenerator trimEffectsDatapack = gen.getBuiltinDatapack(true, BetterTrims.MOD_ID, BetterTrims.TRIM_EFFECTS.getPath());
			trimEffectsDatapack.addProvider(output -> new BetterTrimsTrimEffectsRegistriesDataProvider(output, lookupProvider));
			trimEffectsDatapack.addProvider(output -> new PackMetadataGenerator(output)
					.add(
							PackMetadataSection.TYPE,
							new PackMetadataSection(
									Component.literal("Better Trims Trim Effects Datapack"),
									//? if >=1.21.8 {
									DetectedVersion.BUILT_IN.packVersion(PackType.SERVER_DATA),
									//?} else {
									/^DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
									^///?}
									Optional.empty()
							)
					)
			);
    }
}
*///?}
