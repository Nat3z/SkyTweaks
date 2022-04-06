package natia.skytweaks.features.fishing

import com.google.common.base.Stopwatch
import natia.skytweaks.SecretUtils
import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.core.SkyblockTimer
import natia.skytweaks.features.TimersHook
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent

import java.awt.*
import java.util.concurrent.TimeUnit

class WormTimer {

    private var skyblockTimer: SkyblockTimer? = null
    private val mc = Minecraft.getMinecraft()
    private val notify = Stopwatch.createUnstarted()
    private var alreadyTimed = false
    fun render() {
        if (mc.currentScreen != null) return

        if (notify.isRunning) {
            SecretUtils.sendTitleCentered(SkyTweaksConfig.wormFishText, Color.RED.rgb)

            if (notify.elapsed(TimeUnit.SECONDS) >= 3) {
                notify.reset()
            }
        }
    }

    /* A flaming worm surfaces from the depths! */
    fun chat(event: ClientChatReceivedEvent) {
        if (!SkyTweaksConfig.wormTimer) return

        if (event.message.unformattedText.startsWith("A flaming worm surfaces from the depths!")) {
            if (!alreadyTimed) {
                alreadyTimed = true

                skyblockTimer = SkyblockTimer(Stopwatch.createStarted(),
                        EnumChatFormatting.LIGHT_PURPLE.toString() + "Kill Pit Worms", 280, false) {
                    alreadyTimed = false
                    SecretUtils.playLoudSound("random.orb", 0.5f)
                    notify.start()
                }

                TimersHook.addTimer(skyblockTimer!!)
            }
        }
    }

    companion object {


        val instance = WormTimer()
    }

}
