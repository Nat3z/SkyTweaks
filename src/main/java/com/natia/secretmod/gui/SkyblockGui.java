package com.natia.secretmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;

public class SkyblockGui {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static void render() {

    }

    public static void mouseEvent() {

    }

    static void clickSlotOnContainer(int slotID) {
        GuiChest guiChest = (GuiChest) mc.currentScreen;
        mc.playerController.windowClick(guiChest.inventorySlots.windowId, slotID, 0, 0, mc.thePlayer);
    }
}
