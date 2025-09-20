package com.bawnorton.bettertrims.data.provider;

import com.bawnorton.bettertrims.data.TrimMaterialTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import java.util.concurrent.CompletableFuture;

public class TrimMaterialTagsProvider extends FabricTagProvider<TrimMaterial> {
    public TrimMaterialTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.TRIM_MATERIAL, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(TrimMaterialTags.QUARTZ).setReplace(false).add(TrimMaterials.QUARTZ);
        builder(TrimMaterialTags.IRON).setReplace(false).add(TrimMaterials.IRON);
        builder(TrimMaterialTags.NETHERITE).setReplace(false).add(TrimMaterials.NETHERITE);
        builder(TrimMaterialTags.REDSTONE).setReplace(false).add(TrimMaterials.REDSTONE);
        builder(TrimMaterialTags.COPPER).setReplace(false).add(TrimMaterials.COPPER);
        builder(TrimMaterialTags.GOLD).setReplace(false).add(TrimMaterials.GOLD);
        builder(TrimMaterialTags.EMERALD).setReplace(false).add(TrimMaterials.EMERALD);
        builder(TrimMaterialTags.DIAMOND).setReplace(false).add(TrimMaterials.DIAMOND);
        builder(TrimMaterialTags.LAPIS).setReplace(false).add(TrimMaterials.LAPIS);
        builder(TrimMaterialTags.AMETHYST).setReplace(false).add(TrimMaterials.AMETHYST);
        builder(TrimMaterialTags.SILVER).setReplace(false);
        //? if 1.21.8 {
        builder(TrimMaterialTags.RESIN).setReplace(false).add(TrimMaterials.RESIN);
        //?}
    }

    //? if 1.21.1 {
    /*private FabricTagBuilder builder(TagKey<TrimMaterial> type) {
        return getOrCreateTagBuilder(type);
    }
    *///?}
}
