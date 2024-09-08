package com.bawnorton.bettertrims.mixin.attributes.walking_furnace;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Unique
    private RecipeManager.MatchGetter<SingleStackRecipeInput, SmeltingRecipe> bettertrims$matchGetter;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract void equipStack(EquipmentSlot slot, ItemStack stack);

    @Inject(
            method = "applyArmorToDamage",
            at = @At("RETURN")
    )
    private void applyWalkingFurnace(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (!(source.isIn(DamageTypeTags.IS_FIRE))) return;

        int walkingFurnaceLevel = (int) getAttributeValue(TrimEntityAttributes.WALKING_FURNACE);
        World world = getWorld();

        for(int i = 0; i < walkingFurnaceLevel * AttributeSettings.WalkingFurnace.itemsToSmelt; i++) {
            if((Object) this instanceof PlayerEntity player) {
                PlayerInventory inventory = player.getInventory();
                for(int j = 0; j < inventory.main.size(); j++) {
                    ItemStack stack = inventory.main.get(j);
                    if(stack.isEmpty()) continue;

                    Optional<ItemStack> smelted = bettertrims$smelt(world, stack);
                    if(smelted.isEmpty()) continue;

                    ItemStack smeltedStack = smelted.orElseThrow();
                    stack.setCount(stack.getCount() - 1);
                    if(!inventory.insertStack(smeltedStack)) {
                        player.dropStack(smeltedStack);
                    }
                    if(player instanceof ServerPlayerEntity serverPlayer) {
                        TrimCriteria.WALKING_FURNACE_SMELTED.trigger(serverPlayer);
                    }
                    break;
                }
            } else {
                EquipmentSlot slot = BetterTrims.PROBABILITIES.pickRandom(EquipmentSlot.class);
                ItemStack equipped = getEquippedStack(slot);
                if(equipped.isEmpty()) continue;

                Optional<ItemStack> smelted = bettertrims$smelt(world, equipped);
                if(smelted.isEmpty()) continue;

                ItemStack smeltedStack = smelted.orElseThrow();
                if(equipped.getCount() == 1) {
                    equipStack(slot, smeltedStack);
                } else {
                    equipped.setCount(equipped.getCount() - 1);
                    dropStack(smeltedStack);
                }
            }
        }
    }

    @Unique
    private Optional<ItemStack> bettertrims$smelt(World world, ItemStack stack) {
        if(bettertrims$matchGetter == null) {
            bettertrims$matchGetter = RecipeManager.createCachedMatchGetter(RecipeType.SMELTING);
        }
        boolean oreBlock = stack.isIn(ConventionalItemTags.ORES);
        boolean oreMaterial = stack.isIn(ConventionalItemTags.RAW_MATERIALS);
        if(!(oreBlock || oreMaterial)) return Optional.empty();

        RegistryWrapper.WrapperLookup lookup = world.getRegistryManager().toImmutable();
        Optional<RecipeEntry<SmeltingRecipe>> recipe = bettertrims$matchGetter.getFirstMatch(new SingleStackRecipeInput(stack), world);
        return recipe.map(recipeEntry -> recipeEntry.value().getResult(lookup).copy());
    }
}
