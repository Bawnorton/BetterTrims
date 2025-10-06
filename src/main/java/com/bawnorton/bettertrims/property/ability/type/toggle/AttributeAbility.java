package com.bawnorton.bettertrims.property.ability.type.toggle;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.property.ability.type.TrimToggleAbility;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

//? if <1.21.8 {
/*import com.bawnorton.bettertrims.mixin.accessor.ItemStackAccessor;
*///?}

public record AttributeAbility(ResourceLocation id, Holder<Attribute> attribute, CountBasedValue value,
                               AttributeModifier.Operation operation) implements TrimToggleAbility {
	public static final MapCodec<AttributeAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("id").forGetter(AttributeAbility::id),
			Attribute.CODEC.fieldOf("attribute").forGetter(AttributeAbility::attribute),
			CountBasedValue.CODEC.fieldOf("value").forGetter(AttributeAbility::value),
			AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(AttributeAbility::operation)
	).apply(instance, AttributeAbility::new));

	private Multimap<Holder<Attribute>, AttributeModifier> makeAttributeMap(int count) {
		Multimap<Holder<Attribute>, AttributeModifier> multimap = HashMultimap.create();
		multimap.put(attribute, getAttributeModifier(count));
		return multimap;
	}

	private AttributeModifier getAttributeModifier(int count) {
		return new AttributeModifier(id, value.calculate(count), operation);
	}

	@Override
	public void start(ServerLevel level, LivingEntity wearer, TrimmedItems items) {
		wearer.getAttributes().addTransientAttributeModifiers(makeAttributeMap(items.size()));
	}

	@Override
	public void stop(ServerLevel level, LivingEntity wearer, TrimmedItems items) {
		wearer.getAttributes().removeAttributeModifiers(makeAttributeMap(items.size()));
	}

	@Override
	public @Nullable ClientTooltipComponent getTooltip(ClientLevel level, boolean includeCount) {
		List<Component> modifiers = new ArrayList<>();
		for (int i = 1; i <= 4; i++) {
			int count = i;
			//? if >=1.21.8 {
			ItemAttributeModifiers.Display.attributeModifiers().apply(
			 //?} else {
			/*((ItemStackAccessor) (Object) ItemStack.EMPTY).bettertrims$addModifierTooltip(
					*///?}
					component -> {
						if (includeCount) {
							component = Styler.trim(Component.literal("[%d]".formatted(count)))
									.append(": ")
									.append(component);
						}
						modifiers.add(component);
					},
					Minecraft.getInstance().player,
					attribute,
					getAttributeModifier(i)
			);
		}
		return CompositeContainerComponent.builder()
				.cycle(builder -> modifiers.forEach(builder::textComponent))
				.build();
	}

	@Override
	public boolean usesCount() {
		return true;
	}

	@Override
	public MapCodec<? extends TrimToggleAbility> codec() {
		return CODEC;
	}
}
