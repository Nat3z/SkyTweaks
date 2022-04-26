package natia.skytweaks.commands.waypoints

import cc.blendingMC.commands.BlendingCommand
import cc.blendingMC.commands.Command
import cc.blendingMC.commands.Runner
import cc.blendingMC.vicious.BlendingConfigGui
import natia.skytweaks.SkyTweaks
import natia.skytweaks.hooks.TickHook
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender

@Command(name = "waypoints", alias = ["wp"])
class WaypointsCommand : BlendingCommand {
    @Runner
    fun wpCommand() {
        TickHook.scheduleGui(WaypointsGui())
    }
}