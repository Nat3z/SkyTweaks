package com.natia.secretmod.features.fishing;

import com.google.common.base.Stopwatch;
import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SkyTweaksConfig;
import com.natia.secretmod.core.SkyblockTimer;
import com.natia.secretmod.features.TimersHook;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class WormTimer {

    private SkyblockTimer skyblockTimer = null;
    private Minecraft mc = Minecraft.getMinecraft();
    private Stopwatch notify = Stopwatch.createUnstarted();
    private boolean alreadyTimed = false;


    private static WormTimer INSTANCE = new WormTimer();
    public static WormTimer getInstance() {
        return INSTANCE;
    }
    public void render() {
        if (mc.currentScreen != null) return;

        if (notify.isRunning()) {
            SecretUtils.sendTitleCentered(SkyTweaksConfig.wormFishText, Color.RED.getRGB());

            if (notify.elapsed(TimeUnit.SECONDS) >= 3) {
                notify.reset();
            }
        }
    }
    /* A flaming worm surfaces from the depths! */
    public void chat(ClientChatReceivedEvent event) {
        if (!SkyTweaksConfig.wormTimer) return;

        if (event.message.getUnformattedText().startsWith("A flaming worm surfaces from the depths!")) {
            if (!alreadyTimed) {
                alreadyTimed = true;

                skyblockTimer = new SkyblockTimer(Stopwatch.createStarted(),
                        EnumChatFormatting.LIGHT_PURPLE + "Kill Pit Worms", 280, false, () -> {
                    alreadyTimed = false;
                    SecretUtils.playLoudSound("random.orb", 0.5f);
                    notify.start();
                });

                TimersHook.addTimer(skyblockTimer);
            }
        }
    }

}
