package com.bawnorton.bettertrims.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
abstract class EntityMixin {
	@WrapMethod(
			method = "isInvisible"
	)
	protected boolean isTrulyInvisible(Operation<Boolean> original) {
		return original.call();
	}
}
