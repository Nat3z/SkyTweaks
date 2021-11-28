package com.natia.secretmod.features;

import com.natia.secretmod.config.SkyTweaksConfig;
import com.natia.secretmod.core.SkyblockTimer;
import com.natia.secretmod.vicious.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimersHook {
    private Minecraft mc = Minecraft.getMinecraft();

    private static List<SkyblockTimer> Timers = new ArrayList<>();

    public static void addTimer(SkyblockTimer timer) {
        Timers.add(timer);
    }
    public static void remove(SkyblockTimer timer) {
        Timers.remove(timer);
    }

    public static List<SkyblockTimer> getTimers() {
        return Timers;
    }

    private static TimersHook INSTANCE = new TimersHook();
    public static TimersHook getInstance() {
        return INSTANCE;
    }

    public void render() {
        if (!SkyTweaksConfig.allowTimers) return;
        if (mc.currentScreen != null) return;
        HudElement element = SkyTweaksConfig.timersHUD;

        int y = -10;
        List<SkyblockTimer> timersToRemove = new ArrayList<>();

        for (SkyblockTimer timer : TimersHook.getTimers()) {
            if (timer.timerStopwatch.isRunning() && !timer.hiddenTimer) {
                y += 10;
                mc.fontRendererObj.drawString(timer.displayText + ": " +
                        EnumChatFormatting.GRAY + ( (double) ( (timer.secondsUntil * 1000) - timer.timerStopwatch.elapsed(TimeUnit.MILLISECONDS) ) / 1000) + "s", element.x, element.y + y, Color.white.getRGB(), true);

                if (timer.timerStopwatch.elapsed(TimeUnit.SECONDS) >= timer.secondsUntil) {
                    timer.timerStopwatch.reset();
                    timer.run.run();
                    timersToRemove.add(timer);
                }
            }
        }

        TimersHook.getTimers().removeAll(timersToRemove);
    }


}
