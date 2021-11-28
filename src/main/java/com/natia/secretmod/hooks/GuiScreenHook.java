package com.natia.secretmod.hooks;

import com.natia.secretmod.features.MinionAnalyzer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiScreenHook {

    @SubscribeEvent
    public void preBackground(GuiScreenEvent.BackgroundDrawnEvent event) {
        MinionAnalyzer.getInstance().backgroundDrawn(event);
    }
}
