package com.natia.secretmod.features;

import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.core.ItemPickupEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class AlertPickups {

    List<String> alertedItems = new ArrayList<>();
    public AlertPickups() {
        /* frags */
        alertedItems.add("Diamante's Handle");
        alertedItems.add("L.A.S.R.'s Eye");
        alertedItems.add("Bigfoot's Lasso");
        alertedItems.add("Jolly Pink Rock");
        /* diana */
        alertedItems.add("Chimera I");
        alertedItems.add("Dwarf Turtle Shelmet");
        alertedItems.add("Antique Remedies");
        alertedItems.add("Minos Relic");
        alertedItems.add("Daedalus Stick");
    }
    private Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onPickup(ItemPickupEvent event) {
        if (event.getItemDiff().getAmount() < 0) return;
        if (mc.currentScreen != null) return;
        System.out.println("Pickup - " + event.getItemDiff().getDisplayName());
        if (alertedItems.contains(StringUtils.stripControlCodes(event.getItemDiff().getDisplayName()))) {
            SecretUtils.playLoudSound("random.orb", 0.5f);
            SecretUtils.sendMessage(EnumChatFormatting.GOLD + "" + EnumChatFormatting.BOLD + "RARE DROP! " + EnumChatFormatting.RESET + event.getItemDiff().getDisplayName());
        }
    }

}
