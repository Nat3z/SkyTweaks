package com.natia.secretmod.features.bazaar;

import com.google.common.base.Stopwatch;
import com.google.gson.JsonObject;
import com.natia.secretmod.SecretUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.natia.secretmod.utils.WebUtils.fetch;

public class Notifier {

    public static List<BazaarOrder> orders = new ArrayList<>();
    private List<BazaarOrder> alreadyNotified = new ArrayList<>();
    Minecraft mc = Minecraft.getMinecraft();

    Stopwatch stopwatch = Stopwatch.createUnstarted();
    int tick = 0;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.thePlayer == null) return;
        tick++;

        /* do check every 10 ticks. */
        if (tick % 10 == 0) {
            if (!stopwatch.isRunning()) stopwatch.start();

            /* 10 Seconds have elapsed. Start checks. */
            if (stopwatch.elapsed(TimeUnit.SECONDS) >= 10) {
                /* resets Stopwatch. */
                stopwatch.reset();
                if (!SecretUtils.bazaarCached.get("success").getAsBoolean()) return;

                JsonObject products = SecretUtils.bazaarCached.get("products").getAsJsonObject();
                for (BazaarOrder order : orders) {
                    if (alreadyNotified.contains(order)) continue;

                    String item = order.getItem().toUpperCase().replace(" ", "_");
                    if (products.has(item)) {
                        double sells = products.get(item).getAsJsonObject().get("quick_status").getAsJsonObject().get("buyPrice").getAsDouble();

                        if (order.getCoins() > sells) {
                            alreadyNotified.add(order);
                            mc.thePlayer.addChatComponentMessage(new ChatComponentText(
                                    EnumChatFormatting.AQUA + "Your Bazaar Sell Offer for " + EnumChatFormatting.DARK_AQUA + order.getItem() + EnumChatFormatting.AQUA + " was undercut!"
                            ));
                        }
                    }
                }
            }
        }

    }

}
