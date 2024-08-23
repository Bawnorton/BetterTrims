package com.bawnorton.bettertrims.effect.component;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;
import java.util.function.UnaryOperator;

public class TrimComponentTypes {
    public static final ComponentType<Integer> USED_BLESSINGS = register(
            "used_blessings", builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT)
    );

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                BetterTrims.id(id),
                builderOperator.apply(ComponentType.builder()).build()
        );
    }

    public static void init() {
        //no-op
    }
}
