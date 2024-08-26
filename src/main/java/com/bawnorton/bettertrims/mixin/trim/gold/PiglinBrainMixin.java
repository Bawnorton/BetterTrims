package com.bawnorton.bettertrims.mixin.trim.gold;

import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.util.ArrayList;
import java.util.List;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
    @ModifyReturnValue(
            method = "wearsGoldArmor",
            at = @At("RETURN")
    )
    private static boolean orWearsGoldTrim(boolean original, LivingEntity entity) {
        if (original) return true;

        List<RegistryEntry<ArmorTrimMaterial>> wornMaterials = new ArrayList<>();
        entity.getArmorItems().forEach(itemStack -> wornMaterials.add(itemStack.get(DataComponentTypes.TRIM).getMaterial()));
        return wornMaterials.stream().anyMatch(TrimEffects.GOLD::matchesMaterial);
    }
}