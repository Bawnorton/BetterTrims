package com.bawnorton.bettertrims.client.mixin;

import com.bawnorton.bettertrims.client.tooltip.AbilityTooltipRenderer;
import com.bawnorton.bettertrims.client.tooltip.vanilla.VanillaAbilityTooltipRenderer;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@MixinEnvironment("client")
@Mixin(Screen.class)
abstract class ScreenMixin {
	@WrapMethod(
			method = "getTooltipFromItem"
	)
	private static List<Component> captureTooltipItem(Minecraft minecraft, ItemStack item, Operation<List<Component>> original) {
		AbilityTooltipRenderer.setStack(item);
		return original.call(minecraft, item);
	}
}