package natia.skytweaks.config

import cc.blendingMC.vicious.BlendingHudPreview
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting

import java.awt.*

class HudPreviews {

    class VoidgloomAssist : BlendingHudPreview {

        override fun run(x: Int, y: Int, width: Int, height: Int) {
            /* boss health */
            val mc = Minecraft.getMinecraft()
            mc.fontRendererObj.drawString(EnumChatFormatting.BOLD.toString() + "BOSS: ", x.toFloat(), y.toFloat(), Color.white.rgb, true)
            mc.fontRendererObj.drawString(EnumChatFormatting.AQUA.toString() + "Voidgloom Seraph " + EnumChatFormatting.YELLOW + "25M " + EnumChatFormatting.RED + "❤", (x + 40).toFloat(), y.toFloat(), Color.white.rgb, true)
            /* beacon down */
            mc.fontRendererObj.drawString(EnumChatFormatting.DARK_AQUA.toString() + "" + EnumChatFormatting.BOLD + "BEACON DOWN: ", x.toFloat(), (y + 10).toFloat(), Color.white.rgb, true)
            mc.fontRendererObj.drawString(EnumChatFormatting.RED.toString() + "NO", (x + 110).toFloat(), (y + 10).toFloat(), Color.white.rgb, true)
            /* holding beacon */
            mc.fontRendererObj.drawString(EnumChatFormatting.DARK_AQUA.toString() + "" + EnumChatFormatting.BOLD + "BEACON HELD: ", x.toFloat(), (y + 20).toFloat(), Color.white.rgb, true)
            mc.fontRendererObj.drawString(EnumChatFormatting.GREEN.toString() + "YES", (x + 110).toFloat(), (y + 20).toFloat(), Color.white.rgb, true)
            /* yang glyphs spawned */
            mc.fontRendererObj.drawString(EnumChatFormatting.DARK_PURPLE.toString() + "" + EnumChatFormatting.BOLD + "SKULLS NEARBY: ", x.toFloat(), (y + 30).toFloat(), Color.white.rgb, true)
            mc.fontRendererObj.drawString(EnumChatFormatting.GREEN.toString() + "YES", (x + 110).toFloat(), (y + 30).toFloat(), Color.white.rgb, true)
            /* is in hit phase */
            mc.fontRendererObj.drawString(EnumChatFormatting.RED.toString() + "" + EnumChatFormatting.BOLD + "HIT PHASE: ", x.toFloat(), (y + 40).toFloat(), Color.white.rgb, true)
            mc.fontRendererObj.drawString(EnumChatFormatting.RED.toString() + "NO", (x + 110).toFloat(), (y + 40).toFloat(), Color.white.rgb, true)
        }
    }

    class WormCounter : BlendingHudPreview {

        override fun run(x: Int, y: Int, width: Int, height: Int) {
            val mc = Minecraft.getMinecraft()
            /* Worms Caught */
            mc.fontRendererObj.drawString(EnumChatFormatting.LIGHT_PURPLE.toString() + "Worms Caught:", x.toFloat(), y.toFloat(), Color.WHITE.rgb, true)
            mc.fontRendererObj.drawString(EnumChatFormatting.LIGHT_PURPLE.toString() + "50", (x + 80).toFloat(), y.toFloat(), Color.WHITE.rgb, true)

            /* Worms Killed */
            mc.fontRendererObj.drawString(EnumChatFormatting.DARK_PURPLE.toString() + "Worm Membrane:", x.toFloat(), (y + 10).toFloat(), Color.WHITE.rgb, true)
            mc.fontRendererObj.drawString(EnumChatFormatting.DARK_PURPLE.toString() + "36", (x + 80).toFloat(), (y + 10).toFloat(), Color.WHITE.rgb, true)
        }
    }

    class SkyblockTimers : BlendingHudPreview {

        override fun run(x: Int, y: Int, width: Int, height: Int) {
            val mc = Minecraft.getMinecraft()
            mc.fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY.toString() + "Example Timer: " +
                    EnumChatFormatting.GRAY + (200 * 1000 - 100000).toDouble() / 1000 + "s", x.toFloat(), y.toFloat(), Color.white.rgb, true)
        }
    }

    class RNGesusMeter : BlendingHudPreview {

        override fun run(x: Int, y: Int, width: Int, height: Int) {
            val mc = Minecraft.getMinecraft()
            mc.fontRendererObj.drawString(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + "-----" + EnumChatFormatting.WHITE + "---------------",
                    x, y, Color.white.rgb)
        }
    }

    class DamageMeter : BlendingHudPreview {

        override fun run(x: Int, y: Int, width: Int, height: Int) {
            val mc = Minecraft.getMinecraft()
            mc.fontRendererObj.drawString(EnumChatFormatting.RED.toString() + "Damage per Second:" + EnumChatFormatting.DARK_RED + " ~3000", x.toFloat(), y.toFloat(), Color.WHITE.rgb, true)
        }
    }
}
