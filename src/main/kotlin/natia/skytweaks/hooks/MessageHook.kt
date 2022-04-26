package natia.skytweaks.hooks

import natia.skytweaks.features.RepartyHook
import natia.skytweaks.features.SlayerContracts
import natia.skytweaks.features.SummonsAlert
import natia.skytweaks.features.bazaar.BazaarHook
import natia.skytweaks.features.dungeons.BonzoSpiritHook
import natia.skytweaks.features.dungeons.CopyFails
import natia.skytweaks.features.dungeons.NotifyCrush
import natia.skytweaks.features.fishing.WormCounter
import natia.skytweaks.features.fishing.WormTimer
import natia.skytweaks.features.griffin.GriffinBurrowWaypoints
import natia.skytweaks.features.slayers.CopyRNG
import natia.skytweaks.features.slayers.RNGesusBar
import natia.skytweaks.features.waypoints.GlobalWaypoints
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class MessageHook {


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun chatReceived(event: ClientChatReceivedEvent) {
        RepartyHook.instance.chat(event)
        WormCounter.instance.chat(event)
        GriffinBurrowWaypoints.instance.chat(event)
        WormTimer.instance.chat(event)
        BonzoSpiritHook.instance.chat(event)
        RNGesusBar.instance.chat(event)
        CopyFails.instance.chat(event)
        CopyRNG.instance.chat(event)
        //TerminalHighlight.getInstance().chat(event);
        BazaarHook.instance.chat(event);
        SlayerContracts.instance.chat(event)
        GlobalWaypoints.instance.chat(event)
//        SummonsAlert.instance.chat(event)
        NotifyCrush.instance.chat(event)
    }
}
