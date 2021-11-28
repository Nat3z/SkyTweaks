package com.natia.secretmod.hooks;

import com.natia.secretmod.features.DamagePerSecond;
import com.natia.secretmod.features.TimersHook;
import com.natia.secretmod.features.altC.QuickTab;
import com.natia.secretmod.features.fishing.WormCounter;
import com.natia.secretmod.features.fishing.WormTimer;
import com.natia.secretmod.features.slayers.RNGesusBar;
import com.natia.secretmod.features.slayers.VoidGloom;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RenderHook {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void render(TickEvent.RenderTickEvent event) {
        VoidGloom.getInstance().render();
        TimersHook.getInstance().render();
        WormCounter.getInstance().render();
        WormTimer.getInstance().render();
        RNGesusBar.getInstance().render();
        DamagePerSecond.getInstance().render();
    }

}
