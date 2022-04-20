package natia.skytweaks.gui.trade

import com.google.common.base.Stopwatch
import com.google.common.collect.ImmutableList
import natia.skytweaks.SecretUtils
import natia.skytweaks.gui.base.SkyblockGui
import natia.skytweaks.utils.AsyncAwait
import natia.skytweaks.utils.ItemUtils
import natia.skytweaks.utils.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.inventory.ContainerChest
import net.minecraft.inventory.Slot
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.ResourceLocation
import net.minecraft.util.StringUtils
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class TradeGui : SkyblockGui() {
    val hugeRedFlags: MutableList<RedFlags> = ArrayList()
    var lastContainerSlots: MutableList<Slot> = ArrayList()
    val eventLog: EventLog = EventLog()
    init {
        this.buttonList.add(eventLog)
    }

    var firstOpened = false
    var timeLength = Stopwatch.createUnstarted()
    var lastChanged = -500

    var alreadyRanForTheSecond = false
    override fun render(container: ContainerChest) {
        super.render(container)
        if (!firstOpened) {
            timeLength.start()
            firstOpened = true
        }
        if (container.getSlot(39) == null || container.getSlot(39).stack == null) return
        if (!alreadyRanForTheSecond && StringUtils.stripControlCodes(container.getSlot(39).stack.displayName).startsWith("Deal timer! (3)")) {
            hugeRedFlags.clear()
            for (slot in container.inventorySlots) {
                if (slot == null) continue
                if (slot.stack == null) continue

                for (lore in ItemUtils.getLore(slot.stack)) {
                    val str = StringUtils.stripControlCodes(lore)
                    if (str.contains("Sell Price")) hugeRedFlags.add(RedFlags(slot,
                        "${EnumChatFormatting.YELLOW}This item is a ${EnumChatFormatting.RED}${EnumChatFormatting.BOLD}GLITCHED ITEM${EnumChatFormatting.RESET}${EnumChatFormatting.YELLOW} and is usually used in a scam.", WeighedInfraction.SERIOUS))
                    else if (str.contains("Sell Price")) hugeRedFlags.add(RedFlags(slot,
                        "${EnumChatFormatting.YELLOW}This item is a ${EnumChatFormatting.RED}${EnumChatFormatting.BOLD}GLITCHED ITEM${EnumChatFormatting.RESET}${EnumChatFormatting.YELLOW} and is usually used in a scam.", WeighedInfraction.SERIOUS))
                }
            }
            eventLog.addEvent("${EnumChatFormatting.GOLD}${EnumChatFormatting.BOLD}TRADE MENU UPDATED - ${timeLength.elapsed(TimeUnit.SECONDS)}s")
            SecretUtils.playLoudSound("random.orb", 0.5f)
            lastChanged = timeLength.elapsed(TimeUnit.SECONDS).toInt()
            alreadyRanForTheSecond = true
            val clonedItems: MutableList<Slot> = ArrayList()
            container.inventorySlots.forEach { clonedItems.add(it) }
            lastContainerSlots = clonedItems

            AsyncAwait.start({
                alreadyRanForTheSecond = false
            }, 3000)
        }
    }

    fun drawSlots(container: ContainerChest) {
        if (!container.lowerChestInventory.name.startsWith("You")) return
        hugeRedFlags.forEach {
            RenderUtils.renderTexturedRect(container.inventorySlots.size, it.slot.xDisplayPosition, it.slot.yDisplayPosition, ResourceLocation("secretmod", "gui/warning.png"))
        }

        if (timeLength.elapsed(TimeUnit.SECONDS).toInt() - lastChanged <= 10) {
            val it = container.getSlot(39)
            RenderUtils.renderTexturedRect(container.inventorySlots.size, it.xDisplayPosition, it.yDisplayPosition, ResourceLocation("secretmod", "gui/lock.png"))
        }
    }

    fun clickedSlot(slot: Slot): Boolean {
        if (slot.slotNumber == 39) {
            if (timeLength.elapsed(TimeUnit.SECONDS).toInt() - lastChanged <= 10) {
                return true
            }
        }
        return false
    }

    override fun onGuiClosed() {
        super.onGuiClosed()
        firstOpened = false
        isOpened = false
        lastContainerSlots.clear()
        eventLog.events.clear()
        hugeRedFlags.clear()
        timeLength.reset()
        lastChanged = -500
    }
}