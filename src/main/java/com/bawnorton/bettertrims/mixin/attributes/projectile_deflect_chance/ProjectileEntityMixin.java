package com.bawnorton.bettertrims.mixin.attributes.projectile_deflect_chance;

import com.bawnorton.bettertrims.extend.ProjectileEntityExtender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin extends Entity implements ProjectileEntityExtender {
    @Unique
    private boolean bettertrims$deflected;

    public ProjectileEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void bettertrims$setDeflected(boolean deflected) {
        bettertrims$deflected = deflected;
    }

    @Override
    public boolean bettertrims$getDeflected() {
        return bettertrims$deflected;
    }
}
