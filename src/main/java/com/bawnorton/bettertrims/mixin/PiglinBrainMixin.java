package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
    @ModifyReturnValue(method = "wearsGoldArmor", at = @At("RETURN"))
    private static boolean checkPlayerTrims(boolean original, LivingEntity entity) {
        if (ConfigManager.getConfig().netherBrickEffects.piglinsEnrage) {
            NumberWrapper netherBrickTrimCount = NumberWrapper.zero();
            ArmorTrimEffects.NETHER_BRICK.apply(((LivingEntityExtender) entity).betterTrims$getTrimmables(), () -> netherBrickTrimCount.increment(1));
            if (netherBrickTrimCount.getInt() >= ConfigManager.getConfig().netherBrickEffects.piecesForPiglinsEnrage)
                return false;
        }
        if (original) return true;

        if (ConfigManager.getConfig().goldEffects.piglinsIgnore) {
            NumberWrapper goldTrimCount = NumberWrapper.zero();
            ArmorTrimEffects.GOLD.apply(((LivingEntityExtender) entity).betterTrims$getTrimmables(), () -> goldTrimCount.increment(1));
            return goldTrimCount.getInt() >= ConfigManager.getConfig().goldEffects.piecesForPiglinsIgnore;
        }
        return false;
    }
}
