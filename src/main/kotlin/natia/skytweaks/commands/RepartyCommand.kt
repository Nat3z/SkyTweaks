package natia.skytweaks.commands

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.utils.AsyncAwait
import natia.skytweaks.SecretUtils
import natia.skytweaks.SkyTweaks
import natia.skytweaks.features.RepartyHook
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender

class RepartyCommand : CommandBase() {

    private val mc = Minecraft.getMinecraft()
    /**
     * Gets the name of the command
     */
    override fun getCommandName(): String {
        return "rp"
    }

    override fun getCommandAliases(): List<String> {
        return listOf("srp")
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    override fun getCommandUsage(sender: ICommandSender): String {
        return "/rp"
    }

    /**
     * Callback when the command is invoked
     *
     * @param sender
     * @param args
     */
    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (SkyTweaksConfig.rpCommand) {
            SecretUtils.sendMessage("Attempting to reparty party members....")
            RepartyHook.instance.cancelChats = true
            mc.thePlayer.sendChatMessage("/p list")
            AsyncAwait.start({
                mc.thePlayer.sendChatMessage("/p disband")

                AsyncAwait.start({
                    var command = "/p invite"
                    SkyTweaks.LOGGER.info("Party Members:")
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

    override fun canCommandSenderUseCommand(sender: ICommandSender): Boolean {
        return true
    }
}
