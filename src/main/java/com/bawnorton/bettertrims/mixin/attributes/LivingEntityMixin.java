package com.bawnorton.bettertrims.mixin.attributes;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.event.PreRegistryFreezeCallback;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.registry.TrimRegistries;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;

//? if >=1.21
/*import net.minecraft.component.DataComponentTypes;*/

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityExtender {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    //$ attribute_shadow
    @Shadow public abstract double getAttributeValue(EntityAttribute attribute);

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
        original.add(TrimEntityAttributes.ATTACK_DEFLECT_CHANCE);
        original.add(TrimEntityAttributes.BLAST_RESISTANCE);
        original.add(TrimEntityAttributes.BOUNCY);
        original.add(TrimEntityAttributes.BREWERS_DREAM);
        original.add(TrimEntityAttributes.CLEAVING);
        original.add(TrimEntityAttributes.DENSE);
        original.add(TrimEntityAttributes.DODGE_CHANCE);
        original.add(TrimEntityAttributes.ECHOING);
        original.add(TrimEntityAttributes.ELECTRIFYING);
        original.add(TrimEntityAttributes.ENDS_BLESSING);
        original.add(TrimEntityAttributes.FIREY_THORNS);
        original.add(TrimEntityAttributes.FIRE_ASPECT);
        original.add(TrimEntityAttributes.FIRE_RESISTANCE);
        original.add(TrimEntityAttributes.GLOWING);
        original.add(TrimEntityAttributes.HELLS_BLESSING);
        original.add(TrimEntityAttributes.HOLY);
        original.add(TrimEntityAttributes.HYDROPHOBIC);
        original.add(TrimEntityAttributes.ITEM_MAGNET);
        original.add(TrimEntityAttributes.LIGHT_FOOTED);
        original.add(TrimEntityAttributes.MIDAS_TOUCH);
        original.add(TrimEntityAttributes.MOONS_BLESSING);
        original.add(TrimEntityAttributes.OVERGROWN);
        original.add(TrimEntityAttributes.PROJECTILE_DAMAGE);
        original.add(TrimEntityAttributes.PROJECTILE_DEFLECT_CHANCE);
        original.add(TrimEntityAttributes.PROJECTILE_DODGE_CHANCE);
        original.add(TrimEntityAttributes.PROJECTILE_SPEED);
        original.add(TrimEntityAttributes.REGENERATION);
        original.add(TrimEntityAttributes.RESISTANCE);
        original.add(TrimEntityAttributes.SHARE_EFFECT_RADIUS);
        original.add(TrimEntityAttributes.SUNS_BLESSING);
        original.add(TrimEntityAttributes.SWIM_SPEED);
        original.add(TrimEntityAttributes.THORNS);
        original.add(TrimEntityAttributes.WALKING_FURNACE);
        original.add(TrimEntityAttributes.UNBREAKING);

        PreRegistryFreezeCallback.POST.register(registry -> {
            if (registry != Registries.ATTRIBUTE) {
                return;
            }

            Registries.ENTITY_TYPE.stream()
                    .forEach(entityType -> TrimEntityAttributes.lateAddAttributes(
                            entityType,
                            TrimEntityAttributes.CARMOT_SHIELD,
                            TrimEntityAttributes.LAVA_MOVEMENT_SPEED,
                            TrimEntityAttributes.LAVA_VISIBILITY
                    ));
        });

        //? if <1.21 {
        original.add(TrimEntityAttributes.GENERIC_STEP_HEIGHT);
        original.add(TrimEntityAttributes.GENERIC_OXYGEN_BONUS);
        //?}
        return original;
    }
 
    @Inject(
            method = "writeCustomDataToNbt",
            at = @At("TAIL")
    )
    private void writeEffectData(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound betterTrimsContainer = new NbtCompound();
        nbt.put(BetterTrims.MOD_ID, betterTrimsContainer);
        TrimRegistries.TRIM_EFFECTS.forEach(trimEffect -> {
            NbtCompound compound = trimEffect.writeNbt((LivingEntity) (Object) this, new NbtCompound());
            if(!compound.isEmpty()) {
                betterTrimsContainer.put(trimEffect.getId().toString(), compound);
            }
        });
    }
    
    @Inject(
            method = "readCustomDataFromNbt",
            at = @At("TAIL")
    )
    private void readEffectData(NbtCompound nbt, CallbackInfo ci) {
        if(!nbt.contains(BetterTrims.MOD_ID)) return;
        
        NbtCompound betterTrimsContainer = nbt.getCompound(BetterTrims.MOD_ID);
        TrimRegistries.TRIM_EFFECTS.forEach(trimEffect -> {
            String id = trimEffect.getId().toString();
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
        if (original && bettertrims$didAvoidDamage()) {
            bettertrims$setAvoidedDamage(false);
            return false;
        }
        return true;
    }

    @ModifyReturnValue(
            method = "damage",
            at = @At("RETURN")
    )
    private boolean updateSuccess(boolean original) {
        return original && !bettertrims$didAvoidDamage();
    }

    @Override
    public List<RegistryEntry<ArmorTrimMaterial>> bettertrims$getWornMaterials() {
        List<RegistryEntry<ArmorTrimMaterial>> wornMaterials = new ArrayList<>();
        World world = getWorld();
        getArmorItems().forEach(stack -> {
            ArmorTrim trim = /*$ trim_getter >>*/ ArmorTrim.getTrim(world.getRegistryManager(),stack).orElse(null);
            if(trim == null) return;

            wornMaterials.add(trim.getMaterial());
        });
        return wornMaterials;
    }
}
