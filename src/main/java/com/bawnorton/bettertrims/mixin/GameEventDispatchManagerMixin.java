package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventDispatchManager;
import net.minecraft.world.event.listener.GameEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(GameEventDispatchManager.class)
public abstract class GameEventDispatchManagerMixin {
    @SuppressWarnings("unused")
    @WrapWithCondition(method = "method_45492", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/event/listener/GameEventListener;listen(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/event/GameEvent;Lnet/minecraft/world/event/GameEvent$Emitter;Lnet/minecraft/util/math/Vec3d;)Z"))
    private boolean checkEchoTrimDistance(GameEventListener instance, ServerWorld world, GameEvent gameEvent, GameEvent.Emitter emitter, Vec3d emitterPos, List<GameEvent.Message> list, GameEvent gameEvent2, Vec3d emitterPos2, GameEvent.Emitter emitter2, GameEventListener listener, Vec3d listenerPos) {
        if (!(emitter.sourceEntity() instanceof EntityExtender extender)) return true;

        NumberWrapper distanceReduction = NumberWrapper.zero();
        ArmorTrimEffects.ECHO_SHARD.apply(extender.betterTrims$getTrimmables(), () -> distanceReduction.increment(ConfigManager.getConfig().echoShardVibrationDistanceReduction));
        double listenerDistance = (8 - listenerPos.distanceTo(emitterPos)) - distanceReduction.getFloat();
        return listenerDistance > 0;
    }
}
