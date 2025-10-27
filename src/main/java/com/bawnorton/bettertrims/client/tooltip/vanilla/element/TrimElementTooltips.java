package com.bawnorton.bettertrims.client.tooltip.vanilla.element;

import com.bawnorton.bettertrims.property.AllOf;
import com.bawnorton.bettertrims.property.ability.type.entity.*;
import com.bawnorton.bettertrims.property.ability.type.toggle.AttributeAbility;
import com.bawnorton.bettertrims.property.ability.type.toggle.ToggleMobEffectAbility;
import com.bawnorton.bettertrims.property.ability.type.value.AddValue;
import com.bawnorton.bettertrims.property.ability.type.value.MultiplyValue;
import com.bawnorton.bettertrims.property.ability.type.value.RemoveBinomial;
import com.bawnorton.bettertrims.property.ability.type.value.SetValue;
import com.bawnorton.bettertrims.property.element.TrimElement;

import java.util.HashMap;
import java.util.Map;

public final class TrimElementTooltips {
	private static final Map<Class<? extends TrimElement>, TrimElementTooltipProvider<? extends TrimElement>> PROVIDERS = new HashMap<>();

	static {
		register(ApplyMobEffectAbility.class, new ApplyMobEffectAbility.TooltipProvider());
		register(ChangeItemDamageAbility.class, new ChangeItemDamageAbility.TooltipProvider());
		register(DamageEntityAbility.class, new DamageEntityAbility.TooltipProvider());
		register(ExplodeAbility.class, new ExplodeAbility.TooltipProvider());
		register(IgniteAbility.class, new IgniteAbility.TooltipProvider());
		register(PlaySoundAbility.class, new PlaySoundAbility.TooltipProvider());
		register(ReplaceBlockAbility.class, new ReplaceBlockAbility.TooltipProvider());
		register(ReplaceDiskAbility.class, new ReplaceDiskAbility.TooltipProvider());
		register(RunFunctionAbility.class, new RunFunctionAbility.TooltipProvider());
		register(SetBlockPropertiesAbility.class, new SetBlockPropertiesAbility.TooltipProvider());
		register(SpawnParticlesAbility.class, new SpawnParticlesAbility.TooltipProvider());
		register(SummonEntityAbility.class, new SummonEntityAbility.TooltipProvider());
		register(AttributeAbility.class, new AttributeAbility.TooltipProvider());
		register(ToggleMobEffectAbility.class, new ToggleMobEffectAbility.TooltipProvider());
		register(AddValue.class, new AddValue.TooltipProvider());
		register(MultiplyValue.class, new MultiplyValue.TooltipProvider());
		register(RemoveBinomial.class, new RemoveBinomial.TooltipProvider());
		register(SetValue.class, new SetValue.TooltipProvider());
		register(AllOf.ItemProperties.class, new AllOf.ItemProperties.TooltipProvider());
		register(AllOf.EntityAbilities.class, new AllOf.EntityAbilities.TooltipProvider());
		register(AllOf.ToggleAbilities.class, new AllOf.ToggleAbilities.TooltipProvider());
		register(AllOf.ValueAbilities.class, new AllOf.ValueAbilities.TooltipProvider());
	}

	public static <T extends TrimElement> void register(Class<T> clazz, TrimElementTooltipProvider<T> provider) {
		if (PROVIDERS.containsKey(clazz)) {
			throw new IllegalStateException("Provider for " + clazz + " is already registered");
		}
		PROVIDERS.put(clazz, provider);
	}

	@SuppressWarnings("unchecked")
	public static TrimElementTooltipProvider<TrimElement> getProvider(Class<? extends TrimElement> clazz) {
		return (TrimElementTooltipProvider<TrimElement>) PROVIDERS.getOrDefault(clazz, TrimElementTooltipProvider.EMPTY);
	}
}
