package com.bawnorton.bettertrims.data.tag;

import com.bawnorton.bettertrims.BetterTrims;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import java.util.concurrent.CompletableFuture;

public final class TrimEntityAttributeTagProvider extends FabricTagProvider<EntityAttribute> {
    public TrimEntityAttributeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, RegistryKeys.ATTRIBUTE, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        //? if fabric {
        getOrCreateTagBuilder(TrimEntityAttributeTags.ALL)
                .add(Registries.ATTRIBUTE.streamEntries()
                        .filter(ref -> ref.getKey()
                                .map(RegistryKey::getValue)
                                .map(id -> id.getNamespace().equals(BetterTrims.MOD_ID))
                                .orElse(false))
                        .map(RegistryEntry.Reference::value)
                        .toArray(EntityAttribute[]::new));
        //?}
    }
}