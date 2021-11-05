package com.natia.secretmod.vicious;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class Button extends GuiButton {
    /**
     * Create a button that is assigned a feature (to toggle/change color etc.).
     */

    private String buttonText;
    public ResourceLocation textureResource;
    public Button(int buttonId, int x, int y, int width, int height, ResourceLocation textureResource, String buttonText) {
        this(buttonId, x, y, width, height, width, height, textureResource, buttonText);
    }

    int tHeight;
    int tWidth;

    public Button(int buttonId, int x, int y, int width, int height, int textureWidth, int textureHeight, ResourceLocation textureResource, String buttonText) {
        super(buttonId, x, y, width, height, buttonText);
        this.buttonText = buttonText;
        this.textureResource = textureResource;
        this.tWidth = textureWidth;
        this.tHeight = textureHeight;
    }

    public void setImage(ResourceLocation text) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(text);
        Gui.drawModalRectWithCustomSizedTexture(xPosition, yPosition, 0, 0, width, height, tWidth, tHeight);
        textureResource = text;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            if (isMouseOver()) {
                mc.getTextureManager().bindTexture(textureResource);
                Gui.drawModalRectWithCustomSizedTexture(xPosition, yPosition, 0, 0, width + 20, height + 20, tWidth, tHeight);
                drawCenteredString(buttonText, xPosition + width / 2,
                        yPosition + height / 2 - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2,
                        Color.white.getRGB(), 1F);
            } else {
                mc.getTextureManager().bindTexture(textureResource);
                Gui.drawModalRectWithCustomSizedTexture(xPosition, yPosition, 0, 0, width, height, tWidth, tHeight);
                drawCenteredString(buttonText, xPosition + width / 2,
                        yPosition + height / 2 - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2,
                        Color.white.getRGB(), 1F);
            }
        }
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {	  }

    static void drawCenteredString(String text, int x, int y, int color, double scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        Minecraft.getMinecraft().fontRendererObj.drawString(text,
                (int) (x / scale) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) / 2,
                (int) (y / scale), color, true);
        GlStateManager.popMatrix();
    }
}
