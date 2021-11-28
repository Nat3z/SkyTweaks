package com.natia.secretmod.features.dungeons;

import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SkyTweaksConfig;
import com.natia.secretmod.utils.Location;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CopyFails {
    Minecraft mc = Minecraft.getMinecraft();

    public void chat(ClientChatReceivedEvent event) {
        if (!SkyTweaksConfig.copyFails) return;
        if (SecretUtils.isInDungeons() != Location.THE_CATACOMBS) return;

        String message = event.message.getUnformattedText();
        if (message.contains(": "))
            return;

        if (SecretUtils.isValid() && SecretUtils.isInDungeons() == Location.THE_CATACOMBS) {
            if (message.contains("You were killed by") || message.contains(" was killed by") || message.contains("died to a")) {
                StringSelection stringSelection = new StringSelection(message);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                SecretUtils.sendMessage("Death copied!");
            } else if (message.contains("Yikes!")) {
                StringSelection stringSelection = new StringSelection(message);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                SecretUtils.sendMessage("Fail copied!");
            }
        }
    }

    private static CopyFails INSTANCE = new CopyFails();
    public static CopyFails getInstance() {
        return INSTANCE;
    }

}
