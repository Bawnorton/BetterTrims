package com.bawnorton.bettertrims.mixin.attributes;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyReturnValue(
            method = "createPlayerAttributes",
            at = @At("RETURN")
    )
    private static DefaultAttributeContainer.Builder addTrimAttributes(DefaultAttributeContainer.Builder original) {
        original.add(TrimEntityAttributes.BONUS_XP);
        original.add(TrimEntityAttributes.ENCHANTERS_FAVOUR);
        original.add(TrimEntityAttributes.FORTUNE);
        original.add(TrimEntityAttributes.MINERS_RUSH);
        original.add(TrimEntityAttributes.TRADE_DISCOUNT);
        //? if <1.21
        /*original.add(TrimEntityAttributes.PLAYER_BLOCK_BREAK_SPEED);*/
        return original;
    }

    //? if <1.21 {
    /*@ModifyReturnValue(
            method = "getBlockBreakingSpeed",
            at = @At("RETURN")
    )
    private float applyBlockBreakSpeed(float original) {
        return original * (float) getAttributeValue(TrimEntityAttributes.PLAYER_BLOCK_BREAK_SPEED);
    }
    *///?}
}
