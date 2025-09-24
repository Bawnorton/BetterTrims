package com.bawnorton.bettertrims.client.mixin.accessor;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinEnvironment("client")
@Mixin(IntRange.class)
public interface IntRangeAccessor {
    @Accessor("min")
    NumberProvider bettertrims$min();

    @Accessor("max")
    NumberProvider bettertrims$max();
}
