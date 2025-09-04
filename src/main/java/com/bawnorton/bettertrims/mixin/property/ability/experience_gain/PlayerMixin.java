package com.bawnorton.bettertrims.mixin.property.ability.experience_gain;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityRunner;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import java.util.List;

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
        for(TrimProperty property : TrimProperties.getProperties(level())) {
            List<TrimAbilityRunner> abilities = property.abilityHolders();
            for(TrimAbilityRunner ability : abilities) {
                original = ability.modifyGainedExperience(this, original);
            }
        }
        return original;
    }
}
