package com.bawnorton.bettertrims.mixin.accessor;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.core.Holder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SmithingTrimRecipe.class)
public interface SmithingTrimRecipeAccessor {
	//? if >=1.21.8 {
    @Accessor("pattern")
    Holder<TrimPattern> bettertrims$pattern();
    //?} else {
	/*@Accessor("template")
	Ingredient bettertrims$template();
	*///?}
}
