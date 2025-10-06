//? if <1.21.8 {
/*package com.bawnorton.bettertrims.client.mixin.accessor;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.world.item.alchemy.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinEnvironment("client")
@Mixin(Potion.class)
public interface PotionAccessor {
	@Accessor("name")
	String bettertrims$name();
}
*///?}