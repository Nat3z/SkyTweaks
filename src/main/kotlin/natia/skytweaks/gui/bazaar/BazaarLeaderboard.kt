package natia.skytweaks.gui.bazaar

import com.google.common.base.Stopwatch
import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.SecretUtils
import natia.skytweaks.SecretUtils.readable
import natia.skytweaks.SkyTweaks
import natia.skytweaks.core.SkyblockTimer
import natia.skytweaks.features.TimersHook
import natia.skytweaks.features.bazaar.BazaarAverage
import natia.skytweaks.gui.base.HudElementModule
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.EnumChatFormatting
import org.lwjgl.input.Mouse
import java.awt.Color
import kotlin.math.floor

class BazaarLeaderboard : HudElementModule(SkyTweaksConfig.bazaarLeaderboardHUD) {

    val mc = Minecraft.getMinecraft()
    var pastMostProfit: MutableList<BazaarAverage> = ArrayList()
    var mostProfitable: MutableList<BazaarAverage> = ArrayList()
    var watch = Stopwatch.createUnstarted()
    var timer = SkyblockTimer(watch, "Bazaar Refresh", 90, true) {
        Thread {
            mostProfitable.clear()
            SecretUtils.averageBazaar().forEach {
                val sellprice =
                    SecretUtils.bazaarCached.get("products").asJsonObject.get(it.id).asJsonObject.get("quick_status").asJsonObject.get(
                        "buyPrice"
                    ).asDouble
                val buyPrice =
                    SecretUtils.bazaarCached.get("products").asJsonObject.get(it.id).asJsonObject.get("quick_status").asJsonObject.get(
                        "sellPrice"
                    ).asDouble
                if (it.calcAverage() - buyPrice >= 15000 && SecretUtils.coins > buyPrice + (it.calcAverage() - buyPrice))
                    mostProfitable.add(it)
            }
            pastMostProfit = ArrayList(mostProfitable)
        }.start()
    }

    override fun renderButton(mouseX: Int, mouseY: Int) {
        Gui.drawRect(SkyTweaksConfig.bazaarLeaderboardHUD.x, SkyTweaksConfig.bazaarLeaderboardHUD.y, SkyTweaksConfig.bazaarLeaderboardHUD.x + width, SkyTweaksConfig.bazaarLeaderboardHUD.y + height, Color(103, 99, 101, 150).rgb)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)

        mc.fontRendererObj.drawString("Profitable Items", SkyTweaksConfig.bazaarLeaderboardHUD.x.toFloat() + 5,
            SkyTweaksConfig.bazaarLeaderboardHUD.y.toFloat() + 5, Color.WHITE.rgb, true)
        var y = 5

        var amountOfAdds = 0
        if (!watch.isRunning) {
            watch.start()
            TimersHook.addTimer(timer)
        }
        for (it in mostProfitable) {
            y += 15
            if (amountOfAdds == 5) break
            amountOfAdds++
            val buyprice = SecretUtils.bazaarCached.get("products").asJsonObject.get(it.id).asJsonObject.get("quick_status").asJsonObject.get("buyPrice").asDouble
            val currprice = SecretUtils.bazaarCached.get("products").asJsonObject.get(it.id).asJsonObject.get("quick_status").asJsonObject.get("sellPrice").asDouble
            mc.fontRendererObj.drawString("${it.displayName} ${EnumChatFormatting.DARK_GRAY} - ${EnumChatFormatting.GREEN}+${floor(it.calcAverage() - currprice).toInt().readable()}", SkyTweaksConfig.bazaarLeaderboardHUD.x.toFloat() + 5,
                SkyTweaksConfig.bazaarLeaderboardHUD.y.toFloat() + y, Color.WHITE.rgb, true)
        }

        if (mostProfitable.isEmpty()) {
            mc.fontRendererObj.drawString("${EnumChatFormatting.DARK_GRAY}Nothing Good Yet ;-;", SkyTweaksConfig.bazaarLeaderboardHUD.x.toFloat() + 5,
                SkyTweaksConfig.bazaarLeaderboardHUD.y.toFloat() + 15, Color.WHITE.rgb, true)
        }
        super.renderButton(mouseX, mouseY)
    }
}