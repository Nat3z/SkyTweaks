package natia.skytweaks.commands

import cc.blendingMC.commands.BlendingCommand
import cc.blendingMC.commands.Command
import cc.blendingMC.commands.Runner
import natia.skytweaks.utils.FileUtils
import natia.skytweaks.utils.ItemDiff
import natia.skytweaks.SecretUtils
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.StringUtils

import java.io.File
import java.io.IOException

@Command(name = "savelogs")
class SavePickupLog : BlendingCommand {
    internal var mc = Minecraft.getMinecraft()

    @Runner
    fun logsCommand() {
        val itemLog = arrayOf("**ITEM LOG for HYPIXEL SKYBLOCK provided by Skyblock Secret Mod**")
        SecretUtils.itemPickupLog!!.asMap().forEach { (key, diffs) ->
            for (diff in diffs) {
                if (diff.amount < 0)
                    itemLog[0] = StringUtils.stripControlCodes(itemLog[0] + "\n[-] x" + diff.amount * -1 + " " + diff.displayName + " @" + diff.location.asName() + " | " + diff.lifetime)
                else
                    itemLog[0] = StringUtils.stripControlCodes(itemLog[0] + "\n[+] x" + diff.amount + " " + diff.displayName + " @" + diff.location.asName() + " | " + diff.lifetime)
            }
        }

        val pickuplog = File(SecretUtils.generalFolder.absolutePath + "\\itemlog-" + System.currentTimeMillis() + ".txt")
        try {
            pickuplog.createNewFile()
            FileUtils.writeToFile(pickuplog, itemLog[0])
            mc.thePlayer.addChatComponentMessage(ChatComponentText(EnumChatFormatting.AQUA.toString() + "Successfully saved current session's item log!"))
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
