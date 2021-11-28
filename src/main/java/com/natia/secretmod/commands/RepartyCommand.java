package com.natia.secretmod.commands;

import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SkyTweaksConfig;
import com.natia.secretmod.features.RepartyHook;
import com.natia.secretmod.utils.AsyncAwait;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

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

    private Minecraft mc = Minecraft.getMinecraft();
    /**
     * Callback when the command is invoked
     *
     * @param sender
     * @param args
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (SkyTweaksConfig.rpCommand) {
            SecretUtils.sendMessage("Attempting to reparty party members....");
            RepartyHook.getInstance().cancelChats = true;
            mc.thePlayer.sendChatMessage("/p list");
            AsyncAwait.start(() -> {
                mc.thePlayer.sendChatMessage("/p disband");

                AsyncAwait.start(() -> {
                    String command = "/p invite";
                    System.out.println("Party Members:");
                    for (String member : RepartyHook.getInstance().partyMembers) {
                        if (!member.isEmpty()) {
                            System.out.println("- " + member);
                            command += " " + member.replaceAll(" ", "");
                        }
                    }

                    System.out.println(command);
                    if (RepartyHook.getInstance().partyMembers.isEmpty() && RepartyHook.getInstance().cancelChats) {
                        SecretUtils.sendMessage("Reparty timed out.");
                        RepartyHook.getInstance().cancelChats = false;
                        return;
                    }

                    RepartyHook.getInstance().partyMembers.clear();
                    mc.thePlayer.sendChatMessage(command);
                    }, 1700);
            }, 1100);
        } else {
            SecretUtils.sendMessage("The feature 'Reparty Command' is not enabled.");
        }

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return true;
    }
}
