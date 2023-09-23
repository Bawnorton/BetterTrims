package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElderGuardianEntity.class)
public abstract class ElderGuardianEntityMixin {
    @Inject(method = "method_7011", at = @At("HEAD"), cancellable = true)
    private void cancelElderGuardianEffectOnTrimmedPlayers(ServerPlayerEntity player, CallbackInfo ci) {
        if (player instanceof EntityExtender extender) {
            NumberWrapper count = NumberWrapper.zero();
            ArmorTrimEffects.PRISMARINE_SHARD.apply(extender.betterTrims$getTrimmables(), () -> count.increment(1));
            if (!ConfigManager.getConfig().prismarineShardEffects.miningFatigueImmunity) return;
            if (count.getInt() < ConfigManager.getConfig().prismarineShardEffects.piecesForMiningFatigueImmunity) return;

            player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
            ci.cancel();
        }
    }
}
