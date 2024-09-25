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

@Configurable(value = "kyber", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image(custom = "getImage"), collapsed = true))
public final class KyberTrimEffect extends TrimEffect {
	@Configurable
	public static boolean enabled = true;
    @Configurable(value = "projectile_deflect_chance", min = 0, max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
    public static float projectileDeflectChance = 0.15f;
    @Configurable(value = "fire_resistance", min = 0, max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
    public static float fireResistance = 0.08f;

    public KyberTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyBase(() -> TrimEntityAttributes.PROJECTILE_DEFLECT_CHANCE, projectileDeflectChance));
        adder.accept(TrimAttribute.multiplyBase(() -> TrimEntityAttributes.FIRE_RESISTANCE, fireResistance));
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }

    public static CompletableFuture<Optional<ImageRenderer>> getImage() {
        return getImageFor(AdditionalItemTags.KYBER_INGOTS);
    }
}
