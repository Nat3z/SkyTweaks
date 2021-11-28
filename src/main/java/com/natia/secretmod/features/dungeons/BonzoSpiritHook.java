package com.natia.secretmod.features.dungeons;

import com.google.common.base.Stopwatch;
import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SkyTweaksConfig;
import com.natia.secretmod.core.SkyblockTimer;
import com.natia.secretmod.features.TimersHook;
import com.natia.secretmod.utils.ItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class BonzoSpiritHook {
    Minecraft mc = Minecraft.getMinecraft();
    SkyblockTimer skyblockTimer = null;
    Stopwatch sbStopwatch = Stopwatch.createUnstarted();

    public void chat(ClientChatReceivedEvent event) {
        if (!SkyTweaksConfig.maskTimers) return;

        /* I dont know exact message lmao */
        String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (!message.contains(": ") && message.endsWith("saved your life!")) {
            /* yoinked from Danker's Skyblock Mod.
            * https://github.com/bowser0000/SkyblockMod/
             */
            EntityPlayerSP player = mc.thePlayer;
            ItemStack bonzoMask = player.getCurrentArmor(3);
            if (bonzoMask != null && bonzoMask.getItem() == Items.skull) {
                for (String line : ItemUtils.getLore(bonzoMask)) {
                    String stripped = StringUtils.stripControlCodes(line);
                    if (stripped.startsWith("Cooldown: ")) {
                        if (sbStopwatch.isRunning()) sbStopwatch.reset();
                        sbStopwatch.start();

                        cooldownSeconds = Integer.parseInt(stripped.replace("Cooldown: ", "").replace("s", ""));
                        skyblockTimer = new SkyblockTimer(sbStopwatch,
                                EnumChatFormatting.GOLD + "Bonzo Mask Cooldown", cooldownSeconds, false, SecretUtils.generateEmptyRunnable());
                        TimersHook.addTimer(skyblockTimer);
                        break;
                    }
                }
            }
        }
    }
    int cooldownSeconds = 999999999;

    private static BonzoSpiritHook INSTANCE = new BonzoSpiritHook();
    public static BonzoSpiritHook getInstance() {
        return INSTANCE;
    }

}
