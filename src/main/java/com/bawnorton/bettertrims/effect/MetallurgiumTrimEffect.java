package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.data.tag.AdditionalItemTags;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.OptionType;
import com.bawnorton.configurable.Yacl;
import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Configurable(value = "metallurgium", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image(custom = "getImage"), collapsed = true))
public final class MetallurgiumTrimEffect extends TrimEffect {
	@Configurable
	public static boolean enabled = true;
    @Configurable(min = 0, max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
    public static float resistance = 0.12f;
    @Configurable(min = 0, max = 16, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
    public static float armour = 2;

    public MetallurgiumTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyBase(() -> TrimEntityAttributes.RESISTANCE, resistance));
        adder.accept(TrimAttribute.adding(() -> EntityAttributes.GENERIC_ARMOR, armour));
        adder.accept(TrimAttribute.leveled(() -> TrimEntityAttributes.UNBREAKING));
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }

    public static CompletableFuture<Optional<ImageRenderer>> getImage() {
        return getImageFor(AdditionalItemTags.METALLURGIUM_INGOTS);
    }
}
