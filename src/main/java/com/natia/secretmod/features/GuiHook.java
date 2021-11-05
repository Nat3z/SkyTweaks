package com.natia.secretmod.features;

import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SecretModConfig;
import com.natia.secretmod.gui.PersonalBankAccount;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

public class GuiHook {
    Minecraft mc = Minecraft.getMinecraft();
    @SubscribeEvent
    public void onGuiOpen(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (mc.thePlayer == null) return;
        /* if not in sb, then don't execute */
        if (!SecretUtils.isValid()) return;

        /* check if event.gui is a GuiChest (aka a Skyblock vanilla gui) */
        if (event.gui instanceof GuiChest) {
            /* event.gui is! cast that to a GuiChest */
            GuiChest guiChest = (GuiChest) event.gui;
            ContainerChest container = (ContainerChest) guiChest.inventorySlots;
            IInventory inventory = container.getLowerChestInventory();

            /* Override the gui to a SkyblockGui using the SkyblockGui class */
            if (inventory.getName().contains("Bank") && !inventory.getName().endsWith("Account") && SecretModConfig.bankGui) {
                PersonalBankAccount.render();
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onGuiClicked(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (mc.thePlayer == null) return;
        if (!Mouse.getEventButtonState() && Mouse.getEventButton() != 0) return;
        /* if not in sb, then don't execute */
        if (!SecretUtils.isValid()) return;

        /* check if event.gui is a GuiChest (aka a Skyblock vanilla gui) */
        if (event.gui instanceof GuiChest) {
            /* event.gui is! cast that to a GuiChest */
            GuiChest guiChest = (GuiChest) event.gui;
            ContainerChest container = (ContainerChest) guiChest.inventorySlots;
            IInventory inventory = container.getLowerChestInventory();

            /* Override the gui to a SkyblockGui using the SkyblockGui class */
            if (inventory.getName().contains("Bank") && !inventory.getName().endsWith("Account") && SecretModConfig.bankGui) {
                PersonalBankAccount.mouseEvent();
                event.setCanceled(true);
            }
        }
    }

}
