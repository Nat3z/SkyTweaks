package com.natia.secretmod.features.slayers;

import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SkyTweaksConfig;
import com.natia.secretmod.vicious.HudElement;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.awt.*;

public class RNGesusBar {

    private String RNGesusMeter = "";
    public void chat(ClientChatReceivedEvent event) {
        if (event.message.getUnformattedText().startsWith("    RNGesus Meter:")) {
            RNGesusMeter = event.message.getFormattedText().replace("    ", "")
                            .replace("RNGesus", "")
                            .replace("Meter", "")
                            .replace(":", "")
                            .replace("  ", "");
        }
    }

    public void render() {
        if (!SkyTweaksConfig.rngMeter) return;
        HudElement element = SkyTweaksConfig.rngesusmeter;
        SecretUtils.drawCenteredString(RNGesusMeter, element.x - 100, element.y, Color.WHITE.getRGB(), 1);
    }

    private static RNGesusBar INSTANCE = new RNGesusBar();
    public static RNGesusBar getInstance() {
        return INSTANCE;
    }

}
