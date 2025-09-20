package com.bawnorton.bettertrims.data.provider;

import com.bawnorton.bettertrims.data.BetterTrimsItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import java.util.concurrent.CompletableFuture;

public class BetterTrimsItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public BetterTrimsItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(BetterTrimsItemTags.TRIM_PATTERN_TEMPLATES)
            .add(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE)
            .add(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE);
    }

    //? if 1.21.1 {
    /*private FabricTagBuilder builder(TagKey<EntityType<?>> type) {
        return getOrCreateTagBuilder(type);
    }
    *///?}
}
