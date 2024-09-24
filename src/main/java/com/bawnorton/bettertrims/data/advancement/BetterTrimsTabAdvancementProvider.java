package com.bawnorton.bettertrims.data.advancement;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.data.advancement.criterion.*;
import com.bawnorton.bettertrims.data.loot.TrimLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

//? if >=1.21 {
import net.minecraft.advancement.AdvancementEntry;
//?} else {
/*import net.minecraft.advancement.criterion.AbstractCriterionConditions;
*///?}

public final class BetterTrimsTabAdvancementProvider extends FabricAdvancementProvider {
    //? if >=1.21 {
    public BetterTrimsTabAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookup) {
        super(output, lookup);
    }
    //?} else {
    /*public BetterTrimsTabAdvancementProvider(FabricDataOutput output) {
        super(output);
    }
    *///?}

    //? if >=1.21 {
    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup lookup, Consumer<AdvancementEntry> exporter) {
    //?} else {
    /*@Override
    public void generateAdvancement(Consumer<Advancement> exporter) {
    *///?}
        //? if >=1.21 {
        AdvancementEntry root;
        //?} else {
        /*Advancement root;
        *///?}
        root = Advancement.Builder.create()
                .display(
                        Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE,
                        Text.translatable("advancements.bettertrims.root.title"),
                        Text.translatable("advancements.bettertrims.root.description"),
                        //? if >=1.21 {
                        Identifier.ofVanilla("textures/gui/advancements/backgrounds/adventure.png"),
                        //?} else {
                        /*new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                        *///?}
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create()
                        .tag(ItemTags.TRIM_TEMPLATES)
                        //? if <1.21
                        /*.build()*/
                ))
                .rewards(AdvancementRewards.Builder.loot(TrimLootTables.GUIDE_BOOK))
                .build(exporter, BetterTrims.sid("root"));

        createTaskEntry(
                root,
                Items.AMETHYST_SHARD,
                "it_must_be_a_dream",
                BrewersDreamExtendedCriteron.Conditions.create(),
                exporter
        );

        createTaskEntry(
                root,
                Items.CHORUS_FRUIT,
                "miss_me",
                DodgeCriterion.Conditions.create(),
                exporter
        );

        createTaskEntry(
                root,
                Items.COAL,
                "getting_hot_in_here",
                WalkingFurnaceSmeltedCriteron.Conditions.create(),
                exporter
        );

        createTaskEntry(
                root,
                Items.COPPER_INGOT,
                "shocking",
                ElectrifyingKilledCriterion.Conditions.create(),
                exporter
        );

        createChallengeEntry(
                root,
                Items.DIAMOND,
                "rock_and_stone",
                MinersRushMaxLevelCriterion.Conditions.create(),
                exporter
        );

        createTaskEntry(
                root,
                Items.DRAGON_BREATH,
                "i_am_a_generous_player",
                SharedEffectCriterion.Conditions.create(),
                exporter
        );

        createChallengeEntry(
                root,
                Items.ECHO_SHARD,
                "where_was_i",
                EchoingTriggeredCriterion.Conditions.create(),
                exporter
        );

        createTaskEntry(
                root,
                Items.EMERALD,
                "an_even_better_deal",
                DiscountedTradeCriterion.Conditions.create(),
                exporter
        );

        createTaskEntry(
                root,
                Items.IRON_HELMET,
                "stay_out_of_my_head",
                MagneticHelmetWornCriterion.Conditions.create(),
                exporter
        );

        createTaskEntry(
                root,
                Items.LAPIS_LAZULI,
                "still_nothing_good",
                EnchantersFavourRerolledMaxCriterion.Conditions.create(),
                exporter
        );

        createTaskEntry(
                root,
                Items.WATER_BUCKET,
                "it_burns",
                HydrophobicTouchWaterCriterion.Conditions.create(),
                exporter
        );

        createChallengeEntry(
                root,
                Items.CREEPER_HEAD,
                "whos_the_creeper_now",
                LightFootedSneakByCreeperCriterion.Conditions.create(),
                exporter
        );

        createChallengeEntry(
                root,
                Items.PIGLIN_HEAD,
                "pig_on_a_spike",
                CleavingDecapitatePiglinCriterion.Conditions.create(),
                exporter
        );

        createTaskEntry(
                root,
                Items.SLIME_BALL,
                "boing",
                BouncyBootsWornCriterion.Conditions.create(),
                exporter
        );
    }

    //? if >=1.21 {
    private AdvancementEntry createChallengeEntry(AdvancementEntry parent, Item displayItem, String name, AdvancementCriterion<?> criterion, Consumer<AdvancementEntry> exporter) {
        return createEntry(parent, displayItem, name, AdvancementFrame.CHALLENGE, true, true, false, 100, criterion, exporter);
    }

    private AdvancementEntry createTaskEntry(AdvancementEntry parent, Item displayItem, String name, AdvancementCriterion<?> criterion, Consumer<AdvancementEntry> exporter) {
        return createEntry(parent, displayItem, name, AdvancementFrame.TASK, true, true, false, 0, criterion, exporter);
    }

    private AdvancementEntry createEntry(AdvancementEntry parent, Item displayItem, String name, AdvancementFrame frame, boolean toast, boolean chat, boolean hidden, int xp, AdvancementCriterion<?> criterion, Consumer<AdvancementEntry> exporter) {
        return Advancement.Builder.create()
                .parent(parent)
                .display(
                        displayItem,
                        Text.translatable("advancements.bettertrims.%s.title".formatted(name)),
                        Text.translatable("advancements.bettertrims.%s.description".formatted(name)),
                        null,
                        frame,
                        toast,
                        chat,
                        hidden
                )
                .criterion(name, criterion)
                .rewards(AdvancementRewards.Builder.experience(xp))
                .build(exporter, BetterTrims.sid(name));
    }
    //?} else {
    /*private Advancement createChallengeEntry(Advancement parent, Item displayItem, String name, AbstractCriterionConditions criterion, Consumer<Advancement> exporter) {
        return createEntry(parent, displayItem, name, AdvancementFrame.CHALLENGE, true, true, false, 100, criterion, exporter);
    }

    private Advancement createTaskEntry(Advancement parent, Item displayItem, String name, AbstractCriterionConditions criterion, Consumer<Advancement> exporter) {
        return createEntry(parent, displayItem, name, AdvancementFrame.TASK, true, true, false, 0, criterion, exporter);
    }

    private Advancement createEntry(Advancement parent, Item displayItem, String name, AdvancementFrame frame, boolean toast, boolean chat, boolean hidden, int xp, AbstractCriterionConditions criterion, Consumer<Advancement> exporter) {
        return Advancement.Builder.create()
                .parent(parent)
                .display(
                        displayItem,
                        Text.translatable("advancements.bettertrims.%s.title".formatted(name)),
                        Text.translatable("advancements.bettertrims.%s.description".formatted(name)),
                        null,
                        frame,
                        toast,
                        chat,
                        hidden
                )
                .criterion(name, criterion)
                .rewards(AdvancementRewards.Builder.experience(xp))
                .build(exporter, BetterTrims.sid(name));
    }
    *///?}
}
