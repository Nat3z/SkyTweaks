package com.natia.secretmod.gui.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class SkyblockGui {
    private Minecraft mc = Minecraft.getMinecraft();
    public List<SkyblockButton> buttonList = new ArrayList<>();
    public boolean isOpened = false;

    /**
     * When Gui is being rendered, this method is called.
     */
    public void render(ContainerChest container) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();
        int mouseX = Mouse.getX() * width / Minecraft.getMinecraft().displayWidth;
        int mouseY = height - Mouse.getY() * height / Minecraft.getMinecraft().displayHeight - 1;

        for (SkyblockButton button : buttonList) {
            button.renderButton(mouseX, mouseY);
        }
    }

    /**
     * When someone clicks in your gui. Only use if you are changing how mouse Event is handled.
     */
    public void mouseClicked() {
        for (SkyblockButton button : buttonList) {
            if (button.isMouseOver()) {
                this.actionPreformed(button);
                break;
            }
        }
    }

    /**
     * When someone clicks in your gui. This method is used as the callback for mouseEvent(). Recommended
     * if handling clicks.
     * @param button
     */
    public void actionPreformed(SkyblockButton button) {

    }

    /**
     * When the gui is closed, this method is called.
     */
    public void onGuiClosed() {

    }

    protected void clickSlotOnContainer(int slotID) {
        GuiChest guiChest = (GuiChest) mc.currentScreen;
        mc.playerController.windowClick(guiChest.inventorySlots.windowId, slotID, 0, 0, mc.thePlayer);
    }

}
