package natia.skytweaks.config

import cc.blendingMC.commands.*
import cc.blendingMC.vicious.BlendingConfigGui
import gg.blendingMC.WelcomeUser.Companion.mc
import gg.blendingMC.hook.MinecraftHook
import natia.skytweaks.SecretUtils.toChat
import natia.skytweaks.SkyTweaks
import natia.skytweaks.hooks.TickHook
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.fml.common.gameevent.TickEvent
@Command(name = "skytweaks", alias = ["skt"])
class SkyTweaksCommand : BlendingCommand {

    @Argument
    var commandArgs: Array<String> = emptyArray()

    @Runner
    fun skytweaksCommand() {
        if (commandArgs.isEmpty())
            TickHook.scheduleGui(BlendingConfigGui(SkyTweaks.configHandler, SkyTweaks.configHandler?.getConfigItems(), "General", arrayOf("General", "Dungeons", "Slayers", "Fishing", "Special")))
    }

    @Parameter(name = "params")
    fun params() {
        commandArgs.forEach {
            mc.thePlayer.addChatComponentMessage(it.toChat())
        }
    }

    @Parameter(name = "help")
    fun help() {
        mc.thePlayer.addChatComponentMessage(
            ("${EnumChatFormatting.UNDERLINE}${EnumChatFormatting.AQUA}SkyTweaks Help\n" +
            "${EnumChatFormatting.DARK_AQUA}> /rp ${EnumChatFormatting.GRAY}- Reparties your current party.\n" +
            "${EnumChatFormatting.DARK_AQUA}> /wp ${EnumChatFormatting.GRAY}- Opens the waypoints gui."
            )
        .toChat())
    }
}
