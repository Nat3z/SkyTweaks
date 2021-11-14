package com.natia.secretmod.core;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import com.natia.secretmod.SecretMod;
import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SecretModConfig;
import com.natia.secretmod.extensions.Extension;
import com.natia.secretmod.extensions.ExtensionList;
import com.natia.secretmod.utils.AsyncAwait;
import com.natia.secretmod.vicious.BaseViciousHUDEditor;
import com.natia.secretmod.vicious.BasicViciousConfigUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.concurrent.TimeUnit;

public class TickedEvent {
    public static boolean smGui = false;
    public static boolean smHUD = false;

    private long ticks = 0;
    private Minecraft mc = Minecraft.getMinecraft();
    private Stopwatch tickEventWatch = Stopwatch.createStarted();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.theWorld == null) return;

        if (smGui) {
            smGui = false;
            Minecraft.getMinecraft().displayGuiScreen(new BasicViciousConfigUI(SecretMod.configHandler, SecretMod.configHandler.getConfigItems(), "General", new String[]{"General", "Dungeons", "Slayers"}));
            return;
        } else if (smHUD) {
            smHUD = false;
            Minecraft.getMinecraft().displayGuiScreen(new BaseViciousHUDEditor(SecretMod.configHandler));
            return;
        }

        EntityPlayerSP p = mc.thePlayer;
        ticks++;
        // checks every 5 ticks (yes i am inspired by sba)
        if (ticks % 5 == 0) {
            if (SecretModConfig.itemPickupLogs)
                SecretUtils.getInventoryDiff(p.inventory.mainInventory);
            if (tickEventWatch.elapsed(TimeUnit.SECONDS) >= 10 && SecretModConfig.bazaarCaching) {
                tickEventWatch.reset();
                if (!tickEventWatch.isRunning()) tickEventWatch.start();

                new Thread(SecretUtils::updateBazaarCache).start();
            }
        }
    }

    private boolean firstWorldLoad = false;

    @SubscribeEvent
    public void onSwitch(WorldEvent.Load event) {
        if (!firstWorldLoad) {
            if (mc.theWorld == null) return;

            firstWorldLoad = true;

            AsyncAwait.start(() -> {
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GRAY + "Skyblock Secret Mod Extensions loaded: "));
                for (Extension extension : ExtensionList.getExtensionList()) {
                    mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "- " + extension.getName()));
                }
            }, 1600);
        }
        SecretUtils.setPreviousInventory(null);
    }

}
