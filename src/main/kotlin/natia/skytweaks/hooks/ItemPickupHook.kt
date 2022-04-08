package natia.skytweaks.hooks

import natia.skytweaks.core.ItemPickupEvent
import natia.skytweaks.features.AlertPickups
import natia.skytweaks.features.fishing.WormCounter
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ItemPickupHook {
    @SubscribeEvent
    fun onPickup(event: ItemPickupEvent) {
        WormCounter.instance.onCollect(event)
        AlertPickups.instance.pickup(event)
    }
}
