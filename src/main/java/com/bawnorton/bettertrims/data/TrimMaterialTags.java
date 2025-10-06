package com.bawnorton.bettertrims.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

public interface TrimMaterialTags {
	TagKey<TrimMaterial> QUARTZ = bind("quartz");
	TagKey<TrimMaterial> IRON = bind("iron");
	TagKey<TrimMaterial> NETHERITE = bind("netherite");
	TagKey<TrimMaterial> REDSTONE = bind("redstone");
	TagKey<TrimMaterial> COPPER = bind("copper");
	TagKey<TrimMaterial> GOLD = bind("gold");
	TagKey<TrimMaterial> EMERALD = bind("emerald");
	TagKey<TrimMaterial> DIAMOND = bind("diamond");
	TagKey<TrimMaterial> LAPIS = bind("lapis");
	TagKey<TrimMaterial> AMETHYST = bind("amethyst");
	TagKey<TrimMaterial> RESIN = bind("resin");
	TagKey<TrimMaterial> SILVER = bind("silver");

	private static TagKey<TrimMaterial> bind(String name) {
		return TagKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath("c", name));
	}
}
