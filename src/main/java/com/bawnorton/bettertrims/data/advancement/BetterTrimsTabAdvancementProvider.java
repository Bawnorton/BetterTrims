package com.bawnorton.bettertrims.data.advancement;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.advancement.criterion.WalkingFurnaceSmeltedCriteron;
import com.bawnorton.bettertrims.registry.content.TrimLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class BetterTrimsTabAdvancementProvider extends FabricAdvancementProvider {
    public BetterTrimsTabAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookup) {
        super(output, lookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup lookup, Consumer<AdvancementEntry> exporter) {
        AdvancementEntry root = Advancement.Builder.create()
                .display(
                        Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE,
                        Text.translatable("advancements.bettertrims.root.title"),
                        Text.translatable("advancements.bettertrims.root.description"),
                        Identifier.ofVanilla("textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().tag(ItemTags.TRIM_TEMPLATES)))
                .rewards(AdvancementRewards.Builder.loot(TrimLootTables.GUIDE_BOOK))
                .build(exporter, BetterTrims.sid("root"));
        AdvancementEntry gettingHotInHere = Advancement.Builder.create()
                .parent(root)
                .display(
                        Items.COAL,
                        Text.translatable("advancements.bettertrims.getting_hot_in_here.title"),
                        Text.translatable("advancements.bettertrims.getting_hot_in_here.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("walking_furnace_smelted", WalkingFurnaceSmeltedCriteron.Conditions.create())
                .build(exporter, BetterTrims.sid("getting_hot_in_here"));
    }
}
