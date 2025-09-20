package com.bawnorton.bettertrims.data.provider;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import net.minecraft.data.registries.RegistryPatchGenerator;
import java.util.concurrent.CompletableFuture;

public class BetterTrimsRegistriesDataProvider extends RegistriesDatapackGenerator {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(BetterTrimsRegistries.Keys.TRIM_PROPERTIES, TrimProperties::bootstrap);

    public BetterTrimsRegistriesDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, RegistryPatchGenerator.createLookup(registries, BUILDER).thenApply(RegistrySetBuilder.PatchedRegistries::patches));
    }
}
