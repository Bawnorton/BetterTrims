package com.bawnorton.bettertrims.mixin;

import net.minecraft.command.EntityDataObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityDataObject.class)
public abstract class EntityDataObjectMixin {
    @Shadow @Final private Entity entity;

    @Inject(
            method = "setNbt",
            at = @At("HEAD"),
            cancellable = true
    )
    private void allowPlayer(NbtCompound nbt, CallbackInfo ci) {
        if(entity instanceof PlayerEntity) {
            entity.readNbt(nbt);
            ci.cancel();
        }
    }
}
