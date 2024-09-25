package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.data.tag.AdditionalItemTags;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.OptionType;
import com.bawnorton.configurable.Yacl;
import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

//@Configurable(value = "unobtainium", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image(custom = "getImage"), collapsed = true))
public final class UnobtainiumTrimEffect extends TrimEffect {
//	@Configurable
	public static boolean enabled = false;

    public UnobtainiumTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }

    public static CompletableFuture<Optional<ImageRenderer>> getImage() {
        return getImageFor(AdditionalItemTags.UNOBTAINIUM);
    }
}
