package com.bawnorton.bettertrims.data.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetLoreLootFunction;
import net.minecraft.loot.function.SetNameLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public final class TrimsAdvancementLootProvider extends SimpleFabricLootTableProvider {
    //? if >=1.21 {
    public TrimsAdvancementLootProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookup) {
        super(output, lookup, LootContextTypes.ADVANCEMENT_REWARD);
    }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter) {
        //? if fabric {
        exporter.accept(TrimLootTables.GUIDE_BOOK, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(Items.BOOK))
                        .apply(SetNameLootFunction.builder(
                                Text.literal("BetterTrims Guidebook"),
                                SetNameLootFunction.Target.CUSTOM_NAME
                        ))
                        .apply(SetLoreLootFunction.builder()
                                .lore(Text.literal("In-Game Coming Soon™"))
                                .lore(Text.literal("Right click to open info page"))
                                .build()
                        )
                )
        );
        //?}
    }
    //?} else {
    /*public TrimsAdvancementLootProvider(FabricDataOutput output) {
        super(output, LootContextTypes.ADVANCEMENT_REWARD);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        //? if fabric {
        exporter.accept(TrimLootTables.GUIDE_BOOK, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(Items.BOOK))
                        .apply(SetNameLootFunction.builder(Text.literal("BetterTrims Guidebook")))
                        .apply(SetLoreLootFunction.builder()
                                .lore(Text.literal("In-Game Coming Soon™"))
                                .lore(Text.literal("Right click to open info page"))
                                .build()
                        )
                )
        );
        //?}
    }
    *///?}
}