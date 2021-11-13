package com.natia.secretmod.features.dungeons;

import com.natia.secretmod.config.SecretModConfig;
import com.natia.secretmod.vicious.BaseViciousHUDEditor;
import com.natia.secretmod.vicious.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class AreYouReady {
    private Minecraft mc = Minecraft.getMinecraft();
    private boolean readyGui = false;
    private boolean cancelChats = false;

    public List<String> partyMembers = new ArrayList<>();
    public List<String> readiedMembers = new ArrayList<>();


    public static AreYouReady getInstance() {
        return new AreYouReady();
    }

    @SubscribeEvent
    public void checkIfReady(ClientChatReceivedEvent event) {
        if (!SecretModConfig.readyPlayers) return;
        /* ready system */
        String message = event.message.getUnformattedText();
        if (message.contains("Party") && event.message.getUnformattedText().endsWith("r?") && message.contains(mc.thePlayer.getName())) {
                readyGui = true;
                cancelChats = true;
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        mc.thePlayer.sendChatMessage("/p list");
                        mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Readied Players HUD enabled!"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
        } else if (message.contains("Party") && event.message.getUnformattedText().endsWith("r")) {
            String[] member = message.split(Pattern.quote(">"))[1].replaceFirst(" ", "").split(" ");
            String pmem = member[0].replace(":", "");
            System.out.println(pmem + " is ready.");
            if (pmem.endsWith("]")) {
                pmem = member[1].replace(":", "");
            }
            pmem = StringUtils.stripControlCodes(pmem);
            System.out.println(pmem + " is ready.");
            if (partyMembers.contains(pmem) && !readiedMembers.contains(pmem)) {
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + pmem + " is ready."));
                readiedMembers.add(pmem);
            }
        }
        else if (cancelChats && message.startsWith("-----------------------------")) {
            event.setCanceled(true);
        }
        if (cancelChats && (message.startsWith("Party M") || message.startsWith("Party L"))) {
            if (message.startsWith("Party Members (")) {
                event.setCanceled(true);
                readiedMembers.add(mc.thePlayer.getName());
            } else {
                String[] split = message.replace("Party " + message.split(" ")[1], "").split(" ");
                for (String string : split) {
                    if (!string.contains("]") && !string.contains("●")) {
                        partyMembers.add(StringUtils.stripControlCodes(string));
                    }
                }

                if (message.split(" ")[1].equals("Members:"))
                    cancelChats = false;
                event.setCanceled(true);
            }
        }
    }
    int ticks = 0;
    boolean notified = false;
    @SubscribeEvent
    public void readyPlayers(TickEvent.RenderTickEvent event) {
        if (mc.currentScreen != null && !(mc.currentScreen instanceof BaseViciousHUDEditor) && !(mc.currentScreen instanceof GuiChest)) return;
        if (readyGui && SecretModConfig.readyPlayers) {
            ticks++;
            /* after 30 ticks, just stop cancelling chats */
            if (ticks % 30 == 0) {
                if (cancelChats)
                    cancelChats = false;
            } else if (ticks % 15 == 0) {
                /* all members are ready. queue us in! */
                if (readiedMembers.size() == partyMembers.size() / 2 && !notified) {
                    mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + "All party members are ready!"));
                    notified = true;
                    ticks = 1;
                }
            }
            HudElement hudElement = SecretModConfig.readyHUD;
            mc.fontRendererObj.drawString(EnumChatFormatting.UNDERLINE + "Ready Users HUD", hudElement.x + 20, hudElement.y + 10, Color.white.getRGB(), true);
            AtomicInteger y = new AtomicInteger(hudElement.y + 10);
            partyMembers.forEach(member -> {
                y.addAndGet(7);
                mc.fontRendererObj.drawString(member, hudElement.x + 20, y.get(), readiedMembers.contains(member) ? Color.green.getRGB() : Color.red.getRGB(), true);
            });
            mc.fontRendererObj.drawString("Queuing " + SecretModConfig.autojoindungeonFloor, hudElement.x + 20, y.get() + 25, Color.gray.getRGB(), true);
        }
    }

    @SubscribeEvent
    public void onSwitch(WorldEvent.Load event) {
        partyMembers.clear();
        readiedMembers.clear();
        notified = false;
        ticks = 1;
        cancelChats = false;
        readyGui = false;
    }

}
