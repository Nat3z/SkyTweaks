package natia.skytweaks.features

import com.google.common.base.Stopwatch
import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.SecretUtils
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import java.util.concurrent.TimeUnit

class SlayerContracts {

    var slayerBossSpawnedStopwatch: Stopwatch = Stopwatch.createUnstarted()
    var slayerBossUnspawned: Stopwatch = Stopwatch.createUnstarted()

    fun chat(event: ClientChatReceivedEvent) {
        if (!SkyTweaksConfig.slayerTimeNotify) return

        if (event.message.unformattedText.contains(":")) return
        if (event.message.unformattedText.contains("SLAYER QUEST COMPLETE!") || event.message.unformattedText.contains("SLAYER QUEST FAILED!")) {
            SecretUtils.sendMessage("It took you ${EnumChatFormatting.BOLD}${EnumChatFormatting.RED}" + slayerBossUnspawned.elapsed(TimeUnit.SECONDS) + "s ${EnumChatFormatting.RESET}${EnumChatFormatting.YELLOW}to spawn the slayer boss.")
            SecretUtils.sendMessage("Your Slayer Boss took you ${EnumChatFormatting.BOLD}${EnumChatFormatting.RED}" + slayerBossSpawnedStopwatch.elapsed(TimeUnit.SECONDS) + "s ${EnumChatFormatting.RESET}${EnumChatFormatting.YELLOW}to kill.")
            slayerBossSpawnedStopwatch.reset()
            slayerBossUnspawned.reset()
        } else if (event.message.unformattedText.contains("SLAYER QUEST STARTED!")) {
            slayerBossUnspawned.start()
        }
    }
    val mc = Minecraft.getMinecraft()
    fun tick() {
        if (!SkyTweaksConfig.slayerTimeNotify) return
        if (!slayerBossUnspawned.isRunning) return
        if (mc.theWorld == null) return

        SecretUtils.scoreboardLines.forEach {
            val scoreboard = SecretUtils.cleanSB(it)
            if (scoreboard.contains("Slay the boss!")) {
                slayerBossUnspawned.stop()
                if (!slayerBossSpawnedStopwatch.isRunning)
                    slayerBossSpawnedStopwatch.start()
            }
        }
    }

    companion object {
        val instance = SlayerContracts()
    }
}