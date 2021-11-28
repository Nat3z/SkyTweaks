package com.natia.secretmod.gui.pets;

import com.natia.secretmod.gui.base.SkyblockButton;
import com.natia.secretmod.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class ItemButton extends SkyblockButton {
    private ItemStack stack;
    public int slotNumber;
    public List<String> tooltip;

    public int mouseX = 0;
    public int mouseY = 0;

    public ItemButton(ItemStack stack, int slotNumber,
                      int id, int x, int y, List<String> tooltip) {
        super(id, x, y, 20, 20, "");
        this.slotNumber = slotNumber;
        this.stack = stack;
        this.tooltip = tooltip;
    }

    @Override
    public void renderButton(int mouseX, int mouseY) {
        if (visible) {
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            if (hovered) {
                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                RenderUtils.drawHoveringText(Collections.singletonList(stack.getDisplayName()),
                        0, 20,
                        sr.getScaledWidth(), sr.getScaledHeight(), -1, Minecraft.getMinecraft().fontRendererObj);
            }

            Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, new Color(59, 124, 130, 255).getRGB());

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            RenderUtils.renderItem(stack, xPosition + 2, yPosition + 2, 1f);
        }
    }
}
