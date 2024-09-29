package com.bawnorton.bettertrims.mixin.attributes.cleaving;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

//? if >=1.21 {
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
//?}

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @ModifyArg(
            method = "dropLoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/loot/LootTable;generateLoot(Lnet/minecraft/loot/context/LootContextParameterSet;JLjava/util/function/Consumer;)V"
            ),
            index = 2
    )
    private Consumer<ItemStack> trackStacks(Consumer<ItemStack> original, @Share("tracked") LocalRef<List<ItemStack>> tracked) {
        List<ItemStack> dropped = new ArrayList<>();
        Consumer<ItemStack> tracking = stack -> {
            dropped.add(stack);
            original.accept(stack);
        };
        tracked.set(dropped);
        return tracking;
    }

    @Inject(
            method = "dropLoot",
            at = @At("TAIL")
    )
    private void applyCleaving(DamageSource damageSource, boolean causedByPlayer, CallbackInfo ci, @Share("tracked") LocalRef<List<ItemStack>> tracked) {
        if (!(damageSource.getAttacker() instanceof LivingEntity attacker)) return;

        double cleavingChance = attacker.getAttributeValue(TrimEntityAttributes.CLEAVING) - 1;
        if (cleavingChance <= 0) return;
        if (!BetterTrims.PROBABILITIES.passes(cleavingChance)) return;

        ItemStack skull;
        Object self = this;
        if (self instanceof WitherSkeletonEntity) {
            skull = Items.WITHER_SKELETON_SKULL.getDefaultStack();
        } else if (self instanceof SkeletonEntity) {
            skull = Items.SKELETON_SKULL.getDefaultStack();
        } else if (self instanceof ZombieEntity) {
            skull = Items.ZOMBIE_HEAD.getDefaultStack();
        } else if (self instanceof PiglinEntity) {
            if (attacker instanceof ServerPlayerEntity serverPlayer) {
                TrimCriteria.DECAPITATED_PIGLIN.trigger(serverPlayer);
            }
            skull = Items.PIGLIN_HEAD.getDefaultStack();
        } else if (self instanceof CreeperEntity) {
            skull = Items.CREEPER_HEAD.getDefaultStack();
        } else if (self instanceof EnderDragonEntity) {
            skull = Items.DRAGON_HEAD.getDefaultStack();
        } else if (self instanceof PlayerEntity player) {
            ItemStack playerHead = Items.PLAYER_HEAD.getDefaultStack();
            //? if >=1.21 {
            playerHead.set(DataComponentTypes.PROFILE, new ProfileComponent(player.getGameProfile()));
             //?} else {
            /*NbtCompound nbt = playerHead.getOrCreateNbt();
            nbt.putUuid("SkullOwner", player.getUuid());
            *///?}
            skull = playerHead;
        } else {
            skull = Items.AIR.getDefaultStack();
        }
        if(skull.isEmpty()) return;

        boolean skullExists = tracked.get()
                .stream()
                .anyMatch(stack -> stack.getItem().equals(skull.getItem()));
        if(skullExists) return;

        dropStack(skull);
    }
}
