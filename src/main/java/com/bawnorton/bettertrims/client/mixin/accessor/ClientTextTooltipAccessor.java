package com.bawnorton.bettertrims.client.mixin.accessor;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinEnvironment("client")
@Mixin(ClientTextTooltip.class)
public interface ClientTextTooltipAccessor {
    @Accessor("text")
    FormattedCharSequence bettertrims$text();
}
