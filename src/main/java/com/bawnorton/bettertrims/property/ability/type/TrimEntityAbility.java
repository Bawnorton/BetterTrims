package com.bawnorton.bettertrims.property.ability.type;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.property.AllOf;
import com.bawnorton.bettertrims.property.ability.type.entity.*;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.element.TrimElement;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface TrimEntityAbility extends TrimElement {
	Codec<TrimEntityAbility> CODEC = BetterTrimsRegistries.TRIM_ENTITY_ABILITY_TYPE
			.byNameCodec()
			.dispatch(TrimEntityAbility::codec, Function.identity());

	static MapCodec<? extends TrimEntityAbility> bootstrap(Registry<MapCodec<? extends TrimEntityAbility>> registry) {
		Registry.register(registry, BetterTrims.rl("all_of"), AllOf.EntityAbilities.CODEC);
		Registry.register(registry, BetterTrims.rl("apply_mob_effect"), ApplyMobEffectAbility.CODEC);
		Registry.register(registry, BetterTrims.rl("change_item_damage"), ChangeItemDamageAbility.CODEC);
		Registry.register(registry, BetterTrims.rl("damage_entity"), DamageEntityAbility.CODEC);
		Registry.register(registry, BetterTrims.rl("explode"), ExplodeAbility.CODEC);
		Registry.register(registry, BetterTrims.rl("ignite"), IgniteAbility.CODEC);
		Registry.register(registry, BetterTrims.rl("play_sound"), PlaySoundAbility.CODEC);
		Registry.register(registry, BetterTrims.rl("replace_block"), ReplaceBlockAbility.CODEC);
		Registry.register(registry, BetterTrims.rl("replace_disk"), ReplaceDiskAbility.CODEC);
		Registry.register(registry, BetterTrims.rl("run_function"), RunFunctionAbility.CODEC);
		Registry.register(registry, BetterTrims.rl("set_block_properties"), SetBlockPropertiesAbility.CODEC);
		Registry.register(registry, BetterTrims.rl("spawn_particles"), SpawnParticlesAbility.CODEC);
		return Registry.register(registry, BetterTrims.rl("summon_entity"), SummonEntityAbility.CODEC);
	}

	void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin);

	MapCodec<? extends TrimEntityAbility> codec();
}
