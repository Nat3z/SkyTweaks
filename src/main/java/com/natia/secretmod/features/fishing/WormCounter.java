package com.natia.secretmod.features.fishing;

import com.natia.secretmod.config.SkyTweaksConfig;
import com.natia.secretmod.core.ItemPickupEvent;
import com.natia.secretmod.vicious.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.awt.*;

public class WormCounter {
    /* A flaming worm surfaces from the depths! */
    private Minecraft mc = Minecraft.getMinecraft();

    int wormsCaught = 0;
    int wormsKilled = 0;


    public WormCounter() {
        wormsCaught = SkyTweaksConfig.wormsCaught;
        wormsKilled = SkyTweaksConfig.wormsCollected;
    }

    public void chat(ClientChatReceivedEvent event) {
        if (!SkyTweaksConfig.wormCounter) return;

        if (event.message.getUnformattedText().startsWith("A flaming worm surfaces from the depths!")) {
            wormsCaught++;
            SkyTweaksConfig.wormsCaught = wormsCaught;
        }
    }

    private static WormCounter INSTANCE = new WormCounter();
    public static WormCounter getInstance() {
        return INSTANCE;
    }

    public void render() {
        if (!SkyTweaksConfig.wormCounter) return;
        if (mc.currentScreen != null) return;

        HudElement element = SkyTweaksConfig.wormCounterHUD;

        /* Worms Caught */
        mc.fontRendererObj.drawString(EnumChatFormatting.LIGHT_PURPLE + "Worms Caught:", element.x, element.y, Color.WHITE.getRGB(), true);
        mc.fontRendererObj.drawString(EnumChatFormatting.LIGHT_PURPLE + "" + wormsCaught, element.x + 80, element.y, Color.WHITE.getRGB(), true);

        /* Worms Killed */
        mc.fontRendererObj.drawString(EnumChatFormatting.DARK_PURPLE + "Worm Membrane:", element.x, element.y + 10, Color.WHITE.getRGB(), true);
        mc.fontRendererObj.drawString(EnumChatFormatting.DARK_PURPLE + "" + wormsKilled, element.x + 80, element.y + 10, Color.WHITE.getRGB(), true);
    }

    public void onCollect(ItemPickupEvent event) {
        if (mc.currentScreen != null) return;

        if (StringUtils.stripControlCodes(event.getItemDiff().getDisplayName()).contains("Worm Membrane")) {
            wormsKilled++;
            SkyTweaksConfig.wormsCollected = wormsKilled;

        }
    }
}
