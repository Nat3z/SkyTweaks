package natia.skytweaks.features

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.core.ItemPickupEvent
import natia.skytweaks.SecretUtils
import natia.skytweaks.SkyTweaks
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.StringUtils

import java.util.ArrayList

class AlertPickups {

    internal var alertedItems: MutableList<String> = ArrayList()
    private val mc = Minecraft.getMinecraft()

    init {
        /* frags */
        alertedItems.add("Diamante's Handle")
        alertedItems.add("L.A.S.R.'s Eye")
        alertedItems.add("Bigfoot's Lasso")
        alertedItems.add("Jolly Pink Rock")
        /* diana */
        alertedItems.add("Chimera I")
        alertedItems.add("Dwarf Turtle Shelmet")
        alertedItems.add("Antique Remedies")
        alertedItems.add("Minos Relic")
        alertedItems.add("Daedalus Stick")
    }

    fun pickup(event: ItemPickupEvent) {
        if (event.getItemDiff().amount < 0) return
        if (mc.currentScreen != null) return
        if (!SkyTweaksConfig.alertRareDrops) return

        SkyTweaks.LOGGER.info("Pickup - " + event.getItemDiff().displayName)
        if (alertedItems.contains(StringUtils.stripControlCodes(event.getItemDiff().displayName))) {
            SecretUtils.playLoudSound("random.orb", 0.5f)
            SecretUtils.sendMessage(EnumChatFormatting.GOLD.toString() + "" + EnumChatFormatting.BOLD + "RARE DROP! " + EnumChatFormatting.RESET + event.getItemDiff().displayName)
        }
    }

    companion object {
        val instance = AlertPickups()
    }

}
