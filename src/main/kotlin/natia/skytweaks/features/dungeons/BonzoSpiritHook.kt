package natia.skytweaks.features.dungeons

import com.google.common.base.Stopwatch
import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.core.SkyblockTimer
import natia.skytweaks.features.TimersHook
import natia.skytweaks.utils.ItemUtils
import natia.skytweaks.SecretUtils
import net.minecraft.client.Minecraft
import net.minecraft.init.Items
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent

class BonzoSpiritHook {
    internal var mc = Minecraft.getMinecraft()
    internal var skyblockTimer: SkyblockTimer? = null
    internal var sbStopwatch = Stopwatch.createUnstarted()
    internal var cooldownSeconds = 999999999

    fun chat(event: ClientChatReceivedEvent) {
        if (!SkyTweaksConfig.maskTimers) return

        /* I dont know exact message lmao */
        val message = StringUtils.stripControlCodes(event.message.unformattedText)
        if (!message.contains(": ") && message.endsWith("saved your life!")) {
            /* yoinked from Danker's Skyblock Mod.
            * https://github.com/bowser0000/SkyblockMod/
             */
            val player = mc.thePlayer
            val bonzoMask = player.getCurrentArmor(3)
            if (bonzoMask != null && bonzoMask.item === Items.skull) {
                for (line in ItemUtils.getLore(bonzoMask)) {
                    val stripped = StringUtils.stripControlCodes(line)
                    if (stripped.startsWith("Cooldown: ")) {
                        if (sbStopwatch.isRunning) sbStopwatch.reset()
                        sbStopwatch.start()

                        cooldownSeconds = Integer.parseInt(stripped.replace("Cooldown: ", "").replace("s", ""))
                        skyblockTimer = SkyblockTimer(sbStopwatch,
                                EnumChatFormatting.GOLD.toString() + "Bonzo Mask Cooldown", cooldownSeconds, false, SecretUtils.generateEmptyUnit())
                        TimersHook.addTimer(skyblockTimer!!)
                        break
                    }
                }
            }
        }
    }

    companion object {

        val instance = BonzoSpiritHook()
    }

}
