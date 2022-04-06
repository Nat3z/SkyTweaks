package natia.skytweaks.features.slayers

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.SecretUtils
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent

import java.awt.*
import java.awt.datatransfer.StringSelection

class CopyRNG {
    internal var mc = Minecraft.getMinecraft()

    fun chat(event: ClientChatReceivedEvent) {
        if (!SkyTweaksConfig.copyRNG) return

        val message = event.message.unformattedText
        if (message.contains(": "))
            return

        if (SecretUtils.isValid) {
            if (message.startsWith("CRAZY RARE DROP!")) {
                val stringSelection = StringSelection(message)
                val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                clipboard.setContents(stringSelection, null)
                SecretUtils.sendMessage("RNGesus Drop Copied!")
            }
        }
    }

    companion object {

        val instance = CopyRNG()
    }
}
