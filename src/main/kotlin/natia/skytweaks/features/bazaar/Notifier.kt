package natia.skytweaks.features.bazaar

import com.google.common.base.Stopwatch
import natia.skytweaks.SecretUtils
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting

import java.util.ArrayList
import java.util.concurrent.TimeUnit

class Notifier {
    internal var mc = Minecraft.getMinecraft()

    internal var stopwatch = Stopwatch.createUnstarted()
    internal var tick = 0

    fun tick() {
        if (mc.thePlayer == null) return
        tick++

        /* do check every 10 ticks. */
        if (tick % 10 == 0) {
            if (!stopwatch.isRunning) stopwatch.start()

            /* 10 Seconds have elapsed. Start checks. */
            if (stopwatch.elapsed(TimeUnit.SECONDS) >= 20) {
                /* resets Stopwatch. */
                stopwatch.reset()
                if (!SecretUtils.bazaarCached.get("success").asBoolean) return

                val products = SecretUtils.bazaarCached.get("products").asJsonObject
                for (order in sellorders) {
                    if (alreadyNotified.contains(order)) continue

                    val item = order.item
                    if (products.has(item)) {
                        val sells = products.get(item).asJsonObject.get("quick_status").asJsonObject.get("buyPrice").asDouble

                        if (order.coins > sells) {
                            alreadyNotified.add(order)
                            mc.thePlayer.addChatComponentMessage(ChatComponentText(
                                    EnumChatFormatting.AQUA.toString() + "Your Bazaar Sell Offer for " + EnumChatFormatting.DARK_AQUA + order.displayName + EnumChatFormatting.AQUA + " was undercut!"
                            ))
                        }
                    }
                }
            }
        }

    }

    companion object {

        var sellorders: MutableList<BazaarOrder> = ArrayList()
        val alreadyNotified: MutableList<BazaarOrder> = ArrayList<BazaarOrder>()

        val instance = Notifier()
    }

}
