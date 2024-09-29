package com.bawnorton.bettertrims.mixin.attributes.dense;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract World getWorld();

    @ModifyReturnValue(
            method = "isTouchingWater",
            at = @At("RETURN")
    )
    protected boolean applyDense(boolean original) {
        return original;
    }
}
