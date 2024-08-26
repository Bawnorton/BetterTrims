package com.bawnorton.bettertrims.mixin.attributes;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.registry.TrimRegistries;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityExtender {
    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Unique
    private boolean bettertrims$avoidedDamage;

    @Override
    public void bettertrims$setAvoidedDamage(boolean avoidDamage) {
        this.bettertrims$avoidedDamage = avoidDamage;
    }

    @Override
    public boolean bettertrims$didAvoidDamage() {
        return bettertrims$avoidedDamage;
    }

    @ModifyReturnValue(
            method = "createLivingAttributes",
            at = @At("RETURN")
    )
    private static DefaultAttributeContainer.Builder addTrimAttributes(DefaultAttributeContainer.Builder original) {
        original.add(TrimEntityAttributes.BREWERS_DREAM);
        original.add(TrimEntityAttributes.ELECTRIFYING);
        original.add(TrimEntityAttributes.SUNS_BLESSING);
        original.add(TrimEntityAttributes.ITEM_MAGNET);
        original.add(TrimEntityAttributes.FIRE_RESISTANCE);
        original.add(TrimEntityAttributes.RESISTANCE);
        original.add(TrimEntityAttributes.WALKING_FURNACE);
        original.add(TrimEntityAttributes.SHARE_EFFECT_RADIUS);
        original.add(TrimEntityAttributes.ECHOING);
        original.add(TrimEntityAttributes.DODGE_CHANCE);
        original.add(TrimEntityAttributes.REGENERATION);
        return original;
    }
 
    @Inject(
            method = "writeCustomDataToNbt",
            at = @At("TAIL")
    )
    private void writeEffectData(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound betterTrimsContainer = new NbtCompound();
        nbt.put(BetterTrims.MOD_ID, betterTrimsContainer);
        TrimRegistries.TRIM_EFFECTS.forEach(trimEffect -> betterTrimsContainer.put(trimEffect.getMaterials().id().toString(), trimEffect.writeNbt((LivingEntity) (Object) this, new NbtCompound())));
    }
    
    @Inject(
            method = "readCustomDataFromNbt",
            at = @At("TAIL")
    )
    private void readEffectData(NbtCompound nbt, CallbackInfo ci) {
        if(!nbt.contains(BetterTrims.MOD_ID)) return;
        
        NbtCompound betterTrimsContainer = nbt.getCompound(BetterTrims.MOD_ID);
        TrimRegistries.TRIM_EFFECTS.forEach(trimEffect -> {
            TagKey<Item> materials = trimEffect.getMaterials();
            String id = materials.id().toString();
            if (betterTrimsContainer.contains(id)) {
                NbtCompound effectNbt = betterTrimsContainer.getCompound(id);
                trimEffect.readNbt((LivingEntity) (Object) this, effectNbt);
            }
        });
    }

    @ModifyVariable(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/damage/DamageSource;getAttacker()Lnet/minecraft/entity/Entity;"
            ),
            ordinal = 1
    )
    private boolean updateDamageAvoidance(boolean original) {
        return original && !bettertrims$didAvoidDamage();
    }
}
