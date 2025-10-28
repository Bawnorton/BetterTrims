package com.bawnorton.bettertrims.client.tooltip.braid.widget;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import io.wispforest.owo.braid.framework.BuildContext;
import io.wispforest.owo.braid.framework.proxy.WidgetState;
import io.wispforest.owo.braid.framework.widget.StatefulWidget;
import io.wispforest.owo.braid.framework.widget.Widget;
import io.wispforest.owo.braid.widgets.flex.Column;
import io.wispforest.owo.braid.widgets.flex.CrossAxisAlignment;
import io.wispforest.owo.braid.widgets.flex.MainAxisAlignment;
import io.wispforest.owo.braid.widgets.flex.Row;
import io.wispforest.owo.braid.widgets.label.Label;
import io.wispforest.owo.braid.widgets.stack.Stack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;

import java.util.ArrayList;
import java.util.List;

public class TrimPropertiesRootWidget extends StatefulWidget {
	private final ItemStack stack;
	private final ArmorTrim trim;

	public TrimPropertiesRootWidget(ItemStack stack, ArmorTrim trim) {
		this.stack = stack;
		this.trim = trim;
	}

	@Override
	public WidgetState<TrimPropertiesRootWidget> createState() {
		return new State();
	}

	public static class State extends WidgetState<TrimPropertiesRootWidget> {
		private final List<TrimProperty> properties = new ArrayList<>();
		private RegistryAccess registryAccess;

		private TrimProperty currentProperty = null;

		@Override
		public void init() {
			Minecraft minecraft = Minecraft.getInstance();
			if(minecraft.level == null) return;

			registryAccess = minecraft.level.registryAccess();
			ArmorTrim trim = widget().trim;
			for(TrimProperty property : TrimProperties.getProperties(minecraft.level)) {
				if(property.matcher().matches(trim)) {
					properties.add(property);
				}
			}
			if(properties.isEmpty()) return;

			currentProperty = properties.getFirst();
		}

		@Override
		public Widget build(BuildContext context) {
			if(currentProperty == null) {
				return new Label(Component.literal("empty"));
			}

			ResourceLocation id = registryAccess.lookupOrThrow(BetterTrimsRegistries.Keys.TRIM_PROPERTIES).getKey(currentProperty);
			if(id == null) {
				return new Label(Component.literal("unknown"));
			}

			return new Stack(
					new Column(
							new Row(
									MainAxisAlignment.SPACE_BETWEEN,
									CrossAxisAlignment.CENTER,
									new Label(Component.translatable("bettertrims.property.%s.%s".formatted(id.getNamespace(), id.getPath()))),
									new NumberStepperWidget(0, properties.size() - 1, 0, newPage -> currentProperty = properties.get(newPage))
							),
							new Stack(
									new Label(Component.literal("content"))
							)
					)
			);
		}
	}
}
