package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record PlaySoundAbility(Holder<SoundEvent> soundEvent, FloatProvider volume, FloatProvider pitch) implements TrimEntityAbility {
    public static final MapCodec<PlaySoundAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        SoundEvent.CODEC.fieldOf("sound").forGetter(PlaySoundAbility::soundEvent),
        FloatProvider.codec(1.0E-5F, 10.0F).fieldOf("volume").forGetter(PlaySoundAbility::volume),
        FloatProvider.codec(1.0E-5F, 2.0F).fieldOf("pitch").forGetter(PlaySoundAbility::pitch)
    ).apply(instance, PlaySoundAbility::new));

    @Override
    public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
        RandomSource random = target.getRandom();
        if(target.isSilent()) return;

        level.playSound(
            null,
            origin.x(),
            origin.y(),
            origin.z(),
            soundEvent,
            target.getSoundSource(),
            volume.sample(random),
            pitch.sample(random)
        );
    }

    @Override
    public MapCodec<? extends TrimEntityAbility> codec() {
        return CODEC;
    }
}
