package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.OptionType;
import com.bawnorton.configurable.Yacl;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

//? if >=1.21
/*import net.minecraft.component.type.AttributeModifierSlot;*/

@Configurable(value = "slime", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image("minecraft:textures/item/slime_ball.png"), collapsed = true))
public final class SlimeTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "knockback_vulnerability", max = 4)
    public static double knockbackVulnerability = 1;
    @Configurable(value = "attack_knockback", max = 2)
    public static double attackKnockback = 1;
    @Configurable(value = "bouncy_boots")
    public static boolean bouncyBoots = true;

    public SlimeTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(() -> TrimEntityAttributes.BOUNCY).forSlot(EquipmentSlot.FEET));
        adder.accept(TrimAttribute.adding(() -> EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, -knockbackVulnerability));
        adder.accept(TrimAttribute.adding(() -> EntityAttributes.GENERIC_ATTACK_KNOCKBACK, attackKnockback));
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }
}