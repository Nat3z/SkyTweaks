package natia.skytweaks.gui

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.gui.pets.PetsOverlay
import natia.skytweaks.SecretUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Mouse

class GuiHook {
    private val mc = Minecraft.getMinecraft()

    /* Skyblock Guis */
    private val partyFinderOverlay = PartyFinderOverlay()
    private val petsOverlay = PetsOverlay()

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onGuiOpen(event: GuiScreenEvent.DrawScreenEvent.Pre) {
        if (mc.thePlayer == null) return
        /* if not in sb, then don't execute */
        if (!SecretUtils.isValid) return

        /* check if event.gui is a GuiChest (aka a Skyblock vanilla gui) */
        if (event.gui is GuiChest) {
            /* event.gui is! cast that to a GuiChest */
            val guiChest = event.gui as GuiChest
            val container = guiChest.inventorySlots as ContainerChest
            val inventory = container.lowerChestInventory

            /* Override the gui to a SkyblockGui using the SkyblockGui class */
            if (inventory.name.contains("Party Finder")) {
                partyFinderOverlay.render(container)
            } else if (inventory.name.contains("Pets") && SkyTweaksConfig.customPetsMenu) {
                petsOverlay.isOpened = true
                petsOverlay.render(container)
                event.isCanceled = true
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onGuiClicked(event: GuiOpenEvent) {
        if (mc.thePlayer == null) return
        if (!Mouse.getEventButtonState()) return
        /* if not in sb, then don't execute */
        if (!SecretUtils.isValid) return

        /* guis were closed. update all opened guis */
        if (event.gui == null) {
            /* just create new instance to update lmfaoooo */
            if (petsOverlay.isOpened) {
                petsOverlay.onGuiClosed()
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onGuiClicked(event: GuiScreenEvent.MouseInputEvent.Pre) {
        if (mc.thePlayer == null) return
        if (!Mouse.getEventButtonState()) return
        /* if not in sb, then don't execute */
        if (!SecretUtils.isValid) return

        /* check if event.gui is a GuiChest (aka a Skyblock vanilla gui) */
        if (event.gui is GuiChest) {
            /* event.gui is! cast that to a GuiChest */
            val guiChest = event.gui as GuiChest
            val container = guiChest.inventorySlots as ContainerChest
            val inventory = container.lowerChestInventory
            /* Override the gui to a SkyblockGui using the SkyblockGui class */
            if (inventory.name.contains("Party Finder")) {
                partyFinderOverlay.mouseClicked()
            } else if (inventory.name.contains("Pets") && SkyTweaksConfig.customPetsMenu) {
                petsOverlay.mouseClicked()
                event.isCanceled = true
            }
        }
    }

}
