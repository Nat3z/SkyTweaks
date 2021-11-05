package com.natia.secretmod.features.bazaar;

import com.natia.secretmod.utils.SuggestedName;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BazaarHook {

    /* Sell Offer Setup! 1x Sugar Cane for 2.1 Coins. */
    /* Cancelled! Refunded 1x Raw Rabbit from cancelling sell offer! */
    Minecraft mc = Minecraft.getMinecraft();
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String chat = event.message.getUnformattedText();

        if (chat.startsWith("Sell Offer Setup!")) {
            final String[] selloffersplit = chat.split("x ", 2);
            final int itemQuantity = Integer.parseInt(selloffersplit[0].replace("Sell Offer Setup! ", ""));

            final String itemName = selloffersplit[1].split(" for ")[0];
            final double coins = Double.parseDouble(selloffersplit[1].split(" for ")[1].replace("coins.", "").replace(" ", ""));
            new Thread(() -> {
                String suggested = SuggestedName.getSuggested(itemName);
                Notifier.orders.add(new BazaarOrder(itemQuantity, suggested, coins));
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Added " + suggested + " to Sell Offers List."));
            }).start();
        }
        else if (chat.startsWith("Cancelled!") && chat.endsWith("sell offer!")) {
            String[] chats = chat.split(" ");
            final String[] selloffersplit = chat.split("x ", 2);
            final String itemName = SuggestedName.getSuggested(selloffersplit[1].split(" from ")[0]);
            final int quantity = Integer.parseInt(selloffersplit[0].split("Cancelled! Refunded ", 2)[1]);

            for (BazaarOrder order : Notifier.orders) {
                /* Got the trailing Bazaar order. Remove from orders sheet. */
                if (order.getItem().equals(itemName) && order.getAmount() == quantity) {
                    Notifier.orders.remove(order);
                    mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Removed " + itemName + " from Sell Offers."));
                    break;
                }
            }
        }
    }

    public static String replaceLast(String string, String toReplace, String replacement) {
        int pos = string.lastIndexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos)
                    + replacement
                    + string.substring(pos + toReplace.length());
        } else {
            return string;
        }
    }

}
