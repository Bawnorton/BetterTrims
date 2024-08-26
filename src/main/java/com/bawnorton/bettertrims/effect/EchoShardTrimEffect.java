package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.bettertrims.registry.content.TrimStatusEffects;
import com.google.common.collect.EvictingQueue;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.Vec3d;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public final class EchoShardTrimEffect extends TrimEffect {
    private final Map<UUID, EvictingQueue<Echo>> entityEchoes;

    public EchoShardTrimEffect(TagKey<Item> materials) {
        super(materials);
        entityEchoes = new HashMap<>();
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.ECHOING));
    }

    public void createEcho(LivingEntity entity, Vec3d pos, float pitch, float yaw, float health) {
        if(entity.hasStatusEffect(TrimStatusEffects.DAMPENED)) return;

        Echo echo = new Echo(pos, pitch, yaw, health);
        int echoingLevel = (int) entity.getAttributeValue(TrimEntityAttributes.ECHOING);
        if(echoingLevel <= 0) return;

        int echoCount = echoingLevel * 100;
        EvictingQueue<Echo> queue = entityEchoes.computeIfAbsent(entity.getUuid(), uuid -> EvictingQueue.create(echoCount));
        int maxSize = queue.remainingCapacity() + queue.size();
        if(maxSize != echoCount) {
            EvictingQueue<Echo> newQueue = EvictingQueue.create(echoCount);
            newQueue.addAll(queue);
            entityEchoes.put(entity.getUuid(), newQueue);
            queue = newQueue;
        }
        queue.add(echo);
    }

    @Override
    public NbtCompound writeNbt(LivingEntity entity, NbtCompound nbt) {
        EvictingQueue<Echo> queue = entityEchoes.get(entity.getUuid());
        if(queue == null) return nbt;

        NbtList echoes = new NbtList();
        for(Echo echo : queue) {
            DataResult<NbtElement> encoded = Echo.CODEC.encodeStart(NbtOps.INSTANCE, echo);
            encoded.ifSuccess(echoes::add).ifError(error -> BetterTrims.LOGGER.error("Failed to encode echo: {}", error));
        }
        nbt.put("echoes", echoes);
        return nbt;
    }

    @Override
    public void readNbt(LivingEntity entity, NbtCompound nbt) {
        if(!nbt.contains("echoes")) return;

        NbtList echoes = nbt.getList("echoes", NbtElement.COMPOUND_TYPE);
        for (NbtElement echoElement : echoes) {
            DataResult<Echo> decoded = Echo.CODEC.parse(NbtOps.INSTANCE, echoElement);
            decoded.ifSuccess(echo -> createEcho(entity, echo.pos(), echo.pitch(), echo.yaw(), echo.health()))
                    .ifError(error -> BetterTrims.LOGGER.error("Failed to decode echo: {}", error));
        }
    }

    public EvictingQueue<Echo> getEchoes(LivingEntity instance) {
        return entityEchoes.get(instance.getUuid());
    }

    public record Echo(Vec3d pos, float pitch, float yaw, float health) {
        public static final Codec<Echo> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Vec3d.CODEC.fieldOf("pos").forGetter(Echo::pos),
                        Codec.FLOAT.fieldOf("pitch").forGetter(Echo::pitch),
                        Codec.FLOAT.fieldOf("yaw").forGetter(Echo::yaw),
                        Codec.FLOAT.fieldOf("health").forGetter(Echo::health)
                ).apply(instance, Echo::new)
        );
        public static final PacketCodec<ByteBuf, Echo> PACKET_CODEC = PacketCodec.tuple(
                PacketCodecs.VECTOR3F.xmap(Vec3d::new, Vec3d::toVector3f), Echo::pos,
                PacketCodecs.FLOAT, Echo::pitch,
                PacketCodecs.FLOAT, Echo::yaw,
                PacketCodecs.FLOAT, Echo::health,
                Echo::new
        );
    }
}