package com.natia.secretmod.config;

import com.natia.secretmod.core.TickedEvent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class SecretModCommand extends CommandBase {
    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return "smhud";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/smhud";
    }

    /**
     * Callback when the command is invoked
     *
     * @param sender
     * @param args
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {

        TickedEvent.smHUD = true;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return true;
    }
}
