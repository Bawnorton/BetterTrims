package com.bawnorton.bettertrims.mixin.attributes.submerged_mining_speed;

//? if <1.21 {
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(
            method = "getBlockBreakingSpeed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/EnchantmentHelper;hasAquaAffinity(Lnet/minecraft/entity/LivingEntity;)Z"
            )
    )
    private boolean orHasSubmergedMiningSpeed(boolean original) {
        return original || getAttributeValue(TrimEntityAttributes.PLAYER_SUBMERGED_MINING_SPEED) >= 0;
    }

    @ModifyExpressionValue(
            method = "getBlockBreakingSpeed",
            at = @At(
                    value = "CONSTANT",
                    args = "floatValue=5.0",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/enchantment/EnchantmentHelper;hasAquaAffinity(Lnet/minecraft/entity/LivingEntity;)Z"
                    )
            )
    )
    private float applySubmergedMiningSpeed(float original) {
        double value = getAttributeValue(TrimEntityAttributes.PLAYER_SUBMERGED_MINING_SPEED);
        if(value >= 0) {
            return (float) (1 / value);
        }
        return original;
    }
}
//?}
