package com.natia.secretmod.hooks;

import com.natia.secretmod.core.ItemPickupEvent;
import com.natia.secretmod.features.AlertPickups;
import com.natia.secretmod.features.fishing.WormCounter;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemPickupHook {
    @SubscribeEvent
    public void onPickup(ItemPickupEvent event) {
        WormCounter.getInstance().onCollect(event);
        AlertPickups.getInstance().pickup(event);
    }
}
