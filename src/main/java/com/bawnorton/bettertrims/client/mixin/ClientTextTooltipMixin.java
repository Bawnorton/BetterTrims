package com.bawnorton.bettertrims.client.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import java.util.concurrent.atomic.AtomicLong;

@MixinEnvironment("client")
@Mixin(ClientTextTooltip.class)
abstract class ClientTextTooltipMixin {
    @Shadow
    @Final
    private FormattedCharSequence text;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\"");
        text.accept((i, style, codepoint) -> {
            builder.appendCodePoint(codepoint);
            return true;
        });
        builder.append("\"");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        AtomicLong hash = new AtomicLong();
        text.accept((i, style, codepoint) -> {
            hash.getAndUpdate(h -> 31 * h + codepoint ^ style.hashCode() + 31L * i);
            return true;
        });
        return (int) hash.get();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ClientTextTooltip other)) return false;

        return other.hashCode() == this.hashCode();
    }
}
