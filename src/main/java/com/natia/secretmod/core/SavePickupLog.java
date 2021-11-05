package com.natia.secretmod.core;

import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.utils.FileUtils;
import com.natia.secretmod.utils.ItemDiff;
import io.netty.util.internal.StringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class SavePickupLog extends CommandBase {
    Minecraft mc = Minecraft.getMinecraft();
    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return "savelogs";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/savelogs";
    }

    /**
     * Callback when the command is invoked
     *
     * @param sender
     * @param args
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        final String[] itemLog = new String[]{"**ITEM LOG for HYPIXEL SKYBLOCK provided by Skyblock Secret Mod**"};
        SecretUtils.getItemPickupLog().asMap().forEach((key, diffs) -> {
            for (ItemDiff diff : diffs) {
                if (diff.getAmount() < 0)
                    itemLog[0] = StringUtils.stripControlCodes(itemLog[0] + "\n[-] x" + (diff.getAmount() * -1) + " " + diff.getDisplayName() + " @" + diff.getLocation().asName() + " | " + diff.getLifetime());
                else
                    itemLog[0] = StringUtils.stripControlCodes(itemLog[0] + "\n[+] x" + diff.getAmount() + " " + diff.getDisplayName() + " @" + diff.getLocation().asName() + " | " + diff.getLifetime());
            }
        });

        File pickuplog = new File(SecretUtils.getGeneralFolder().getAbsolutePath() + "\\itemlog-" + System.currentTimeMillis() + ".txt");
        try {
            pickuplog.createNewFile();
            FileUtils.writeToFile(pickuplog, itemLog[0]);
            mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Successfully saved current session's item log!"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return true;
    }
}
