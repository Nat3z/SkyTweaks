package com.natia.secretmod.commands;

import com.natia.secretmod.config.SecretModConfig;
import com.natia.secretmod.core.TickedEvent;
import com.natia.secretmod.features.RepartyHook;
import com.natia.secretmod.utils.AsyncAwait;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RepartyCommand extends CommandBase {
    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return "rp";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("srp");
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/rp";
    }

    Minecraft mc = Minecraft.getMinecraft();
    /**
     * Callback when the command is invoked
     *
     * @param sender
     * @param args
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (SecretModConfig.rpCommand) {
            mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[Secret Mod] " + EnumChatFormatting.GRAY + "Attempting to reparty members..."));
            RepartyHook.getInstance().cancelChats = true;
            mc.thePlayer.sendChatMessage("/p list");
            AsyncAwait.start(() -> {
                AsyncAwait.start(() -> {
                    mc.thePlayer.sendChatMessage("/p disband");

                    AsyncAwait.start(() -> {
                        String command = "/p invite";
                        for (String member : RepartyHook.getInstance().partyMembers) {
                            command += " " + member;
                        }
                        System.out.println(command);

                        mc.thePlayer.sendChatMessage(command);
                    }, 980);
                }, 500);
            }, 750);
        } else {
            mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[Secret Mod] " + EnumChatFormatting.GRAY + "The feature 'Reparty Command' is not enabled."));
        }

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return true;
    }
}
