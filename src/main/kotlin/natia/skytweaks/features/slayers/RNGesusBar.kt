package natia.skytweaks.features.slayers

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.SecretUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent

import java.awt.*

class RNGesusBar {

    private var RNGesusMeter = ""
    fun chat(event: ClientChatReceivedEvent) {
        if (event.message.unformattedText.startsWith("    RNGesus Meter:")) {
            RNGesusMeter = event.message.formattedText.replace("    ", "")
                    .replace("RNGesus", "")
                    .replace("Meter", "")
                    .replace(":", "")
                    .replace("  ", "")
        }
    }

    fun render() {
        if (!SkyTweaksConfig.rngMeter) return
        val element = SkyTweaksConfig.rngesusmeter
        SecretUtils.drawCenteredString(RNGesusMeter, element.x - 100, element.y, Color.WHITE.rgb, 1.0)
    }

    companion object {

        val instance = RNGesusBar()
    }

}
