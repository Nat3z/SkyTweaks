package com.natia.secretmod.gui.base;

import jline.internal.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class SkyblockButton {

    public int xPosition;
    public int yPosition;
    public int width;
    public int height;
    public int id;
    public String displayText;
    public boolean visible;
    private int textureWidth;
    private int textureHeight;
    @Nullable
    public ResourceLocation texture;

    public int mouseX = 0;
    public int mouseY = 0;

    public boolean hovered = false;

    public SkyblockButton(int id, int x, int y, int width, int height, ResourceLocation texture, int textureWidth, int textureHeight, String displayText) {
        this.xPosition = x;
        this.visible = true;
        this.yPosition = y;
        this.width = width;
        this.height = height;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.texture = texture;
        this.id = id;
        this.displayText = displayText;
    }

    public SkyblockButton(int id, int x, int y, int width, int height, String displayText) {
        this.xPosition = x;
        this.visible = true;
        this.yPosition = y;
        this.width = width;
        this.height = height;
        this.textureWidth = 0;
        this.textureHeight = 0;
        this.texture = null;
        this.id = id;
        this.displayText = displayText;
    }

    public void renderButton(int mouseX, int mouseY) {
        if (visible) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            this.mouseX = mouseX;
            this.mouseY = mouseY;

            if (texture == null) {
                if (hovered) {
                    Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, new Color(148, 146, 142, 40).getRGB());
                } else {
                    Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, new Color(68, 116, 179, 100).getRGB());
                }
            }
            else {
                Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
                Gui.drawModalRectWithCustomSizedTexture(xPosition, yPosition, 0, 0, width + textureWidth, height + textureHeight, textureWidth, textureHeight);
            }

            drawCenteredString(displayText, xPosition + width / 2,
                    yPosition + height / 2 - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2,
                    Color.white.getRGB(), 1F);
        }
    }

    private void drawCenteredString(String text, int x, int y, int color, double scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        Minecraft.getMinecraft().fontRendererObj.drawString(text,
                (int) (x / scale) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) / 2,
                (int) (y / scale), color, true);
        GlStateManager.popMatrix();
    }

    public boolean isMouseOver() {
        return this.hovered;
    }
}
