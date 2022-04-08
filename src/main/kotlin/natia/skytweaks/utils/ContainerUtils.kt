package natia.skytweaks.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.opengl.GL11

object ContainerUtils {
    fun showOnSlot(size: Int, xSlotPos: Int, ySlotPos: Int, color: Int) {
        val sr = ScaledResolution(Minecraft.getMinecraft())
        val guiLeft = (sr.scaledWidth - 176) / 2
        val guiTop = (sr.scaledHeight - 222) / 2

        val x = guiLeft + xSlotPos
        var y = guiTop + ySlotPos

        if (size != 90) y += (6 - (size - 36) / 9) * 9


        GL11.glTranslated(0.0, 0.0, 1.0)
        Gui.drawRect(x, y, x + 16, y + 16, color)
        GL11.glTranslated(0.0, 0.0, -1.0)

    }
}
