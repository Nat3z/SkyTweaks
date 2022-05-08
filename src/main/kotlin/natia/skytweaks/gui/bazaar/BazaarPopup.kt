package natia.skytweaks.gui.bazaar

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.features.bazaar.Notifier
import natia.skytweaks.gui.base.HudElementModule
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.EnumChatFormatting
import java.awt.Color


class BazaarPopup() : HudElementModule(SkyTweaksConfig.bazaarOverlayHUD) {

    val mc = Minecraft.getMinecraft()

    override fun renderButton(mouseX: Int, mouseY: Int) {
        Gui.drawRect(SkyTweaksConfig.bazaarOverlayHUD.x, SkyTweaksConfig.bazaarOverlayHUD.y, SkyTweaksConfig.bazaarOverlayHUD.x + width, SkyTweaksConfig.bazaarOverlayHUD.y + height, Color(103, 99, 101, 150).rgb)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)

        mc.fontRendererObj.drawString("Current Orders", SkyTweaksConfig.bazaarOverlayHUD.x.toFloat() + 5,
            SkyTweaksConfig.bazaarOverlayHUD.y.toFloat() + 5, Color.WHITE.rgb, true)
        var y = 5
        Notifier.sellorders.forEach {
            y += 15

            mc.fontRendererObj.drawString("${it.displayName} ${EnumChatFormatting.GRAY}x${it.amount} ${EnumChatFormatting.DARK_GRAY} - ${EnumChatFormatting.BOLD}${if (Notifier.alreadyNotified.contains(it)) "${EnumChatFormatting.RED}UNDERCUT" else "${EnumChatFormatting.GREEN}GOOD"}"
                , SkyTweaksConfig.bazaarOverlayHUD.x.toFloat() + 5,
                SkyTweaksConfig.bazaarOverlayHUD.y.toFloat() + y, Color.WHITE.rgb, true)
        }
        if (Notifier.sellorders.isEmpty()) {
            mc.fontRendererObj.drawString("${EnumChatFormatting.DARK_GRAY}No Sell Orders Active ;-;"
                , SkyTweaksConfig.bazaarOverlayHUD.x.toFloat() + 5,
                SkyTweaksConfig.bazaarOverlayHUD.y.toFloat() + 15, Color.WHITE.rgb, true)
        }

        super.renderButton(mouseX, mouseY)
    }
}