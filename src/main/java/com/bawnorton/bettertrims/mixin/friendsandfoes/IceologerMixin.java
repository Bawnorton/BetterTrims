package com.bawnorton.bettertrims.mixin.friendsandfoes;

import com.bawnorton.bettertrims.annotation.ConditionalMixin;
import com.bawnorton.bettertrims.annotation.MultiConditionMixin;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.mixin.LivingEntityMixin;
import com.faboslav.friendsandfoes.entity.IceologerEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(IceologerEntity.class)
@MultiConditionMixin(
        conditions = {
                @ConditionalMixin(modid = "friendsandfoes"),
                @ConditionalMixin(modid = "illagerinvasion")
        }
)
public abstract class IceologerMixin extends LivingEntityMixin {
    @Override
    protected boolean shouldTargetTrimmedPlayer(boolean original, LivingEntity target) {
        return super.shouldTargetTrimmedPlayer(original, target) && !ArmorTrimEffects.PLATINUM.appliesTo(((EntityExtender) target).betterTrims$getTrimmables());
    }
}
