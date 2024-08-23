package com.bawnorton.bettertrims.mixin.accessor;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerEntity.class)
public interface PlayerEntityAccessor {
    @Accessor
    void setEnchantmentTableSeed(int enchantmentTableSeed);
}
