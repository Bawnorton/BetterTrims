package com.bawnorton.bettertrims.client.mixin.attributes.enchanters_blessing;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.registry.content.TrimComponentTypes;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin extends HandledScreen<EnchantmentScreenHandler> {
    @Shadow private ItemStack stack;
    @Unique
    private static final ButtonTextures bettertrims$REROLL_BUTTON_TEXTURES = new ButtonTextures(
            BetterTrims.id("reroll"),
            BetterTrims.id("reroll")
    );
    @Unique
    private ButtonWidget rerollButton;

    public EnchantmentScreenMixin(EnchantmentScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(
            method = "init",
            at = @At("TAIL")
    )
    private void initRerollButton(CallbackInfo ci) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        rerollButton = new TexturedButtonWidget(
                x + backgroundWidth - 20, y - 6, 15, 15,
                bettertrims$REROLL_BUTTON_TEXTURES,
                button -> {}, ScreenTexts.EMPTY
        );
    }

    @Inject(
            method = "drawBackground",
            at = @At("HEAD")
    )
    private void renderReroll(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        rerollButton.visible = client.player.getAttributeValue(TrimEntityAttributes.ENCHANTERS_FAVOUR) > 0;
        if(!rerollButton.visible) return;

        if(rerollButton.getTooltip() == null) {
            bettertrims$updateRerollTooltip();
        }
        int y = (this.height - this.backgroundHeight) / 2;
        context.enableScissor(
                rerollButton.getX() + 1,
                y - rerollButton.getHeight() + 3,
                rerollButton.getX() + rerollButton.getWidth(),
                y
        );
        if(mouseY < y && rerollButton.isMouseOver(mouseX, mouseY) && !stack.isEmpty() && !EnchantmentHelper.hasEnchantments(stack)) {
            rerollButton.setY(MathHelper.lerp(delta, rerollButton.getY(), y - 15));
        } else {
            rerollButton.setY(y - 6);
        }
        rerollButton.render(context, mouseX, mouseY, delta);
        context.disableScissor();
    }

    @WrapOperation(
            method = "mouseClicked",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;mouseClicked(DDI)Z"
            )
    )
    private boolean handleRerollButtonClick(EnchantmentScreen instance, double mouseX, double mouseY, int button, Operation<Boolean> original) {
        if (rerollButton.mouseClicked(mouseX, mouseY, button) && handler.onButtonClick(client.player, 67)) {
            client.interactionManager.clickButton(this.handler.syncId, 67);
            return true;
        }
        return original.call(instance, mouseX, mouseY, button);
    }

    @Unique
    private void bettertrims$updateRerollTooltip() {
        Text message;
        if(stack.isEmpty() || EnchantmentHelper.hasEnchantments(stack)) {
            message = ScreenTexts.EMPTY;
        } else {
            int enchantersBlessingLevel = (int) client.player.getAttributeValue(TrimEntityAttributes.ENCHANTERS_FAVOUR);
            int usedBlessings = stack.getOrDefault(TrimComponentTypes.USED_BLESSINGS, 0);
            int rerollsLeft = enchantersBlessingLevel * AttributeSettings.EnchantersFavour.rerolls - usedBlessings;
            if(rerollsLeft > 0) {
                message = Text.translatable("bettertrims.enchanters_blessing.rerolls", enchantersBlessingLevel - usedBlessings, enchantersBlessingLevel);
            } else {
                message = Text.translatable("bettertrims.enchanters_blessing.no_rerolls");
            }
        }
        rerollButton.setTooltip(Tooltip.of(message));
    }

    @Inject(
            method = "doTick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screen/ingame/EnchantmentScreen;stack:Lnet/minecraft/item/ItemStack;",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void onItemUpdate(CallbackInfo ci) {
        bettertrims$updateRerollTooltip();
    }
}
