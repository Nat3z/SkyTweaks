package natia.skytweaks.gui.base

import cc.blendingMC.vicious.HudElement
import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.features.bazaar.Notifier
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.EnumChatFormatting
import org.lwjgl.input.Mouse
import java.awt.Color

open class HudElementModule(private val attachedHUD: HudElement) : SkyblockButton(0, attachedHUD.x, attachedHUD.y, attachedHUD.width, attachedHUD.height, "") {
    var lastMouseX = -1
    var lastMouseY = -1
    override fun renderButton(mouseX: Int, mouseY: Int) {
        isMouseOver = mouseX >= attachedHUD.x && mouseY >= attachedHUD.y && mouseX < attachedHUD.x + width && mouseY < attachedHUD.y + height

        if (isMouseOver && Mouse.isButtonDown(0)) {
            val xMove = mouseX - lastMouseX
            val yMove = mouseY - lastMouseY
            attachedHUD.x += xMove
            attachedHUD.y += yMove
        }
        lastMouseX = mouseX
        lastMouseY = mouseY
    }
}