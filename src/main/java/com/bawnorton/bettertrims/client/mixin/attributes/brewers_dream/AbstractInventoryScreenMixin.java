package com.bawnorton.bettertrims.client.mixin.attributes.brewers_dream;

import com.bawnorton.bettertrims.extend.ModifiedTimeHolder;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.include.com.google.common.collect.ImmutableList;
import java.util.List;

@Mixin(AbstractInventoryScreen.class)
public abstract class AbstractInventoryScreenMixin<T extends ScreenHandler> extends HandledScreen<T> {
    public AbstractInventoryScreenMixin(T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @ModifyExpressionValue(
            method = "drawStatusEffects",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;of(Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/List;",
                    remap = false
            )
    )
    private List<Text> appendBrewersDreamTimeModification(List<Text> original, @Local(ordinal = 0) StatusEffectInstance statusEffectInstance) {
        int ticks = ((ModifiedTimeHolder) statusEffectInstance).bettertrims$getModifiedTime();
        if(ticks <= 0) return original;

        //? if >=1.21 {
        /*MutableText modifiedTime = Text.literal(StringHelper.formatTicks(ticks, client.world.getTickManager().getTickRate()))
                .withColor(0xFF9A5CC6)
                .append(ScreenTexts.SPACE)
                .append(Text.translatable("bettertrims.brewers_dream.%s".formatted(
                        statusEffectInstance.getEffectType().value().isBeneficial() ? "added" : "removed"
                )));
        *///?} else {
        MutableText modifiedTime = Text.literal(StringHelper.formatTicks(ticks))
                .styled(style -> style.withColor(0xFF9A5CC6))
                .append(ScreenTexts.SPACE)
                .append(Text.translatable("bettertrims.brewers_dream.%s".formatted(
                        statusEffectInstance.getEffectType().isBeneficial() ? "added" : "removed"
                )));
        //?}

        return ImmutableList.<Text>builder()
                .addAll(original)
                .add(modifiedTime)
                .build();
    }

    @Inject(
            method = "drawStatusEffectDescriptions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I",
                    shift = At.Shift.AFTER,
                    ordinal = 1
            )
    )
    private void drawBrewersDreamTimeModification(DrawContext context, int x, int height, Iterable<StatusEffectInstance> statusEffects, CallbackInfo ci,
            @Local StatusEffectInstance statusEffectInstance,
            @Local(ordinal = 2) int adjustedY) {
        int ticks = ((ModifiedTimeHolder) statusEffectInstance).bettertrims$getModifiedTime();
        if(ticks <= 0) return;

        //? if >=1.21 {
        /*Text modifiedTime = Text.literal("%s%s".formatted(
                statusEffectInstance.getEffectType().value().isBeneficial() ? "+" : "-",
                StringHelper.formatTicks(ticks, client.world.getTickManager().getTickRate())
        ));
        *///?} else {
        Text modifiedTime = Text.literal("%s%s".formatted(
                statusEffectInstance.getEffectType().isBeneficial() ? "+" : "-",
                StringHelper.formatTicks(ticks)
        ));
        //?}
        int xPos = x + 120 - textRenderer.getWidth(modifiedTime) - 8;
        int yPos = adjustedY + 16;
        context.drawTextWithShadow(textRenderer, modifiedTime, xPos, yPos, 0xFF9A5CC6);
        context.getMatrices().push();
        float scale = 0.55f;
        context.getMatrices().scale(scale, scale, 1);
        context.drawItem(Items.AMETHYST_SHARD.getDefaultStack(), (int) ((xPos - 10) / scale), (int) ((yPos - 1) / scale));
        context.getMatrices().pop();
    }
}
