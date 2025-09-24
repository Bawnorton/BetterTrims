package com.bawnorton.bettertrims.property.item.type;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.property.AllOf;
import com.bawnorton.bettertrims.property.element.TrimElement;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import java.util.function.Function;

public interface TrimItemProperty extends TrimElement {
    Codec<TrimItemProperty> CODEC = BetterTrimsRegistries.TRIM_ITEM_PROPERTY_TYPES.byNameCodec().dispatch(TrimItemProperty::codec, Function.identity());

    MapCodec<? extends TrimItemProperty> codec();

    static MapCodec<? extends TrimItemProperty> bootstrap(Registry<MapCodec<? extends TrimItemProperty>> registry) {
        return Registry.register(registry, BetterTrims.rl("damage_resistant"), AllOf.ItemProperties.CODEC);
    }
}
