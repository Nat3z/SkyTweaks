package com.natia.secretmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;

public class PersonalBankAccount extends SkyblockGui {
    private static Minecraft mc = Minecraft.getMinecraft();
    public static void render() {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();
        Gui.drawRect(0, 0, width, height, new Color(192, 192, 192, 155).getRGB());

        Gui.drawRect(width / 2 + 10, (int) (height * 0.7), width - 120, height - 25, new Color(43, 43, 43, 155).getRGB());

        mc.fontRendererObj.drawString("Coop Bank Account", width / 2 - 100, (int) (height * 0.7), Color.white.getRGB(), true);
    }

    public static void mouseEvent() {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();
        int mouseX = Mouse.getX() * width / Minecraft.getMinecraft().displayWidth;
        int mouseY = height - Mouse.getY() * height / Minecraft.getMinecraft().displayHeight - 1;

        if (mouseX > 10 && mouseX < 120 && mouseY <= 25 && mouseY >= 5)
        clickSlotOnContainer(11);
    }
}
