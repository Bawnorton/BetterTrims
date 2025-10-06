package com.bawnorton.bettertrims.client.tooltip.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface Formatter {
	static MutableComponent decimal(Double value) {
		if (value == null) return Component.literal("null");

		if (value % 1 == 0) {
			return Component.literal("%.0f".formatted(value));
		} else if ((value * 10) % 1 == 0) {
			return Component.literal("%.1f".formatted(value));
		} else {
			return Component.literal("%.2f".formatted(value));
		}
	}

	static MutableComponent decimal(Float value) {
		return decimal(Double.valueOf(value));
	}

	static MutableComponent percentage(Double value) {
		if (value == null) return Component.literal("null");

		return decimal(value * 100).append(Component.literal("%"));
	}

	static MutableComponent percentage(Float value) {
		return percentage(Double.valueOf(value));
	}
}
