package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;
@Configurable("slime")
public final class SlimeTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "knockback_vulnerability", min = 0, max = 4)
    public static double knockbackVulnerability = 1;
    @Configurable(value = "attack_knockback", min = 0, max = 2)
    public static double attackKnockback = 1;
    @Configurable(value = "bouncy_boots")
    public static boolean bouncyBoots = true;

    public SlimeTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        if(bouncyBoots) {
            adder.accept(TrimAttribute.leveled(TrimEntityAttributes.BOUNCY).forSlot(AttributeModifierSlot.FEET));
        }
        adder.accept(TrimAttribute.adding(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, -knockbackVulnerability));
        adder.accept(TrimAttribute.adding(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, attackKnockback));
    }

    @Override
    protected boolean getEnabled() {
        return enabled;
    }
}