package natia.skytweaks.features.fishing

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.core.ItemPickupEvent
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent

import java.awt.*

class WormCounter {
    /* A flaming worm surfaces from the depths! */
    private val mc = Minecraft.getMinecraft()

    internal var wormsCaught = 0
    internal var wormsKilled = 0

    init {
        wormsCaught = SkyTweaksConfig.wormsCaught
        wormsKilled = SkyTweaksConfig.wormsCollected
    }

    fun chat(event: ClientChatReceivedEvent) {
        if (!SkyTweaksConfig.wormCounter) return

        if (event.message.unformattedText.startsWith("A flaming worm surfaces from the depths!")) {
            wormsCaught++
            SkyTweaksConfig.wormsCaught = wormsCaught
        }
    }

    fun render() {
        if (!SkyTweaksConfig.wormCounter) return
        if (mc.currentScreen != null) return

        val element = SkyTweaksConfig.wormCounterHUD

        /* Worms Caught */
        mc.fontRendererObj.drawString(EnumChatFormatting.LIGHT_PURPLE.toString() + "Worms Caught:", element.x.toFloat(), element.y.toFloat(), Color.WHITE.rgb, true)
        mc.fontRendererObj.drawString(EnumChatFormatting.LIGHT_PURPLE.toString() + "" + wormsCaught, (element.x + 80).toFloat(), element.y.toFloat(), Color.WHITE.rgb, true)

        /* Worms Killed */
        mc.fontRendererObj.drawString(EnumChatFormatting.DARK_PURPLE.toString() + "Worm Membrane:", element.x.toFloat(), (element.y + 10).toFloat(), Color.WHITE.rgb, true)
        mc.fontRendererObj.drawString(EnumChatFormatting.DARK_PURPLE.toString() + "" + wormsKilled, (element.x + 80).toFloat(), (element.y + 10).toFloat(), Color.WHITE.rgb, true)
    }

    fun onCollect(event: ItemPickupEvent) {
        if (mc.currentScreen != null) return

        if (StringUtils.stripControlCodes(event.getItemDiff().displayName).contains("Worm Membrane")) {
            wormsKilled++
            SkyTweaksConfig.wormsCollected = wormsKilled

        }
    }

    companion object {

        val instance = WormCounter()
    }
}
