package natia.skytweaks.gui.trade

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.gui.base.HudElementModule
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import java.awt.Color
import java.util.*
import kotlin.collections.ArrayList

class EventLog : HudElementModule(SkyTweaksConfig.tradeLogHUD) {

    var events: MutableList<String?> = ArrayList()
    val mc = Minecraft.getMinecraft()

    override fun renderButton(mouseX: Int, mouseY: Int) {
        Gui.drawRect(SkyTweaksConfig.tradeLogHUD.x, SkyTweaksConfig.tradeLogHUD.y, SkyTweaksConfig.tradeLogHUD.x + width, SkyTweaksConfig.tradeLogHUD.y + height, Color(103, 99, 101, 150).rgb)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)

        var y = 0
        for (it in events.asReversed()) {
            y += 15
            mc.fontRendererObj.drawString("$it", SkyTweaksConfig.tradeLogHUD.x + 5, SkyTweaksConfig.tradeLogHUD.y + y, Color.WHITE.rgb)
        }
        super.renderButton(mouseX, mouseY)
    }

    fun addEvent(eventName: String) {
        events.add(eventName)
    }
}