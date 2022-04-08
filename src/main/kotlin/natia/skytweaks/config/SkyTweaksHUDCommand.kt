package natia.skytweaks.config

import cc.blendingMC.vicious.BlendingHUDEditor
import gg.blendingMC.hook.MinecraftHook
import natia.skytweaks.SkyTweaks
import natia.skytweaks.hooks.TickHook
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender

class SkyTweaksHUDCommand : CommandBase() {
    /**
     * Gets the name of the command
     */
    override fun getCommandName(): String {
        return "smhud"
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    override fun getCommandUsage(sender: ICommandSender): String {
        return "/smhud"
    }

    /**
     * Callback when the command is invoked
     *
     * @param sender
     * @param args
     */
    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        TickHook.scheduleGui(BlendingHUDEditor(SkyTweaks.configHandler))
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender): Boolean {
        return true
    }
}