package com.bawnorton.bettertrims.mixin.property.ability.experience_gain;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimValueAbilityRunner;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@MixinEnvironment
@Mixin(Player.class)
abstract class PlayerMixin extends LivingEntity {
    PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyVariable(
        method = "giveExperiencePoints",
        at = @At("HEAD"),
        argsOnly = true
    )
    private int applyTrimToXp(int original) {
        if (!(level() instanceof ServerLevel level)) return original;

        for(TrimProperty property : TrimProperties.getProperties(level)) {
            for (TrimValueAbilityRunner<?> ability : property.getValueAbilityRunners(TrimAbilityComponents.EXPERIENCE_GAINED)) {
                original = (int) ability.runEquipment(level, this, original);
            }
        }
        return original;
    }
}
