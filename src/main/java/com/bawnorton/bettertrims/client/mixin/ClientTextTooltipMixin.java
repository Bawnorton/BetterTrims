package com.bawnorton.bettertrims.client.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@MixinEnvironment("client")
@Mixin(ClientTextTooltip.class)
abstract class ClientTextTooltipMixin {
    @Shadow
    @Final
    private FormattedCharSequence text;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\"");
        text.accept((i, style, j) -> {
            builder.appendCodePoint(j);
            return true;
        });
        builder.append("\"");
        return builder.toString();
    }
}
