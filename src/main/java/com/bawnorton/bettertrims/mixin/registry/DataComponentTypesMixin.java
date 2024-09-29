package com.bawnorton.bettertrims.mixin.registry;

//? if >=1.21 {
import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.registry.content.TrimComponentTypes;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import java.util.function.UnaryOperator;

@Mixin(DataComponentTypes.class)
public abstract class DataComponentTypesMixin {
    static {
        TrimComponentTypes.USED_BLESSINGS = bettertrims$register(
                "used_blessings", builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT)
        );
    }

    @Unique
    private static <T> ComponentType<T> bettertrims$register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                BetterTrims.id(id),
                builderOperator.apply(ComponentType.builder()).build()
        );
    }
}
//?}