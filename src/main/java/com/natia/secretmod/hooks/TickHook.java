package com.natia.secretmod.hooks;

import com.google.common.base.Stopwatch;
import com.natia.secretmod.SkyTweaks;
import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SkyTweaksConfig;
import com.natia.secretmod.extensions.Extension;
import com.natia.secretmod.extensions.ExtensionList;
import com.natia.secretmod.features.bazaar.Notifier;
import com.natia.secretmod.features.slayers.VoidGloom;
import com.natia.secretmod.utils.AsyncAwait;
import com.natia.secretmod.vicious.BaseViciousHUDEditor;
import com.natia.secretmod.vicious.BasicViciousConfigUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TickHook {
    public static boolean smGui = false;
    public static boolean smHUD = false;

    private long ticks = 0;
    private Minecraft mc = Minecraft.getMinecraft();
    private Stopwatch tickEventWatch = Stopwatch.createStarted();

    private static GuiScreen showScreen = null;

    public static void scheduleGui(GuiScreen screen) {
        showScreen = screen;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        /* Tick Hook */
        VoidGloom.getInstance().tick();
        //Notifier.getInstance().tick();

        if (mc.theWorld == null) return;

        if (showScreen != null) {
            Minecraft.getMinecraft().displayGuiScreen(showScreen);
            showScreen = null;
            return;
        }

        if (smGui) {
            smGui = false;
            Minecraft.getMinecraft().displayGuiScreen(new BasicViciousConfigUI(SkyTweaks.configHandler, SkyTweaks.configHandler.getConfigItems(), "General", new String[]{"General", "Dungeons", "Slayers", "Fishing"}));
            return;
        } else if (smHUD) {
            smHUD = false;
            Minecraft.getMinecraft().displayGuiScreen(new BaseViciousHUDEditor(SkyTweaks.configHandler));
            return;
        }

        EntityPlayerSP p = mc.thePlayer;
        ticks++;
        // checks every 5 ticks (yes i am inspired by sba)
        if (ticks % 5 == 0) {
            SecretUtils.getInventoryDiff(p.inventory.mainInventory);

            if (tickEventWatch.elapsed(TimeUnit.SECONDS) >= 10 && SkyTweaksConfig.bazaarCaching) {
                tickEventWatch.reset();
                if (!tickEventWatch.isRunning()) tickEventWatch.start();

                new Thread(SecretUtils::updateBazaarCache).start();
            }
        }
    }

    private boolean firstWorldLoad = false;
    @SubscribeEvent
    public void onSwitch(WorldEvent.Load event) {
        AsyncAwait.start(() -> {
            SecretUtils.setPreviousInventory(Arrays.asList(mc.thePlayer.inventory.mainInventory));
        }, 100);
        if (!firstWorldLoad) {
            if (mc.theWorld == null) return;

            firstWorldLoad = true;

            AsyncAwait.start(() -> {
                mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.AQUA + "SkyTweaks Extensions loaded: "));
                for (Extension extension : ExtensionList.getExtensionList()) {
                    mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "- " + extension.getName()));
                }
            }, 1600);
        }
    }

}
