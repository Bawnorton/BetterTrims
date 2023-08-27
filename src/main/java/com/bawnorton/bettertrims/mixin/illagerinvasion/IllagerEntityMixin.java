package com.bawnorton.bettertrims.mixin.illagerinvasion;

import com.bawnorton.bettertrims.annotation.ConditionalMixin;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.mixin.LivingEntityMixin;
import fuzs.illagerinvasion.world.entity.monster.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.mob.VindicatorEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = {
        VindicatorEntity.class,
        PillagerEntity.class,
        EvokerEntity.class,
        IllusionerEntity.class,
        Alchemist.class,
        Archivist.class,
        Basher.class,
        Firecaller.class,
        Inquisitor.class,
        Invoker.class,
        Marauder.class,
        Necromancer.class,
        Provoker.class,
        Sorcerer.class
}, priority = 1500)
@ConditionalMixin(modid = "illagerinvasion")
public abstract class IllagerEntityMixin extends LivingEntityMixin {
    @Override
    protected boolean shouldTargetTrimmedPlayer(boolean original, LivingEntity target) {
        return super.shouldTargetTrimmedPlayer(original, target) && !ArmorTrimEffects.PLATINUM.appliesTo(((EntityExtender) target).betterTrims$getTrimmables());
    }
}





