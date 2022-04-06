package natia.skytweaks.config

import cc.blendingMC.vicious.BlendingConfigGui
import gg.blendingMC.hook.MinecraftHook
import natia.skytweaks.SkyTweaks
import natia.skytweaks.hooks.TickHook
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraftforge.fml.common.gameevent.TickEvent

class SkyTweaksCommand : CommandBase() {
    /**
     * Gets the name of the command
     */
    override fun getCommandName(): String {
        return "skytweaks"
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    override fun getCommandUsage(sender: ICommandSender): String {
        return "/skytweaks"
    }

    /**
     * Callback when the command is invoked
     *
     * @param sender
     * @param args
     */
    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        TickHook.scheduleGui(BlendingConfigGui(SkyTweaks.configHandler, SkyTweaks.configHandler?.getConfigItems(), "General", arrayOf("General", "Dungeons", "Slayers", "Fishing", "Special")))
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender): Boolean {
        return true
    }
}
