package natia.skytweaks.commands.waypoints

import cc.blendingMC.vicious.BlendingConfigGui
import natia.skytweaks.SkyTweaks
import natia.skytweaks.hooks.TickHook
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender

class WaypointsCommand : CommandBase() {
    /**
     * Gets the name of the command
     */
    override fun getCommandName(): String {
        return "waypoints"
    }

    override fun getCommandAliases(): MutableList<String> {
        return arrayListOf("wp")
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    override fun getCommandUsage(sender: ICommandSender): String {
        return "/waypoints"
    }

    /**
     * Callback when the command is invoked
     *
     * @param sender
     * @param args
     */
    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        TickHook.scheduleGui(WaypointsGui())
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender): Boolean {
        return true
    }
}