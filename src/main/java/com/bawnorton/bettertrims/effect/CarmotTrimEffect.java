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

@Configurable(value = "carmot", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image(custom = "getImage"), collapsed = true))
public final class CarmotTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "carmot_shield", min = 0, max = 15)
    public static float carmotShield = 3f;
    @Configurable(value = "max_health", min = 0, max = 15)
    public static float maxHealth = 1f;

    public CarmotTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.addingAliased(() -> TrimEntityAttributes.CARMOT_SHIELD, carmotShield));
        adder.accept(TrimAttribute.leveled(() -> TrimEntityAttributes.FORTUNE));
        adder.accept(TrimAttribute.adding(() -> EntityAttributes.GENERIC_MAX_HEALTH, maxHealth));
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }

    public static CompletableFuture<Optional<ImageRenderer>> getImage() {
        return getImageFor(AdditionalItemTags.CARMOT_INGOTS);
    }
}
