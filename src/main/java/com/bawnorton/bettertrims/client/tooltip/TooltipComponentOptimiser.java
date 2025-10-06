package com.bawnorton.bettertrims.client.tooltip;

import com.bawnorton.bettertrims.client.mixin.accessor.ClientTextTooltipAccessor;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeComponent;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.component.ConditionalComponent;
import com.bawnorton.bettertrims.client.tooltip.component.CyclingComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


public final class TooltipComponentOptimiser {
	private static final CompositeContainerComponent EMPTY = CompositeContainerComponent.builder().build();

	private static final List<Pass> PASSES = List.of(
			TooltipComponentOptimiser::flatten,
			TooltipComponentOptimiser::flattenHorizontal,
			TooltipComponentOptimiser::mergeTextTooltips
	);

	private TooltipComponentOptimiser() {
	}

	public static ClientTooltipComponent optimise(CompositeComponent component) {
		if (component == null || component.getComponents().isEmpty()) return EMPTY;

		ClientTooltipComponent current = component;
		for (int i = 0; i < 2; i++) {
			for (Pass pass : PASSES) {
				current = pass.apply(current);
			}
		}
		return current;
	}

	private static ClientTooltipComponent flatten(ClientTooltipComponent component) {
		if (!(component instanceof CompositeComponent composite)) {
			if (component instanceof ConditionalComponent(
					ClientTooltipComponent ifTrue, ClientTooltipComponent ifFalse, Supplier<Boolean> checker
			)) {
				return new ConditionalComponent(flatten(ifTrue), flatten(ifFalse), checker);
			}
			return component;
		}

		if (composite instanceof CyclingComponent cycling) {
			return forwardPassToCycling(cycling, TooltipComponentOptimiser::flatten);
		}

		CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();

		if (composite instanceof CompositeContainerComponent container) {
			if (container.getComponents().size() == 1) {
				ClientTooltipComponent single = container.getComponents().getFirst();
				return flatten(single);
			}
			if (container instanceof CompositeContainerComponent.Vertical) builder.vertical();
			if (container.isCentred()) builder.centred();
		}

		for (ClientTooltipComponent child : composite.getComponents()) {
			switch (child) {
				case ConditionalComponent(
						ClientTooltipComponent ifTrue, ClientTooltipComponent ifFalse, Supplier<Boolean> checker
				) -> builder.component(new ConditionalComponent(flatten(ifTrue), flatten(ifFalse), checker));
				case CompositeComponent cc -> builder.component(flatten(cc));
				default -> builder.component(flatten(child));
			}
		}

		return builder.build();
	}

	private static ClientTooltipComponent mergeTextTooltips(ClientTooltipComponent component) {
		if (!(component instanceof CompositeComponent composite)) {
			if (component instanceof ConditionalComponent(
					ClientTooltipComponent ifTrue, ClientTooltipComponent ifFalse, Supplier<Boolean> checker
			)) {
				return new ConditionalComponent(mergeTextTooltips(ifTrue), mergeTextTooltips(ifFalse), checker);
			}
			return component;
		}

		if (composite instanceof CyclingComponent cycling) {
			return forwardPassToCycling(cycling, TooltipComponentOptimiser::mergeTextTooltips);
		}

		CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
		if (composite instanceof CompositeContainerComponent container) {
			if (container instanceof CompositeContainerComponent.Vertical) builder.vertical();
			if (container.isCentred()) builder.centred();
		}

		TextMerger merger = new TextMerger(composite instanceof CompositeContainerComponent.Vertical);
		for (ClientTooltipComponent child : composite.getComponents()) {
			switch (child) {
				case ClientTextTooltip text -> merger.add(text);
				case ConditionalComponent(
						ClientTooltipComponent ifTrue, ClientTooltipComponent ifFalse, Supplier<Boolean> checker
				) -> {
					merger.flush(builder);
					builder.component(new ConditionalComponent(mergeTextTooltips(ifTrue), mergeTextTooltips(ifFalse), checker));
				}
				default -> {
					merger.flush(builder);
					builder.component(mergeTextTooltips(child));
				}
			}
		}

		merger.flush(builder);
		return builder.build();
	}

	private static ClientTooltipComponent flattenHorizontal(ClientTooltipComponent component) {
		if (!(component instanceof CompositeComponent composite)) {
			if (component instanceof ConditionalComponent(
					ClientTooltipComponent ifTrue, ClientTooltipComponent ifFalse, Supplier<Boolean> checker
			)) {
				return new ConditionalComponent(flattenHorizontal(ifTrue), flattenHorizontal(ifFalse), checker);
			}
			return component;
		}

		if (composite instanceof CyclingComponent cycling) {
			return forwardPassToCycling(cycling, TooltipComponentOptimiser::flattenHorizontal);
		}

		if (composite instanceof CompositeContainerComponent container) {
			CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();

			if (container instanceof CompositeContainerComponent.Vertical) {
				builder.vertical();
			} else if (container instanceof CompositeContainerComponent.Horizontal) {
				builder.horizontal();
			}
			if (container.isCentred()) builder.centred();

			for (ClientTooltipComponent child : container.getComponents()) {
				ClientTooltipComponent flattenedChild = flattenHorizontal(child);

				if (container instanceof CompositeContainerComponent.Horizontal && flattenedChild instanceof CompositeContainerComponent.Horizontal nested) {
					for (ClientTooltipComponent grandChild : nested.getComponents()) {
						builder.component(grandChild);
					}
				} else {
					builder.component(flattenedChild);
				}
			}

			return builder.build();
		}

		CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
		for (ClientTooltipComponent child : composite.getComponents()) {
			builder.component(flattenHorizontal(child));
		}
		return builder.build();
	}

	private static CyclingComponent forwardPassToCycling(CyclingComponent cycling, Pass pass) {
		CyclingComponent.Builder cyclingBuilder = CyclingComponent.builder();
		for (ClientTooltipComponent child : cycling.getComponents()) {
			cyclingBuilder.component(pass.apply(child));
		}
		return cyclingBuilder.build();
	}

	private static class TextMerger {
		private final List<ClientTextTooltip> buffer = new ArrayList<>();
		private final boolean isVertical;

		public TextMerger(boolean isVertical) {
			this.isVertical = isVertical;
		}

		void add(ClientTextTooltip text) {
			buffer.add(text);
		}

		void flush(CompositeContainerComponent.Builder builder) {
			if (buffer.isEmpty()) return;
			if (buffer.size() == 1) {
				builder.component(buffer.getFirst());
			} else if (!isVertical) {
				builder.component(mergeAll(buffer));
			} else {
				buffer.forEach(builder::component);
			}
			buffer.clear();
		}

		private ClientTextTooltip mergeAll(List<ClientTextTooltip> tooltips) {
			MutableComponent combined = Component.empty();
			var ref = new Object() {
				Style currentStyle = null;
			};
			StringBuilder sb = new StringBuilder();

			FormattedCharSink sink = (index, style, codepoint) -> {
				if (ref.currentStyle == null) {
					ref.currentStyle = style;
				}
				if (!style.equals(ref.currentStyle)) {
					if (!sb.isEmpty()) {
						combined.append(Component.literal(sb.toString()).withStyle(ref.currentStyle));
						sb.setLength(0);
					}
					ref.currentStyle = style;
				}
				sb.appendCodePoint(codepoint);
				return true;
			};

			for (ClientTextTooltip t : tooltips) {
				((ClientTextTooltipAccessor) t).bettertrims$text().accept(sink);
			}

			if (!sb.isEmpty()) {
				combined.append(Component.literal(sb.toString()).withStyle(ref.currentStyle));
			}

			return new ClientTextTooltip(combined.getVisualOrderText());
		}
	}

	interface Pass {
		ClientTooltipComponent apply(ClientTooltipComponent component);
	}
}