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

@Configurable(value = "celestium", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image(custom = "getImage"), collapsed = true))
public final class CelestiumTrimEffect extends TrimEffect {
	@Configurable
	public static boolean enabled = true;
    @Configurable(value = "elytra_rocket_speed", min = 0, max = 5, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
    public static float elytraRocketSpeed = 0.1f;
    @Configurable(value = "movement_speed", min = 0, max = 16)
    public static float movementSpeed = 0.1f;
    @Configurable(value = "attack_damage", min = 0, max = 16)
    public static float attackDamage = 0.5f;

    public CelestiumTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.addingAliased(() -> TrimEntityAttributes.ELYTRA_ROCKET_SPEED, elytraRocketSpeed));
        adder.accept(TrimAttribute.adding(() -> EntityAttributes.GENERIC_MOVEMENT_SPEED, movementSpeed));
        adder.accept(TrimAttribute.adding(() -> EntityAttributes.GENERIC_ATTACK_DAMAGE, attackDamage));
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }

    public static CompletableFuture<Optional<ImageRenderer>> getImage() {
        return getImageFor(AdditionalItemTags.CELESTIUM_INGOTS);
    }
}
