package com.bawnorton.bettertrims.client.mixin.attributes.enchanters_blessing;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21 {
/*import com.bawnorton.bettertrims.registry.content.TrimComponentTypes;
import net.minecraft.client.gui.screen.ButtonTextures;
*///?}

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin extends HandledScreen<EnchantmentScreenHandler> {
    @Shadow private ItemStack stack;

    //? if >=1.21 {
    /*@Unique
    private static final ButtonTextures bettertrims$REROLL_BUTTON_TEXTURES = new ButtonTextures(
            BetterTrims.id("reroll"),
            BetterTrims.id("reroll")
    );
    *///?} else {
    @Unique
    private static final Identifier bettertrims$REROLL_BUTTON_TEXTURE = BetterTrims.id("reroll");
    //?}

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
        //? if >=1.21 {
        /*rerollButton = new TexturedButtonWidget(
                x + backgroundWidth - 20,
                y - 6,
                15,
                15,
                bettertrims$REROLL_BUTTON_TEXTURES,
                button -> {}, ScreenTexts.EMPTY
        );
        *///?} else {
        rerollButton = new TexturedButtonWidget(
                x + backgroundWidth - 20,
                y - 6,
                15,
                15,
                0,
                0,
                0,
                bettertrims$REROLL_BUTTON_TEXTURE,
                15,
                15,
                button -> {},
                ScreenTexts.EMPTY
        );
        //?}
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
        //? if >=1.21 {
        /*boolean enchanted = EnchantmentHelper.hasEnchantments(stack);
        *///?} else {
        boolean enchanted = stack.hasEnchantments();
        //?}
        if(mouseY < y && rerollButton.isMouseOver(mouseX, mouseY) && !stack.isEmpty() && !enchanted) {
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
        //? if >=1.21 {
        /*boolean enchanted = EnchantmentHelper.hasEnchantments(stack);
         *///?} else {
        boolean enchanted = stack.hasEnchantments();
        //?}
        if(stack.isEmpty() || enchanted) {
            message = ScreenTexts.EMPTY;
        } else {
            int enchantersBlessingLevel = (int) client.player.getAttributeValue(TrimEntityAttributes.ENCHANTERS_FAVOUR);
            //? if >=1.21 {
            /*int usedBlessings = stack.getOrDefault(TrimComponentTypes.USED_BLESSINGS, 0);
            *///?} else {
            int usedBlessings = 0;
            NbtCompound nbt = stack.getNbt();
            if(nbt != null) {
                usedBlessings = nbt.getInt("bettertrims$used_blessings");
            }
            //?}
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
