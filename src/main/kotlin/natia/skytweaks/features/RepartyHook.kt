package natia.skytweaks.features

import mixin.natia.skytweaks.SkyTweaksConfig
import net.minecraft.client.Minecraft
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent

import java.util.ArrayList

class RepartyHook {
    private val mc = Minecraft.getMinecraft()
    var cancelChats = false

    var partyMembers: MutableList<String> = ArrayList()

    fun chat(event: ClientChatReceivedEvent) {
        if (!SkyTweaksConfig.rpCommand) return
        /* reparty system */
        val message = event.message.unformattedText
        if (cancelChats && message.startsWith("-----------------------------")) {
            event.isCanceled = true
        }
        if (cancelChats && (message.startsWith("Party M") || message.startsWith("Party L"))) {
            if (message.startsWith("Party Members (")) {
                event.isCanceled = true
            } else {
                val split = message.replace("Party " + message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1], "").split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (string in split) {
                    if (!string.contains("]") && !string.contains("●")) {
                        partyMembers.add(StringUtils.stripControlCodes(string).replace(" ", ""))
                    }
                }

                if (message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1] == "Members:")
                    cancelChats = false
                event.isCanceled = true
            }
        }
    }

    fun worldLoad() {
        cancelChats = false
    }

    companion object {
        val instance = RepartyHook()
    }
}
