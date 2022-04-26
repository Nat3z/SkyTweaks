package natia.skytweaks.features.dungeons

import com.google.common.base.Stopwatch
import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.SecretUtils
import natia.skytweaks.SecretUtils.parsable
import natia.skytweaks.SkyTweaks
import natia.skytweaks.features.waypoints.Waypoint
import natia.skytweaks.utils.AsyncAwait
import natia.skytweaks.utils.Location
import net.minecraft.client.Minecraft
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import org.lwjgl.input.Keyboard
import java.awt.Color
import java.util.concurrent.TimeUnit

class NotifyCrush {

    val mc = Minecraft.getMinecraft()
    val crushWatch = Stopwatch.createUnstarted()
    fun render() {
        if (!crushWatch.isRunning) return
        if (!SkyTweaksConfig.crushStorm) return
        SecretUtils.sendTitleCentered("Crush Storm!", Color.red.rgb)

        if (crushWatch.elapsed(TimeUnit.SECONDS) >= 3) crushWatch.reset()
    }

    fun chat(event: ClientChatReceivedEvent) {
        if (crushWatch.isRunning) return
        if (!SkyTweaksConfig.crushStorm) return
        if (Location.currentLocation != Location.THE_CATACOMBS) return
        try {
            if (!event.message.unformattedText.contains(": ")) return
            val message = event.message.unformattedText.split(": ", limit = 2)[1]
            // user sending
            if (message == "@SkyTweaks-F7Crush") {
                val userSending = event.message.unformattedText.split(": ")[0].split(" ")[
                        event.message.unformattedText.split(": ")[0].split(" ").size - 1
                ]
                event.isCanceled = true
                if (userSending == mc.thePlayer.name) return
                crushWatch.start()

            }
        } catch (ex: Exception) {
            event.isCanceled = true
        }
    }

    fun key() {
        if (Keyboard.getEventKey() != SkyTweaks.NOTIFY_CRUSH.keyCode) return
        mc.thePlayer.sendChatMessage("/pc @SkyTweaks-F7Crush")
    }

    companion object {
        val instance = NotifyCrush()
    }
}