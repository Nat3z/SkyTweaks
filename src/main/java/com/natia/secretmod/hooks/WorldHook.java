package com.natia.secretmod.hooks;

import com.natia.secretmod.features.RepartyHook;
import com.natia.secretmod.features.dungeons.TerminalHighlight;
import com.natia.secretmod.features.slayers.VoidGloom;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldHook {

    @SubscribeEvent
    public void load(WorldEvent.Load event) {
        RepartyHook.getInstance().worldLoad();
        VoidGloom.getInstance().worldLoad();
        TerminalHighlight.getInstance().worldLoad();
    }

    @SubscribeEvent
    public void blockDrawn(DrawBlockHighlightEvent event) {
        VoidGloom.getInstance().blockRender(event);
        TerminalHighlight.getInstance().blockRender(event);

    }
}
