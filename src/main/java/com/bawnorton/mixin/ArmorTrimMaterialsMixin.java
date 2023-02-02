package com.bawnorton.mixin;

import com.bawnorton.material.CustomArmorTrimMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(ArmorTrimMaterials.class)
public abstract class ArmorTrimMaterialsMixin {

    @Shadow private static void method_48578(Registerable<ArmorTrimMaterial> registerable, RegistryKey<ArmorTrimMaterial> registryKey, Item item, Style style, float f) {}
    @Shadow private static RegistryKey<ArmorTrimMaterial> of(String id) {
        return null;
    }

    @Inject(method = "oneTwentyBootstrap", at = @At("HEAD"))
    private static void oneTwentyBootstrap(Registerable<ArmorTrimMaterial> registry, CallbackInfo ci) {
        CustomArmorTrimMaterials.PRISMARINE = of("prismarine_shard");
        CustomArmorTrimMaterials.ECHO_SHARD = of("echo_shard");

        method_48578(registry, CustomArmorTrimMaterials.PRISMARINE, Items.PRISMARINE_SHARD, Style.EMPTY.withColor(4288151), 1.1F);
        method_48578(registry, CustomArmorTrimMaterials.ECHO_SHARD, Items.ECHO_SHARD, Style.EMPTY.withColor(6445145), 1.2F);
    }
}
