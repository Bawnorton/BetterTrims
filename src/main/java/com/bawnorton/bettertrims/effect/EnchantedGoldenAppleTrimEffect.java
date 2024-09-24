package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.client.compat.yacl.ItemImageRenderer;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.OptionType;
import com.bawnorton.configurable.Yacl;
import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

@Configurable(value = "enchanted_golden_apple", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image(custom = "getImage"), collapsed = true))
public final class EnchantedGoldenAppleTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "max_health", min = 0, max = 20)
    public static int maxHealth = 3;
    @Configurable(min = 0, max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
    public static float resistance = 0.04f;
    @Configurable(min = 0, max = 16, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
    public static float regeneration = 0.4f;

    public EnchantedGoldenAppleTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.adding(EntityAttributes.GENERIC_MAX_HEALTH, maxHealth));
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.RESISTANCE, resistance));
        adder.accept(TrimAttribute.adding(TrimEntityAttributes.REGENERATION, regeneration));
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }

    public static ImageRenderer getImage() {
        return new ItemImageRenderer(Items.ENCHANTED_GOLDEN_APPLE.getDefaultStack());
    }
}