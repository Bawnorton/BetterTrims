package com.bawnorton.bettertrims.mixin.attributes;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.bettertrims.util.Aliasable;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.util.List;
import java.util.function.Consumer;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyReturnValue(
            method = "createPlayerAttributes",
            at = @At("RETURN")
    )
    private static DefaultAttributeContainer.Builder addTrimAttributes(DefaultAttributeContainer.Builder original) {
        original.add(TrimEntityAttributes.BLAST_MINING);
        original.add(TrimEntityAttributes.BONUS_XP);
        original.add(TrimEntityAttributes.ENCHANTERS_FAVOUR);
        original.add(TrimEntityAttributes.FORTUNE);
        original.add(TrimEntityAttributes.LOOTING);
        original.add(TrimEntityAttributes.MINERS_RUSH);
        original.add(TrimEntityAttributes.TRADE_DISCOUNT);
        original.add(TrimEntityAttributes.WARRIORS_OF_OLD);

        //? if <1.21 {
        /*original.add(TrimEntityAttributes.PLAYER_BLOCK_BREAK_SPEED);
        original.add(TrimEntityAttributes.PLAYER_SUBMERGED_MINING_SPEED);
        *///?}

        return original;
    }

    @Override
    //? if >=1.21 {
    public void bettertrims$addLateAttributes(Consumer<Aliasable<RegistryEntry<EntityAttribute>>> adder) {
    //?} else {
    /*public void bettertrims$addLateAttributes(Consumer<Aliasable<EntityAttribute>> adder) {
    *///?}
        super.bettertrims$addLateAttributes(adder);
        adder.accept(TrimEntityAttributes.ELYTRA_ROCKET_SPEED);
        adder.accept(TrimEntityAttributes.LAVA_VISIBILITY);
    }
}
