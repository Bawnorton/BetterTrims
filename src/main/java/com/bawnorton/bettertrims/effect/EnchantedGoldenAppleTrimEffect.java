package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.Yacl;
import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.TagKey;
import java.util.function.Consumer;

@Configurable(value = "enchanted_golden_apple", yacl = @Yacl(image = @Image(custom = "getImage"), collapsed = true))
public final class EnchantedGoldenAppleTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "max_health", min = 0, max = 20)
    public static int maxHealth = 3;
    @Configurable(min = 0, max = 1)
    public static float resistance = 0.04f;
    @Configurable(min = 0, max = 16)
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
    protected boolean getEnabled() {
        return enabled;
    }

    public static ImageRenderer getImage() {
        return new ImageRenderer() {
            @Override
            public int render(DrawContext graphics, int x, int y, int renderWidth, float tickDelta) {
                float ratio = renderWidth / 16f;
                int targetHeight = (int) (16f * ratio);

                graphics.getMatrices().push();
                graphics.getMatrices().translate(x, y, 0);
                graphics.getMatrices().scale(ratio, ratio, 1);
                graphics.drawItem(Items.ENCHANTED_GOLDEN_APPLE.getDefaultStack(), 0, 0);
                graphics.getMatrices().pop();

                return targetHeight;
            }

            @Override
            public void close() {
            }
        };
    }
}