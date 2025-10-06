package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
abstract class BuiltInRegistriesMixin {
	@Inject(
			method = "createContents",
			at = @At("TAIL")
	)
	private static void createBetterTrimsContents(CallbackInfo ci) {
		BetterTrimsRegistries.createContents();
	}
}
