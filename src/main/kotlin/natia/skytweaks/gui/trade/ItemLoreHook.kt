package natia.skytweaks.gui.trade

import natia.skytweaks.gui.GuiHook
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.init.Blocks
import net.minecraft.inventory.ContainerChest
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.concurrent.TimeUnit

class ItemLoreHook {

    @SubscribeEvent
    fun lore(event: ItemTooltipEvent) {
        for (it in GuiHook.tradeGui.hugeRedFlags) {
            if (it.slot.stack != event.itemStack) return
            event.toolTip.add(" ")
            event.toolTip.add("${EnumChatFormatting.STRIKETHROUGH}${EnumChatFormatting.RED}--------------------")
            event.toolTip.add("${EnumChatFormatting.YELLOW}⚠${EnumChatFormatting.RED} THIS ITEM IS A RED FLAG ${EnumChatFormatting.YELLOW}⚠")
            event.toolTip.add("${EnumChatFormatting.RED}REASON - ${EnumChatFormatting.YELLOW}${it.reason}")
            event.toolTip.add("${EnumChatFormatting.YELLOW}⚠ ${EnumChatFormatting.RED}THIS IS A LEVEL ${it.infraction}/3 INFRACTION ${EnumChatFormatting.YELLOW}⚠")
            event.toolTip.add("${EnumChatFormatting.STRIKETHROUGH}${EnumChatFormatting.RED}--------------------")
            break
        }

        if (Minecraft.getMinecraft().currentScreen !is GuiChest) return
        val guiChest = Minecraft.getMinecraft().currentScreen as GuiChest
        val container = guiChest.inventorySlots as ContainerChest
        val inventory = container.lowerChestInventory
        if (!inventory.name.startsWith("You")) return
        if (container.getSlot(39).stack == event.itemStack
            && GuiHook.tradeGui.timeLength.elapsed(TimeUnit.SECONDS) - GuiHook.tradeGui.lastChanged <= 10) {
            event.toolTip.add(" ")
            event.toolTip.add("${EnumChatFormatting.STRIKETHROUGH}${EnumChatFormatting.RED}--------------------")
            event.toolTip.add("${EnumChatFormatting.YELLOW}⚠ ${EnumChatFormatting.RED}The Trade Menu has recently changed! ${EnumChatFormatting.YELLOW}⚠")
            event.toolTip.add("${EnumChatFormatting.GRAY}Please wait 10s before doing this.")
            event.toolTip.add("${EnumChatFormatting.STRIKETHROUGH}${EnumChatFormatting.RED}--------------------")
        }
    }
}