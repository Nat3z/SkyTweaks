package natia.skytweaks.commands

import cc.blendingMC.commands.BlendingCommand
import cc.blendingMC.commands.Command
import cc.blendingMC.commands.Runner
import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.utils.AsyncAwait
import natia.skytweaks.SecretUtils
import natia.skytweaks.SkyTweaks
import natia.skytweaks.features.RepartyHook
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender

@Command(name = "srp", alias = ["rp", "reparty"])
class RepartyCommand : BlendingCommand {

    private val mc = Minecraft.getMinecraft()
    @Runner
    fun repartyCommand() {
        if (SkyTweaksConfig.rpCommand) {
            SecretUtils.sendMessage("Attempting to reparty party members....")
            RepartyHook.instance.cancelChats = true
            mc.thePlayer.sendChatMessage("/p list")
            AsyncAwait.start({
                mc.thePlayer.sendChatMessage("/p disband")

                AsyncAwait.start({
                    var command = "/p invite"
                    SkyTweaks.LOGGER.info("Party Members:")
                    RepartyHook.instance.partyMembers.remove(mc.thePlayer.name)
                    for (member in RepartyHook.instance.partyMembers) {
                        if (!member.isEmpty()) {
                            SkyTweaks.LOGGER.info("- $member")
                            command += " " + member.replace(" ".toRegex(), "")
                        }
                    }

                    SkyTweaks.LOGGER.info(command)
                    if (RepartyHook.instance.partyMembers.isEmpty() && RepartyHook.instance.cancelChats) {
                        SecretUtils.sendMessage("Reparty timed out.")
                        RepartyHook.instance.cancelChats = false
                    } else {
                        RepartyHook.instance.partyMembers.clear()
                        mc.thePlayer.sendChatMessage(command)
                    }
                }, 2300)
            }, 1100)
        } else {
            SecretUtils.sendMessage("The feature 'Reparty Command' is not enabled.")
        }

    }
}
