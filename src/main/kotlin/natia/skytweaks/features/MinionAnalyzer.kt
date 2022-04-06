package natia.skytweaks.features

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.utils.ItemUtils
import natia.skytweaks.SecretUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraftforge.client.event.GuiScreenEvent
import org.lwjgl.opengl.GL11

import java.awt.*
import java.text.DecimalFormat
import java.util.ArrayList

class MinionAnalyzer {
    internal var mc = Minecraft.getMinecraft()
    private var moneyInMinion = 0.0

    internal var invalidSlots: MutableList<Int> = ArrayList()

    init {
        invalidSlots.add(3)
        invalidSlots.add(4)
        invalidSlots.add(5)
        invalidSlots.add(10)
        invalidSlots.add(19)
        invalidSlots.add(28)
        invalidSlots.add(37)
        invalidSlots.add(46)
        invalidSlots.add(48)
        invalidSlots.add(50)
        invalidSlots.add(53)
    }

    fun backgroundDrawn(event: GuiScreenEvent.BackgroundDrawnEvent) {
        if (!SkyTweaksConfig.minionAnalyzer) return
        if (mc.theWorld == null) return
        if (!SecretUtils.isValid) return
        moneyInMinion = 0.0
        if (event.gui is GuiChest) {
            val guiChest = event.gui as GuiChest
            val container = guiChest.inventorySlots as ContainerChest
            val inventory = container.lowerChestInventory
            if (inventory.displayName.unformattedText.contains(" Minion")) {
                /* multi thread */
                for (slot in container.inventorySlots) {
                    if (slot == null) continue
                    if (slot.stack == null) continue
                    if (invalidSlots.contains(slot.slotNumber)) continue
                    if (slot.slotNumber > 53) break

                    val type = ItemUtils.getItemType(slot.stack)
                    if (type != null && SecretUtils.bazaarCached.get("products").asJsonObject.has(type)) {
                        val coins = SecretUtils.bazaarCached.get("products").asJsonObject
                                .get(type).asJsonObject.get("buy_summary").asJsonArray.get(0)
                                .asJsonObject.get("pricePerUnit").asDouble
                        moneyInMinion += coins * slot.stack.stackSize
                    }
                }
                moneyInMinion = Math.ceil(moneyInMinion)

                /* text renderer */
                val sr = ScaledResolution(mc)
                val guiLeft = (sr.scaledWidth - 176) / 2
                val guiTop = (sr.scaledHeight - 222) / 2

                val x = guiLeft + 85
                val y = guiTop + 6.6.toInt()
                val myFormatter = DecimalFormat("###,###,###")
                val output = myFormatter.format(moneyInMinion)
                GL11.glTranslated(0.0, 0.0, 1.0)
                if (moneyInMinion != -1.0)
                    Minecraft.getMinecraft().fontRendererObj.drawString("Minion Value: +$output", x.toFloat(), y.toFloat(), Color.GREEN.rgb, true)
                else
                    Minecraft.getMinecraft().fontRendererObj.drawString("Unable to get Minion Value.", x.toFloat(), y.toFloat(), Color.GRAY.rgb, true)
                GL11.glTranslated(0.0, 0.0, -1.0)
            }
        }
    }

    companion object {

        val instance = MinionAnalyzer()
    }

}
