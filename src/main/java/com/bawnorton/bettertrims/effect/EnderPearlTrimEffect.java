package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.Yacl;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

@Configurable(value = "ender_pearl", yacl = @Yacl(image = @Image("minecraft:textures/item/ender_pearl.png"), collapsed = true))
public final class EnderPearlTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "projectile_dodge_chane", min = 0, max = 1)
    public static float projectileDodegChance = 0.25f;

    public EnderPearlTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.HYDROPHOBIC));
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.PROJECTILE_DODGE_CHANCE, projectileDodegChance));
    }

    @Override
    protected boolean getEnabled() {
        return enabled;
    }
}