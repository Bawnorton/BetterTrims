package com.bawnorton.bettertrims.mixin.attributes.block_break_speed;

//? if <1.21 {
/*import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReturnValue(
            method = "getBlockBreakingSpeed",
            at = @At("RETURN")
    )
    private float applyBlockBreakSpeed(float original) {
        return original * (float) getAttributeValue(TrimEntityAttributes.PLAYER_BLOCK_BREAK_SPEED);
    }
}
*///?}
