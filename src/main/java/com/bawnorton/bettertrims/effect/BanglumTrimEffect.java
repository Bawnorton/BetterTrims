package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.client.compat.Compat;
import com.bawnorton.bettertrims.client.compat.mythicmetals.MythicMetalsCompat;
import com.bawnorton.bettertrims.client.compat.yacl.CyclingItemImageRenderer;
import com.bawnorton.bettertrims.client.compat.yacl.ItemImageRenderer;
import com.bawnorton.bettertrims.data.tag.AdditionalItemTags;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.OptionType;
import com.bawnorton.configurable.Yacl;
import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Configurable(value = "banglum", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image(custom = "getImage"), collapsed = true))
public final class BanglumTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;

    public BanglumTrimEffect(TagKey<Item> materials) {
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
        return CompletableFuture.completedFuture(
                Registries.ITEM.getEntryList(AdditionalItemTags.BANGLUM_INGOTS)
                        .map(named -> named.stream()
                                .map(itemEntry -> itemEntry.value().getDefaultStack())
                                .toList())
                        .map(CyclingItemImageRenderer::new)
        );
    }
}
