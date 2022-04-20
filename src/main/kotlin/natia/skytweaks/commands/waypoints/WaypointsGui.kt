package natia.skytweaks.commands.waypoints

import cc.blendingMC.vicious.ColoredButton
import natia.skytweaks.features.waypoints.GlobalWaypoints
import natia.skytweaks.hooks.TickHook
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.init.Blocks
import net.minecraft.util.EnumChatFormatting
import java.awt.Color

class WaypointsGui : GuiScreen() {

    val addWaypoints = ColoredButton(0, 10, 10, 170, 30, "Add Waypoint at Current Location", false)
    override fun initGui() {

        // 110 because that's width of waypointselect button
        val x = this.width / 2 - 80
        var y = 20
        var buttonID = 0
        for (wp in GlobalWaypoints.instance.waypoints) {
            y += 25
            buttonID++
            this.buttonList.add(WaypointSelect(buttonID, x, y, wp))
        }

        this.buttonList.add(addWaypoints)

        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawDefaultBackground()
        if (GlobalWaypoints.instance.waypoints.isEmpty()) {
            drawCenteredString(mc.fontRendererObj, "No Waypoints Found ;-;", width / 2,
                (ScaledResolution(mc).scaledHeight * 0.5).toInt(), Color.GRAY.rgb)
        } else {
            drawCenteredString(mc.fontRendererObj, "${EnumChatFormatting.UNDERLINE}Waypoints", width / 2,
                20, Color.WHITE.rgb)
        }
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun actionPerformed(button: GuiButton?) {
        if (button == addWaypoints && mc.objectMouseOver != null && mc.objectMouseOver.blockPos != null &&
            mc.theWorld.getBlockState(mc.objectMouseOver.blockPos!!).block!! != Blocks.air) {
            mc.thePlayer.closeScreen()
            TickHook.scheduleGui(WaypointCreateGui(mc.objectMouseOver.blockPos))
        }
        super.actionPerformed(button)
    }
}