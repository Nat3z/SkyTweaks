package com.natia.secretmod.features.dungeons;

import com.google.common.base.Stopwatch;
import com.natia.secretmod.config.SecretModConfig;
import com.natia.secretmod.utils.ItemUtils;
import com.natia.secretmod.vicious.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class BonzoSpiritHook {
    Minecraft mc = Minecraft.getMinecraft();
    Stopwatch cooldown = Stopwatch.createUnstarted();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent event) {
        if (!SecretModConfig.maskTimers) return;

        /* I dont know exact message lmao */
        String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (!message.contains(": ") && message.endsWith("saved your life!")) {
            if (cooldown.isRunning()) cooldown.reset();
            if (!cooldown.isRunning()) cooldown.start();
            /* yoinked from Danker's Skyblock Mod.
            * https://github.com/bowser0000/SkyblockMod/
             */
            EntityPlayerSP player = mc.thePlayer;
            ItemStack bonzoMask = player.getCurrentArmor(3);
            if (bonzoMask != null && bonzoMask.getItem() == Items.skull) {
                for (String line : ItemUtils.getLore(bonzoMask)) {
                    String stripped = StringUtils.stripControlCodes(line);
                    if (stripped.startsWith("Cooldown: "))
                        cooldownSeconds = Integer.parseInt(stripped.replace("Cooldown: ", "").replace("s", ""));
                }
            }
        }
    }

    private ResourceLocation BONZO_MASK = new ResourceLocation("secretmod", "bonzo_mask.png");
    int cooldownSeconds = 999999999;
    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (!SecretModConfig.maskTimers) return;
        if (mc.currentScreen != null) return;

        HudElement hudElement = SecretModConfig.spiritBonzoTimerHUD;
        if (cooldown.isRunning() && cooldown.elapsed(TimeUnit.SECONDS) >= cooldownSeconds) {
            cooldown.stop();
        }

        if (cooldown.isRunning()) {
            mc.getTextureManager().bindTexture(BONZO_MASK);
            Gui.drawModalRectWithCustomSizedTexture(hudElement.x, hudElement.y, 0, 0, 50, 50, 50, 50);
            mc.fontRendererObj.drawString("Cooldown:", hudElement.x + 60, hudElement.y + 10, new Color(255, 136, 57).getRGB(), true);
            mc.fontRendererObj.drawString((cooldownSeconds - cooldown.elapsed(TimeUnit.SECONDS)) + "s", hudElement.x + 60, hudElement.y + 20, new Color(255, 228, 62).getRGB(), true);
        }
    }

}
