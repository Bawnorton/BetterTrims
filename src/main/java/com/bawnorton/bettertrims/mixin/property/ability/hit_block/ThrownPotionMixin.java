//? if <1.21.8 {
/*package com.bawnorton.bettertrims.mixin.property.ability.hit_block;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimEntityAbilityRunner;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownPotion.class)
abstract class ThrownPotionMixin extends ThrowableItemProjectile {
	ThrownPotionMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(
			method = "onHitBlock",
			at = @At("HEAD")
	)
	private void triggerTrimOnHit(BlockHitResult result, CallbackInfo ci) {
		if (!(getEffectSource() instanceof LivingEntity wearer)) return;
		if (!(level() instanceof ServerLevel level)) return;

		for (TrimProperty property : TrimProperties.getProperties(level)) {
			for (TrimEntityAbilityRunner<?> ability : property.getEntityAbilityRunners(TrimAbilityComponents.HIT_BLOCK)) {
				Vec3 pos = result.getLocation();
				BlockState state = level.getBlockState(BlockPos.containing(pos));
				ability.runHitBlock(level, wearer, wearer, pos, state, getItem());
			}
		}
	}
}
*///?}