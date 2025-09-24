package com.bawnorton.bettertrims.client.mixin;

import com.bawnorton.bettertrims.client.tooltip.AbilityTooltipRenderer;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import java.util.List;

@MixinEnvironment("client")
@Mixin(CreativeModeInventoryScreen.class)
abstract class CreativeModeInventoryScreenMixin {
    @WrapMethod(
        method = "getTooltipFromContainerItem"
    )
    private List<Component> captureTooltipItem(ItemStack stack, Operation<List<Component>> original) {
        AbilityTooltipRenderer.setStack(stack);
        return original.call(stack);
    }
}
