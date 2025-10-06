package com.bawnorton.bettertrims.property.element;

import com.bawnorton.bettertrims.property.Matcher;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.function.Predicate;

public final class ConditionalElementMatcher<T extends TrimElement> extends ElementMatcher<T> {
	private final ConditionalElement<T> conditionalElement;

	public ConditionalElementMatcher(Matcher matcher, ConditionalElement<T> conditionalElement) {
		super(matcher, conditionalElement.element());
		this.conditionalElement = conditionalElement;
	}

	@Override
	public Predicate<LootContext> conditionChecker() {
		return conditionalElement::matches;
	}

	public ConditionalElement<T> getConditionalElement() {
		return conditionalElement;
	}
}
