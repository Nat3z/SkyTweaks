package natia.skytweaks.features

import com.google.common.base.Stopwatch
import natia.skytweaks.SecretUtils
import natia.skytweaks.SkyTweaks
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.awt.Color
import java.util.concurrent.TimeUnit

class SummonsAlert {
    val mc = Minecraft.getMinecraft()
    var respawnSummons = false
    fun tick() {
        if (mc.theWorld == null) return
        if (respawnSummons) return
        for (entity in mc.theWorld.getEntitiesWithinAABB(Entity::class.java, mc.thePlayer.entityBoundingBox.expand(15.0, 10.0, 15.0))) {
            val name = StringUtils.stripControlCodes(entity.name)
            if (!name.endsWith("❤")) return
            SkyTweaks.LOGGER.info(name)
            val splitBySpace = name.split(" ")
            val currentHealth = Integer.parseInt(
                splitBySpace[splitBySpace.size - 1].split("❤")[0]
                .replace("[kMB]|,".toRegex(), ""))
            SkyTweaks.LOGGER.info(currentHealth)
            if (splitBySpace[splitBySpace.size - 1].contains("k")) currentHealth * 1000
            if (splitBySpace[splitBySpace.size - 1].contains("M")) currentHealth * 1000000
            if (splitBySpace[splitBySpace.size - 1].contains("B")) currentHealth * 1000000000

            respawnSummons = currentHealth < 300000
            if (respawnSummons) SecretUtils.playLoudSound("random.orb", 0.5f)
            if (respawnSummons) break
        }
    }

    fun chat(event: ClientChatReceivedEvent) {
        if (event.message.unformattedText.equals("You have despawned your monster!")) {
            respawnSummons = false
        }
    }
    fun render() {
        if (respawnSummons) {
            SecretUtils.sendTitleCentered("Respawn Your Monsters", Color.red.rgb)
        }
    }

    companion object {
        val instance = SummonsAlert()
    }
}