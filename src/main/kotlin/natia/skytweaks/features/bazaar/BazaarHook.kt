package natia.skytweaks.features.bazaar

import natia.skytweaks.utils.SuggestedName
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import java.util.regex.Pattern

class BazaarHook {

    /* Sell Offer Setup! 1x Sugar Cane for 2.1 Coins. */
    /* Cancelled! Refunded 1x Raw Rabbit from cancelling sell offer! */
    internal var mc = Minecraft.getMinecraft()

    fun chat(event: ClientChatReceivedEvent) {
        val chat = event.message.unformattedText

        if (chat.startsWith("Sell Offer Setup!")) {
            val selloffersplit = chat.split("x ".toRegex(), 2).toTypedArray()
            val itemQuantity = Integer.parseInt(selloffersplit[0].replace("Sell Offer Setup! ", ""))

            val itemName = selloffersplit[1].split(" for ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            val coins = java.lang.Double.parseDouble(selloffersplit[1]
                .split(" for ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                .replace("coins.", "").replace(" ", "").replace(",", ""))
            Thread {
                val suggested = SuggestedName.getSuggested(itemName)
                Notifier.sellorders.add(BazaarOrder(itemQuantity.toFloat(), suggested, coins, itemName))
                mc.thePlayer.addChatComponentMessage(ChatComponentText(EnumChatFormatting.AQUA.toString() + "Added " + itemName + " to Sell Offers List."))
            }.start()
        } else if (chat.startsWith("Cancelled!") && chat.endsWith("sell offer!")) {
            val chats = chat.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val selloffersplit = chat.split("x ".toRegex(), 2).toTypedArray()
            val itemName = SuggestedName.getSuggested(selloffersplit[1].split(" from ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
            val quantity = Integer.parseInt(selloffersplit[0].split("Cancelled! Refunded ".toRegex(), 2).toTypedArray()[1])

            for (order in Notifier.sellorders) {
                /* Got the trailing Bazaar order. Remove from orders sheet. */
                if (order.item == itemName && order.amount == quantity.toFloat()) {
                    Notifier.sellorders.remove(order)
                    if (Notifier.alreadyNotified.contains(order))
                        Notifier.alreadyNotified.remove(order)
                    mc.thePlayer.addChatComponentMessage(ChatComponentText(EnumChatFormatting.AQUA.toString() + "Removed " + order.displayName + " from Sell Offers."))
                    break
                }
            }
        } else if (chat.startsWith("[Bazaar] Your Sell Offer for")) {
            val selloffersplit = chat.split("x ".toRegex(), 2).toTypedArray()
            val itemName = SuggestedName.getSuggested(selloffersplit[1].split(" was")[0])
            val quantity = Integer.parseInt(selloffersplit[0].replace("[Bazaar] Your Sell Offer for ", ""))

            for (order in Notifier.sellorders) {
                /* Got the trailing Bazaar order. Remove from orders sheet. */
                if (order.item == itemName && order.amount == quantity.toFloat()) {
                    Notifier.sellorders.remove(order)
                    if (Notifier.alreadyNotified.contains(order))
                        Notifier.alreadyNotified.remove(order)
                    mc.thePlayer.addChatComponentMessage(ChatComponentText(EnumChatFormatting.AQUA.toString() + "Removed " + order.displayName + " from Sell Offers."))
                    break
                }
            }
        }
    }

    companion object {

        fun replaceLast(string: String, toReplace: String, replacement: String): String {
            val pos = string.lastIndexOf(toReplace)
            return if (pos > -1) {
                (string.substring(0, pos)
                        + replacement
                        + string.substring(pos + toReplace.length))
            } else {
                string
            }
        }

        val instance = BazaarHook()
    }

}
