package com.bawnorton.bettertrims.mixin.property.ability.incoming_damage;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimValueAbilityRunner;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
	LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@ModifyVariable(
			method = "getDamageAfterMagicAbsorb",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z"
			),
			argsOnly = true
	)
	private float applyTrimToDamage(float original, DamageSource source) {
		if (!(level() instanceof ServerLevel level)) return original;

		for (TrimProperty property : TrimProperties.getProperties(level)) {
			for (TrimValueAbilityRunner<?> ability : property.getValueAbilityRunners(TrimAbilityComponents.INCOMING_DAMAGE)) {
				original = ability.runDamage(level, (LivingEntity) (Object) this, source, original);
			}
		}
		return original;
	}
}
