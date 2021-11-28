package com.natia.secretmod.hooks;

import com.natia.secretmod.features.RepartyHook;
import com.natia.secretmod.features.bazaar.BazaarHook;
import com.natia.secretmod.features.dungeons.BonzoSpiritHook;
import com.natia.secretmod.features.dungeons.CopyFails;
import com.natia.secretmod.features.dungeons.TerminalHighlight;
import com.natia.secretmod.features.fishing.WormCounter;
import com.natia.secretmod.features.fishing.WormTimer;
import com.natia.secretmod.features.slayers.RNGesusBar;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MessageHook {


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void chatReceived(ClientChatReceivedEvent event) {
        RepartyHook.getInstance().chat(event);
        WormCounter.getInstance().chat(event);
        WormTimer.getInstance().chat(event);
        BonzoSpiritHook.getInstance().chat(event);
        RNGesusBar.getInstance().chat(event);
        CopyFails.getInstance().chat(event);
        TerminalHighlight.getInstance().chat(event);
        //BazaarHook.getInstance().chat(event);
    }
}
