package natia.skytweaks.gui.pets

import natia.skytweaks.gui.base.SkyblockButton
import natia.skytweaks.utils.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack

import java.awt.*

class ItemButton(private val stack: ItemStack, var slotNumber: Int,
                 id: Int, x: Int, y: Int, var tooltip: List<String>) : SkyblockButton(id, x, y, 20, 20, "") {

    override fun renderButton(mouseX: Int, mouseY: Int) {
        if (visible) {
            this.mouseX = mouseX
            this.mouseY = mouseY
            this.isMouseOver = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height

            if (isMouseOver) {
                val sr = ScaledResolution(Minecraft.getMinecraft())
                RenderUtils.drawHoveringText(listOf(stack.displayName),
                        0, 20,
                        sr.scaledWidth, sr.scaledHeight, -1, Minecraft.getMinecraft().fontRendererObj)
            }

            Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, Color(59, 124, 130, 255).rgb)

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            RenderUtils.renderItem(stack, xPosition + 2, yPosition + 2, 1f)
        }
    }
}
