package com.bawnorton.bettertrims.client.tooltip.braid.widget;

import io.wispforest.owo.braid.framework.BuildContext;
import io.wispforest.owo.braid.framework.proxy.WidgetState;
import io.wispforest.owo.braid.framework.widget.StatefulWidget;
import io.wispforest.owo.braid.framework.widget.StatelessWidget;
import io.wispforest.owo.braid.framework.widget.Widget;
import io.wispforest.owo.braid.widgets.button.Button;
import io.wispforest.owo.braid.widgets.flex.CrossAxisAlignment;
import io.wispforest.owo.braid.widgets.flex.MainAxisAlignment;
import io.wispforest.owo.braid.widgets.flex.Row;
import io.wispforest.owo.braid.widgets.label.Label;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NumberStepperWidget extends StatefulWidget {
	private final int min;
	private final int max;
	private final int number;
	private final Consumer<Integer> onValueChanged;

	public NumberStepperWidget(int min, int max, int number, Consumer<Integer> onValueChanged) {
		this.min = min;
		this.max = max;
		this.number = number;
		this.onValueChanged = onValueChanged;
	}

	@Override
	public WidgetState<NumberStepperWidget> createState() {
		return new State();
	}

	public static class State extends WidgetState<NumberStepperWidget> {
		private int number = 0;

		@Override
		public void init() {
			number = widget().number;
		}

		@Override
		public Widget build(BuildContext context) {
			List<Widget> widgets = new ArrayList<>();
			if (number > widget().min) {
				widgets.add(new StepperButtonWidget("<", () -> {
					setState(() -> number -= 1);
					widget().onValueChanged.accept(number);
				}));
			}
			if (number != widget().min || number != widget().max) {
				widgets.add(new Label(Component.literal(Integer.toString(number))));
			}
			if (number < widget().max) {
				widgets.add(new StepperButtonWidget(">", () -> {
					setState(() -> number += 1);
					widget().onValueChanged.accept(number);
				}));
			}
			return new Row(MainAxisAlignment.START, CrossAxisAlignment.CENTER, widgets);
		}

		private static class StepperButtonWidget extends StatelessWidget {
			private final String label;
			private final Runnable onClick;

			public StepperButtonWidget(String label, Runnable onClick) {
				this.label = label;
				this.onClick = onClick;
			}

			@Override
			public Widget build(BuildContext context) {
				return new Button(
						onClick,
						new Label(Component.literal(label))
				);
			}
		}
	}
}
