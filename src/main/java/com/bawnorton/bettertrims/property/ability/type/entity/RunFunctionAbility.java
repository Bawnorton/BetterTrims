package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.element.TrimElementTooltipProvider;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record RunFunctionAbility(ResourceLocation function, String tooltipTranslationKey) implements TrimEntityAbility {
	public static final MapCodec<RunFunctionAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("function").forGetter(RunFunctionAbility::function),
			Codec.STRING.fieldOf("tooltip_translation_key").forGetter(RunFunctionAbility::tooltipTranslationKey)
	).apply(instance, RunFunctionAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		MinecraftServer server = level.getServer();
		ServerFunctionManager functionManager = server.getFunctions();
		functionManager.get(function).ifPresentOrElse(
				commandFunction -> {
					Vec3 position = target.getPosition(1);
					Vec2 rotation = target.getRotationVector();
					CommandSourceStack stack = server.createCommandSourceStack()
							.withPermission(4)
							.withSuppressedOutput()
							.withEntity(target)
							.withLevel(level)
							.withPosition(position)
							.withRotation(rotation);
					functionManager.execute(commandFunction, stack);
				}, () -> BetterTrims.LOGGER.error("Trim bettertrims:run_function ability failed for non-existent function {}", function)
		);
	}

	@Override
	public MapCodec<? extends TrimEntityAbility> codec() {
		return CODEC;
	}

	public static class TooltipProvider implements TrimElementTooltipProvider<RunFunctionAbility> {
		@Nullable
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, RunFunctionAbility element, boolean includeCount) {
			return CompositeContainerComponent.builder()
					.translate(element.tooltipTranslationKey(), Styler::positive)
					.build();
		}
	}
}
