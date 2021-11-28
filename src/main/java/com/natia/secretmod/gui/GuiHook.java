package com.natia.secretmod.gui;

import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SkyTweaksConfig;
import com.natia.secretmod.gui.pets.PetsOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

public class GuiHook {
    private Minecraft mc = Minecraft.getMinecraft();

    /* Skyblock Guis */
    private PartyFinderOverlay partyFinderOverlay = new PartyFinderOverlay();
    private PetsOverlay petsOverlay = new PetsOverlay();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
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
            if (inventory.getName().contains("Party Finder")) {
                partyFinderOverlay.render(container);
            } else if (inventory.getName().contains("Pets") && SkyTweaksConfig.customPetsMenu) {
                petsOverlay.isOpened = true;
                petsOverlay.render(container);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onGuiClicked(GuiOpenEvent event) {
        if (mc.thePlayer == null) return;
        if (!Mouse.getEventButtonState()) return;
        /* if not in sb, then don't execute */
        if (!SecretUtils.isValid()) return;

        /* guis were closed. update all opened guis */
        if (event.gui == null) {
            /* just create new instance to update lmfaoooo */
            if (petsOverlay.isOpened) {
                petsOverlay.onGuiClosed();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onGuiClicked(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (mc.thePlayer == null) return;
        if (!Mouse.getEventButtonState()) return;
        /* if not in sb, then don't execute */
        if (!SecretUtils.isValid()) return;

        /* check if event.gui is a GuiChest (aka a Skyblock vanilla gui) */
        if (event.gui instanceof GuiChest) {
            /* event.gui is! cast that to a GuiChest */
            GuiChest guiChest = (GuiChest) event.gui;
            ContainerChest container = (ContainerChest) guiChest.inventorySlots;
            IInventory inventory = container.getLowerChestInventory();
            /* Override the gui to a SkyblockGui using the SkyblockGui class */
            if (inventory.getName().contains("Party Finder")) {
                partyFinderOverlay.mouseClicked();
            } else if (inventory.getName().contains("Pets") && SkyTweaksConfig.customPetsMenu) {
                petsOverlay.mouseClicked();
                event.setCanceled(true);
            }
        }
    }

}
