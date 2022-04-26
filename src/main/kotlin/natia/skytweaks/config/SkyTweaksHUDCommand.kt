package natia.skytweaks.config

import cc.blendingMC.commands.BlendingCommand
import cc.blendingMC.commands.Command
import cc.blendingMC.commands.Runner
import cc.blendingMC.vicious.BlendingHUDEditor
import gg.blendingMC.hook.MinecraftHook
import natia.skytweaks.SkyTweaks
import natia.skytweaks.hooks.TickHook
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender

@Command(name = "skhud")
class SkyTweaksHUDCommand : BlendingCommand {

    @Runner
    fun hudCommand() {
        TickHook.scheduleGui(BlendingHUDEditor(SkyTweaks.configHandler))
    }
}
