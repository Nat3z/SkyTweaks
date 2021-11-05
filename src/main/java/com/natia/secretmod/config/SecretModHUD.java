package com.natia.secretmod.config;

import com.natia.secretmod.core.TickedEvent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class SecretModHUD extends CommandBase {
    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return "sm";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sm";
    }

    /**
     * Callback when the command is invoked
     *
     * @param sender
     * @param args
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        TickedEvent.smGui = true;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return true;
    }
}
