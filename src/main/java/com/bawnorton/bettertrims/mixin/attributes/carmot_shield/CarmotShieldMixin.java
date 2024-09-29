package com.bawnorton.bettertrims.mixin.attributes.carmot_shield;

//? if <1.21 {
/*import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.PlayerEntity;
import nourl.mythicmetals.armor.CarmotShield;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(CarmotShield.class)
public abstract class CarmotShieldMixin {
    @Shadow
    @Final
    private PlayerEntity player;

    @ModifyReturnValue(
            method = "getMaxHealth",
            at = @At("RETURN"),
            remap = false
    )
    private float applyCarmotShield(float original) {
        if(TrimEntityAttributes.CARMOT_SHIELD.isUsingAlias()) return original;

        if(player.getAttributeValue(TrimEntityAttributes.CARMOT_SHIELD.get()) >= 0) {
            return original + (float) player.getAttributeValue(TrimEntityAttributes.CARMOT_SHIELD.get());
        }
        return original;
    }
}
*///?}
