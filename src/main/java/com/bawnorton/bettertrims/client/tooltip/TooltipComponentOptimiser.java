package com.bawnorton.bettertrims.client.tooltip;

import com.bawnorton.bettertrims.client.mixin.accessor.ClientTextTooltipAccessor;
import com.bawnorton.bettertrims.client.tooltip.component.*;
import net.minecraft.client.gui.Font;
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

	private static final List<Pass> OPTIMISATION_PASSES = List.of(
			TooltipComponentOptimiser::flatten,
			TooltipComponentOptimiser::flattenHorizontal,
			TooltipComponentOptimiser::flattenVertical,
			TooltipComponentOptimiser::mergeTextTooltips
	);

	private static final List<Pass> FINAL_PASSES = List.of(
			TooltipComponentOptimiser::clearHorizontalOffsets,
			TooltipComponentOptimiser::setPropertVerticalOffsets
	);

	private TooltipComponentOptimiser() {
	}

	public static ClientTooltipComponent optimise(CompositeComponent component, Font font) {
		if (component == null || component.getComponents().isEmpty()) return EMPTY;

		ClientTooltipComponent current = component;
		for (int i = 0; i < 2; i++) {
			for (Pass pass : OPTIMISATION_PASSES) {
				current = pass.apply(current, font);
			}
		}
		for (Pass pass : FINAL_PASSES) {
			current = pass.apply(current, font);
		}
		return current;
	}

	/**
	 * Flattens single-child containers and removes unnecessary nesting.
	 */
	private static ClientTooltipComponent flatten(ClientTooltipComponent component, Font font) {
		if (!(component instanceof CompositeComponent composite)) {
			return forwardPassToNonComposite(component, font, TooltipComponentOptimiser::flatten);
		}
		if (composite instanceof CyclingComponent cycling) {
			return forwardPassToCycling(cycling, font, TooltipComponentOptimiser::flatten);
		}

		CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();

		if (composite instanceof CompositeContainerComponent container) {
			if (container.getComponents().size() == 1) {
				ClientTooltipComponent single = container.getComponents().getFirst();
				return flatten(single, font);
			}
			if (container instanceof CompositeContainerComponent.Vertical) builder.vertical();
			if (container.isCentred()) builder.centred(true);
		}

		for (ClientTooltipComponent child : composite.getComponents()) {
			switch (child) {
				case ConditionalComponent(
						ClientTooltipComponent ifTrue, ClientTooltipComponent ifFalse, Supplier<Boolean> checker
				) -> builder.component(new ConditionalComponent(flatten(ifTrue, font), flatten(ifFalse, font), checker));
				case CompositeComponent cc -> builder.component(flatten(cc, font));
				default -> builder.component(flatten(child, font));
			}
		}

		return builder.build();
	}

	/**
	 * Merges sequential text tooltips into a single text tooltip.
	 */
	private static ClientTooltipComponent mergeTextTooltips(ClientTooltipComponent component, Font font) {
		if (!(component instanceof CompositeComponent composite)) {
			return forwardPassToNonComposite(component, font, TooltipComponentOptimiser::mergeTextTooltips);
		}
		if (composite instanceof CyclingComponent cycling) {
			return forwardPassToCycling(cycling, font, TooltipComponentOptimiser::mergeTextTooltips);
		}

		CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();
		if (composite instanceof CompositeContainerComponent container) {
			if (container instanceof CompositeContainerComponent.Vertical) builder.vertical();
			if (container.isCentred()) builder.centred(true);
		}

		TextMerger merger = new TextMerger(composite instanceof CompositeContainerComponent.Vertical);
		for (ClientTooltipComponent child : composite.getComponents()) {
			switch (child) {
				case ClientTextTooltip text -> merger.add(text);
				case ConditionalComponent(
						ClientTooltipComponent ifTrue, ClientTooltipComponent ifFalse, Supplier<Boolean> checker
				) -> {
					merger.flush(builder);
					builder.component(new ConditionalComponent(mergeTextTooltips(ifTrue, font), mergeTextTooltips(ifFalse, font), checker));
				}
				default -> {
					merger.flush(builder);
					builder.component(mergeTextTooltips(child, font));
				}
			}
		}

		merger.flush(builder);
		return builder.build();
	}

	/**
	 * Flattens nested horizontal containers into a single horizontal container.
	 */
	private static ClientTooltipComponent flattenHorizontal(ClientTooltipComponent component, Font font) {
		if (!(component instanceof CompositeComponent composite)) {
			return forwardPassToNonComposite(component, font, TooltipComponentOptimiser::flattenHorizontal);
		}
		if (composite instanceof CyclingComponent cycling) {
			return forwardPassToCycling(cycling, font, TooltipComponentOptimiser::flattenHorizontal);
		}

		if (composite instanceof CompositeContainerComponent container) {
			CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();

			if (container instanceof CompositeContainerComponent.Vertical) {
				builder.vertical();
			} else if (container instanceof CompositeContainerComponent.Horizontal) {
				builder.horizontal();
			}
			if (container.isCentred()) builder.centred(true);

			for (ClientTooltipComponent child : container.getComponents()) {
				ClientTooltipComponent flattenedChild = flattenHorizontal(child, font);

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
			builder.component(flattenHorizontal(child, font));
		}
		return builder.build();
	}

	/**
	 * Flattens nested vertical containers into a single vertical container.
	 */
	private static ClientTooltipComponent flattenVertical(ClientTooltipComponent component, Font font) {
		if (!(component instanceof CompositeComponent composite)) {
			return forwardPassToNonComposite(component, font, TooltipComponentOptimiser::flattenVertical);
		}
		if (composite instanceof CyclingComponent cycling) {
			return forwardPassToCycling(cycling, font, TooltipComponentOptimiser::flattenVertical);
		}

		if (composite instanceof CompositeContainerComponent container) {
			CompositeContainerComponent.Builder builder = CompositeContainerComponent.builder();

			if (container instanceof CompositeContainerComponent.Vertical) {
				builder.vertical();
			} else if (container instanceof CompositeContainerComponent.Horizontal) {
				builder.horizontal();
			}
			if (container.isCentred()) builder.centred(true);

			for (ClientTooltipComponent child : container.getComponents()) {
				ClientTooltipComponent flattenedChild = flattenVertical(child, font);

				if (container instanceof CompositeContainerComponent.Vertical && flattenedChild instanceof CompositeContainerComponent.Vertical nested) {
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
			builder.component(flattenVertical(child, font));
		}
		return builder.build();
	}


	public static ClientTooltipComponent clearHorizontalOffsets(ClientTooltipComponent root, Font font) {
		return clearHorizontalOffsets(root, font, 0, 0, 0);
	}

	private static ClientTooltipComponent clearHorizontalOffsets(
			ClientTooltipComponent component,
			Font font,
			int absX,
			int anchorAbsX,
			int verticalDepth
	) {
		if (!(component instanceof CompositeComponent composite)) {
			return forwardPassToNonComposite(component, font, (c, f) -> clearHorizontalOffsets(c, f, absX, anchorAbsX, verticalDepth));
		}
		switch (composite) {
			case CyclingComponent cycling -> {
				return forwardPassToCycling(cycling, font, (c, f) -> clearHorizontalOffsets(c, f, absX, anchorAbsX, verticalDepth));
			}
			case CompositeContainerComponent.Horizontal horizontal -> {
				int runningX = 0;
				for (ClientTooltipComponent child : horizontal.getComponents()) {
					int childAbsX = absX + runningX;
					clearChildHorizontalOffsets(font, anchorAbsX, verticalDepth, child, childAbsX);
					runningX += child.getWidth(font);
				}
				return composite;
			}
			case CompositeContainerComponent.Vertical vertical -> {
				vertical.setHorizontalOffset(absX - anchorAbsX);

				List<ClientTooltipComponent> children = vertical.getComponents();
				for (int i = 0; i < children.size(); i++) {
					ClientTooltipComponent child = children.get(i);
					int childAbsX = (i == 0) ? absX : anchorAbsX;
					clearChildHorizontalOffsets(font, anchorAbsX, verticalDepth, child, childAbsX);
				}
				return vertical;
			}
			default -> {
				int runningX = 0;
				for (ClientTooltipComponent child : composite.getComponents()) {
					int childAbsX = absX + runningX;
					clearHorizontalOffsets(child, font, childAbsX, anchorAbsX, verticalDepth);
					runningX += child.getWidth(font);
				}
				return composite;
			}
		}
	}

	private static void clearChildHorizontalOffsets(Font font, int anchorAbsX, int verticalDepth, ClientTooltipComponent child, int childAbsX) {
		if (child instanceof CompositeContainerComponent.Vertical nestedVert) {
			nestedVert.setHorizontalOffset(childAbsX - anchorAbsX);
			clearHorizontalOffsets(nestedVert, font, childAbsX, anchorAbsX, verticalDepth + 1);
		} else if (child instanceof CompositeContainerComponent.Horizontal horChild) {
			clearHorizontalOffsets(horChild, font, childAbsX, anchorAbsX, verticalDepth);
		} else {
			clearHorizontalOffsets(child, font, childAbsX, anchorAbsX, verticalDepth);
		}
	}

	private static ClientTooltipComponent setPropertVerticalOffsets(ClientTooltipComponent clientTooltipComponent, Font font) {
		return setProperVerticalOffsets(clientTooltipComponent, font, 0);
	}

	private static ClientTooltipComponent setProperVerticalOffsets(ClientTooltipComponent component, Font font, int verticalDepth) {
		int initialDepth = verticalDepth;
		if (!(component instanceof CompositeComponent composite)) {
			return forwardPassToNonComposite(component, font, (c, f) -> setProperVerticalOffsets(c, f, initialDepth));
		}
		if (composite instanceof CyclingComponent cycling) {
			return forwardPassToCycling(cycling, font, (c, f) -> setProperVerticalOffsets(c, f, initialDepth));
		}

		if (composite instanceof CompositeContainerComponent.Vertical vertical) {
			vertical.setHorizontalOffset(vertical.getHorizontalOffset() - verticalDepth * font.width("  "));
			verticalDepth++;
		}

		for (ClientTooltipComponent child : composite.getComponents()) {
			setProperVerticalOffsets(child, font, verticalDepth);
		}
		return composite;
	}

	private static ClientTooltipComponent forwardPassToNonComposite(ClientTooltipComponent component, Font font, Pass pass) {
		if (component instanceof ConditionalComponent(
				ClientTooltipComponent ifTrue, ClientTooltipComponent ifFalse, Supplier<Boolean> checker
		)) {
			return new ConditionalComponent(pass.apply(ifTrue, font), pass.apply(ifFalse, font), checker);
		}
		return component;
	}

	private static CyclingComponent forwardPassToCycling(CyclingComponent cycling, Font font, Pass pass) {
		CyclingComponent.Builder cyclingBuilder = CyclingComponent.builder();
		for (ClientTooltipComponent child : cycling.getComponents()) {
			cyclingBuilder.component(pass.apply(child, font));
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
		ClientTooltipComponent apply(ClientTooltipComponent component, Font font);
	}
}