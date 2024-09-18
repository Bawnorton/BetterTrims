package com.bawnorton.bettertrims.mixin.attributes.electrifying;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract World getWorld();

    @Shadow public abstract BlockPos getBlockPos();

    @Shadow public abstract Vec3d getEyePos();

    @ModifyReturnValue(
            method = "isInvulnerableTo",
            at = @At("RETURN")
    )
    protected boolean applyElectrifyingToInvulnerability(boolean original, DamageSource source) {
        return original; // stub
    }
}
