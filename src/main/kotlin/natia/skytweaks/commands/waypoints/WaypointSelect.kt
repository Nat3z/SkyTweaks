package natia.skytweaks.commands.waypoints

import natia.skytweaks.features.waypoints.GlobalWaypoints
import natia.skytweaks.features.waypoints.Waypoint
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.SoundHandler
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Mouse
import java.awt.Color

class WaypointSelect(val buttonID: Int, val x: Int, val y: Int, val waypoint: Waypoint) : GuiButton(buttonID, x, y, 160, 20, "") {

    val mc = Minecraft.getMinecraft()

    override fun drawButton(mc1: Minecraft?, mouseX: Int, mouseY: Int) {
        super.drawButton(mc, mouseX, mouseY)
        if (!this.visible) return

        GlStateManager.enableAlpha()
        Gui.drawRect(x, y, x + this.width, y + this.height, Color(103, 99, 101).rgb)
        GlStateManager.disableAlpha()
        GlStateManager.color(1f, 1f, 1f)

        mc.fontRendererObj.drawString(waypoint.name, (x + 10).toFloat(), (y + 7).toFloat(), Color.WHITE.rgb, true)

        drawShareButton(mouseX, mouseY)
        drawRemoveButton(mouseX, mouseY)
    }

    fun drawShareButton(mouseX: Int, mouseY: Int) {
        val shareButtonX = x + 120
        val shareButtonY = y + 3
        val shareButtonWidth = 15
        val shareButtonHeight = 15

        val isShareHovered =
            mouseX >= shareButtonX && mouseY >= shareButtonY && mouseX < shareButtonX + shareButtonWidth && mouseY < shareButtonY + shareButtonHeight

        mc.textureManager.bindTexture(ResourceLocation("secretmod", "gui/share.png"))
        GlStateManager.enableAlpha()
        Gui.drawModalRectWithCustomSizedTexture(shareButtonX, shareButtonY, 0f, 0f, shareButtonWidth, shareButtonHeight,
            shareButtonWidth.toFloat(), shareButtonHeight.toFloat()
        )
        GlStateManager.disableAlpha()
        /* Click Event */
        if (!Mouse.getEventButtonState() || !isShareHovered) return
        mc.thePlayer.closeScreen()
        mc.thePlayer.sendChatMessage(waypoint.toShareable())
    }

    override fun playPressSound(soundHandlerIn: SoundHandler?) {

    }

    fun drawRemoveButton(mouseX: Int, mouseY: Int) {
        val removeButtonX = x + 140
        val removeButtonY = y + 3
        val removeButtonWidth = 15
        val removeButtonHeight = 15

        val isShareHovered =
            mouseX >= removeButtonX && mouseY >= removeButtonY && mouseX < removeButtonX + removeButtonWidth && mouseY < removeButtonY + removeButtonHeight

        mc.textureManager.bindTexture(ResourceLocation("secretmod", "gui/trash.png"))
        GlStateManager.enableAlpha()
        Gui.drawModalRectWithCustomSizedTexture(removeButtonX, removeButtonY, 0f, 0f, removeButtonWidth, removeButtonHeight,
            removeButtonWidth.toFloat(), removeButtonHeight.toFloat()
        )
        GlStateManager.disableAlpha()
        /* Click Event */
        if (!Mouse.getEventButtonState() || !isShareHovered) return
        mc.thePlayer.closeScreen()
        GlobalWaypoints.instance.waypoints.remove(waypoint)
    }
}