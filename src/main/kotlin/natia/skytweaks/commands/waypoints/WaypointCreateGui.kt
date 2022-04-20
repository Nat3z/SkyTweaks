package natia.skytweaks.commands.waypoints

import natia.skytweaks.features.waypoints.GlobalWaypoints
import natia.skytweaks.features.waypoints.Waypoint
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.GuiTextField
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import org.lwjgl.input.Keyboard
import java.awt.Color

class WaypointCreateGui(val pos: BlockPos) : GuiScreen() {

    var waypointName: GuiTextField? = null
    override fun initGui() {
        super.initGui()
        waypointName = GuiTextField(0, fontRendererObj, width / 2 - 80, 50, 200, 15)
        waypointName!!.isFocused = true;
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawCenteredString(mc.fontRendererObj, "Waypoint Name", width / 2,
            20, Color.GRAY.rgb)
        waypointName!!.drawTextBox()
        super.updateScreen()
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        waypointName!!.mouseClicked(mouseX, mouseY, mouseButton)
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        waypointName!!.textboxKeyTyped(typedChar, keyCode)
        super.keyTyped(typedChar, keyCode)
    }

    override fun onGuiClosed() {
        super.onGuiClosed()
        exit()
    }

    fun exit() {
        GlobalWaypoints.instance.waypoints.add(Waypoint(pos, waypointName!!.text))
    }

}