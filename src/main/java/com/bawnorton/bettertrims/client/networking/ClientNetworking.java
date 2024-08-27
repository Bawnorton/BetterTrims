package com.bawnorton.bettertrims.client.networking;

import com.bawnorton.bettertrims.effect.EchoShardTrimEffect;
import com.bawnorton.bettertrims.extend.ModifiedTimeHolder;
import com.bawnorton.bettertrims.networking.packet.s2c.EchoTriggeredS2CPacket;
import com.bawnorton.bettertrims.networking.packet.s2c.EntityEchoedS2CPacket;
import com.bawnorton.bettertrims.networking.packet.s2c.StatusEffectDurationModifiedS2CPacket;
import com.bawnorton.bettertrims.registry.content.TrimSoundEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public final class ClientNetworking {
    public static void init() {
        PayloadTypeRegistry<RegistryByteBuf> playS2C = PayloadTypeRegistry.playS2C();
        playS2C.register(StatusEffectDurationModifiedS2CPacket.PACKET_ID, StatusEffectDurationModifiedS2CPacket.PACKET_CODEC);
        playS2C.register(EchoTriggeredS2CPacket.PACKET_ID, EchoTriggeredS2CPacket.PACKET_CODEC);
        playS2C.register(EntityEchoedS2CPacket.PACKET_ID, EntityEchoedS2CPacket.PACKET_CODEC);

        ClientPlayNetworking.registerGlobalReceiver(StatusEffectDurationModifiedS2CPacket.PACKET_ID, ClientNetworking::handleStatusEffectDurationModified);
        ClientPlayNetworking.registerGlobalReceiver(EchoTriggeredS2CPacket.PACKET_ID, ClientNetworking::handleEchoTriggered);
        ClientPlayNetworking.registerGlobalReceiver(EntityEchoedS2CPacket.PACKET_ID, ClientNetworking::handleEntityEchoed);
    }

    private static void handleStatusEffectDurationModified(StatusEffectDurationModifiedS2CPacket packet, ClientPlayNetworking.Context context) {
        ClientPlayerEntity player = context.player();
        StatusEffectInstance effectInstance = player.getStatusEffect(packet.effect());
        if(effectInstance == null) return;

        ((ModifiedTimeHolder) effectInstance).bettertrims$setModifiedTime(packet.modifiedTime());
    }

    private static void handleEchoTriggered(EchoTriggeredS2CPacket packet, ClientPlayNetworking.Context context) {
        EchoShardTrimEffect.Echo echo = packet.echo();
        ClientPlayerEntity player = context.player();
        Vec3d pos = echo.pos();
        player.updatePositionAndAngles(pos.x, pos.y, pos.z, echo.yaw(), echo.pitch());
        ClientWorld world = context.client().world;
        for(int i = 5; i > 0; i--) {
            float pitch = i * 0.1f;
            CompletableFuture.delayedExecutor(50L * i, TimeUnit.MILLISECONDS).execute(() -> context.client().execute(() -> world.playSound(player, BlockPos.ofFloored(pos), TrimSoundEvents.ECHO_REWIND, SoundCategory.PLAYERS, 2f, pitch)));
        }
    }

    private static void handleEntityEchoed(EntityEchoedS2CPacket packet, ClientPlayNetworking.Context context) {
        EchoShardTrimEffect.Echo echo = packet.echo();
        ClientWorld world = context.client().world;
        if(world == null) return;

        Vec3d oldPos = packet.priorPos();
        Vec3d pos = echo.pos();
        Random random = world.getRandom();
        for(int i = 0; i < 50; i++) {
            float dx = 2 * random.nextFloat() - 1;
            float dy = 2 * random.nextFloat() - 1;
            float dz = 2 * random.nextFloat() - 1;
            world.addParticle(ParticleTypes.TRIAL_SPAWNER_DETECTION_OMINOUS, pos.x + dx, pos.y + dy, pos.z + dz, 0, 0, 0);
            world.addParticle(ParticleTypes.TRIAL_SPAWNER_DETECTION_OMINOUS, oldPos.x + dx, oldPos.y + dy, oldPos.z + dz, 0, 0, 0);
        }
    }
}
