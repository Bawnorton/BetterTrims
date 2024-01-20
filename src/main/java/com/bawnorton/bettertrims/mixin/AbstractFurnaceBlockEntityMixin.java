package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.config.ConfigManager;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.mixin.accessor.AbstractFurnaceBlockEntityAccessor;
import com.bawnorton.bettertrims.util.EquippedStack;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.ArrayList;
import java.util.Collection;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {
    @SuppressWarnings({"MixinAnnotationTarget", "InvalidInjectorMethodSignature"}) // shut up mcdev, you're clueless
    @ModifyExpressionValue(method = "tick", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, ordinal = 0), slice = @Slice(from = @At(value = "INVOKE", target = "net/minecraft/block/entity/AbstractFurnaceBlockEntity.canAcceptRecipeOutput(Lnet/minecraft/registry/DynamicRegistryManager;Lnet/minecraft/recipe/Recipe;Lnet/minecraft/util/collection/DefaultedList;I)Z", ordinal = 1)))
    private static int increaseCookTime(int original, World world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity) {
        boolean nearbyCoalTrim = world.getEntitiesByClass(PlayerEntity.class, state.getCollisionShape(world, pos)
                                                                                   .getBoundingBox()
                                                                                   .offset(pos)
                                                                                   .expand(ConfigManager.getConfig().coalEffects.playerDetectionRadius), player -> true)
                                      .stream()
                                      .map(player -> ((LivingEntityExtender) player).betterTrims$getTrimmables())
                                      .flatMap(iterable -> {
                                          Collection<EquippedStack> list = new ArrayList<>();
                                          for (EquippedStack stack : iterable) {
                                              if (ArmorTrimEffects.COAL.appliesTo(stack)) list.add(stack);
                                          }
                                          return list.stream();
                                      })
                                      .findAny()
                                      .isPresent();

        if (nearbyCoalTrim) {
            return Math.min(original + ConfigManager.getConfig().coalEffects.furnaceSpeedIncrease, ((AbstractFurnaceBlockEntityAccessor) blockEntity).getCookTimeTotal() - 1);
        }
        return original;
    }
}
