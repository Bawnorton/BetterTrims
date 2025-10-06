package com.bawnorton.bettertrims.version;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;

import java.util.Optional;

//? if >=1.21.8 {
import net.minecraft.world.item.component.ProvidesTrimMaterial;
 //?}

public interface VTrims {
	static TrimMaterial getMaterialFromStack(ClientLevel level, ItemStack stack) {
		//? if >=1.21.8 {
        ProvidesTrimMaterial providesTrimMaterial = stack.get(DataComponents.PROVIDES_TRIM_MATERIAL);
        if (providesTrimMaterial == null) return null;

        return providesTrimMaterial.material()
            .unwrap(level.registryAccess().lookupOrThrow(Registries.TRIM_MATERIAL))
            .orElse(null);
        //?} else {
		/*Optional<Holder.Reference<TrimMaterial>> materialReference = TrimMaterials.getFromIngredient(level.registryAccess(), stack);
		return materialReference.map(Holder::value).orElse(null);
		*///?}
	}
}
