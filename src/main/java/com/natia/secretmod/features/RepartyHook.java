package com.natia.secretmod.features;

import com.natia.secretmod.config.SecretModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class RepartyHook {
    private Minecraft mc = Minecraft.getMinecraft();
    public boolean cancelChats = false;

    public List<String> partyMembers = new ArrayList<>();
    private static RepartyHook INSTANCE = new RepartyHook();

    public static RepartyHook getInstance() {
        return INSTANCE;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void checkIfReady(ClientChatReceivedEvent event) {
        if (!SecretModConfig.rpCommand) return;
        /* reparty system */
        String message = event.message.getUnformattedText();
        if (cancelChats && message.startsWith("-----------------------------")) {
            event.setCanceled(true);
        }
        if (cancelChats && (message.startsWith("Party M") || message.startsWith("Party L"))) {
            if (message.startsWith("Party Members (")) {
                event.setCanceled(true);
            } else {
                String[] split = message.replace("Party " + message.split(" ")[1], "").split(" ");
                for (String string : split) {
                    if (!string.contains("]") && !string.contains("●")) {
                        partyMembers.add(StringUtils.stripControlCodes(string).replace(" ", ""));
                    }
                }

                if (message.split(" ")[1].equals("Members:"))
                    cancelChats = false;
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onSwitch(WorldEvent.Load event) {
        cancelChats = false;
    }
}
