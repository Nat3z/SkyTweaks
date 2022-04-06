package natia.skytweaks.features

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.core.SkyblockTimer
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting

import java.awt.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class TimersHook {
    private val mc = Minecraft.getMinecraft()

    fun render() {
        if (!SkyTweaksConfig.allowTimers) return
        if (mc.currentScreen != null) return
        val element = SkyTweaksConfig.timersHUD

        var y = -10
        val timersToRemove = ArrayList<SkyblockTimer>()

        for (timer in TimersHook.timers) {
            if (timer.timerStopwatch.isRunning() && !timer.hiddenTimer) {
                y += 10
                mc.fontRendererObj.drawString(timer.displayText + ": " +
                        EnumChatFormatting.GRAY + (timer.secondsUntil * 1000 - timer.timerStopwatch.elapsed(TimeUnit.MILLISECONDS)) as Double / 1000 + "s", element.x.toFloat(), (element.y + y).toFloat(), Color.white.rgb, true)

                if (timer.timerStopwatch.elapsed(TimeUnit.SECONDS) >= timer.secondsUntil) {
                    timer.timerStopwatch.reset()
                    timer.run()
                    timersToRemove.add(timer)
                }
            }
        }

        TimersHook.timers.removeAll(timersToRemove)
    }

    companion object {

        val timers: MutableList<SkyblockTimer> = ArrayList<SkyblockTimer>()

        fun addTimer(timer: SkyblockTimer) {
            timers.add(timer)
        }

        fun remove(timer: SkyblockTimer) {
            timers.remove(timer)
        }

        val instance = TimersHook()
    }


}
