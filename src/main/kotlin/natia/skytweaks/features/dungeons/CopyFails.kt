package natia.skytweaks.features.dungeons

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.utils.Location
import natia.skytweaks.SecretUtils
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent

import java.awt.*
import java.awt.datatransfer.StringSelection

class CopyFails {
    internal var mc = Minecraft.getMinecraft()

    fun chat(event: ClientChatReceivedEvent) {
        if (!SkyTweaksConfig.copyFails) return
        if (SecretUtils.isInDungeons !== Location.THE_CATACOMBS) return

        val message = event.message.unformattedText
        if (message.contains(": "))
            return

        if (SecretUtils.isValid && SecretUtils.isInDungeons === Location.THE_CATACOMBS) {
            if (message.contains("You were killed by") || message.contains(" was killed by") || message.contains("died to a")) {
                val stringSelection = StringSelection(message)
                val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                clipboard.setContents(stringSelection, null)
                SecretUtils.sendMessage("Death copied!")
            } else if (message.contains("Yikes!")) {
                val stringSelection = StringSelection(message)
                val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                clipboard.setContents(stringSelection, null)
                SecretUtils.sendMessage("Fail copied!")
            }
        }
    }

    companion object {

        val instance = CopyFails()
    }

}
