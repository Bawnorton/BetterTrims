//? if >=1.21.8 {
package com.bawnorton.bettertrims.mixin.property.ability.hit_block;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimEntityAbilityRunner;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractThrownPotion;
import net.minecraft.world.entity.projectile.ThrownLingeringPotion;
import net.minecraft.world.entity.projectile.ThrownSplashPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinEnvironment
@Mixin({ThrownSplashPotion.class, ThrownLingeringPotion.class})
abstract class AbstractThrownPotionMixin extends AbstractThrownPotion {
    AbstractThrownPotionMixin(EntityType<? extends AbstractThrownPotion> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "onHitAsPotion",
        at = @At("HEAD")
    )
    private void triggerTrimOnHit(ServerLevel level, ItemStack stack, HitResult hitResult, CallbackInfo ci) {
        if(!(getEffectSource() instanceof LivingEntity wearer)) return;

        if(hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            for(TrimProperty property : TrimProperties.getProperties(level)) {
                for (TrimEntityAbilityRunner<?> ability : property.getEntityAbilityRunners(TrimAbilityComponents.HIT_BLOCK)) {
                    Vec3 pos = blockHitResult.getLocation();
                    BlockState state = level.getBlockState(BlockPos.containing(pos));
                    ability.runHitBlock(level, wearer, wearer, pos, state, stack);
                }
            }
        }
    }
}
//?}