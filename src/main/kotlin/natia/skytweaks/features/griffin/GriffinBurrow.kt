package natia.skytweaks.features.griffin

import natia.skytweaks.utils.RenderUtils
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumChatFormatting

import javax.vecmath.Vector3f
import java.awt.*

class GriffinBurrow(var x: Int, var y: Int, var z: Int, type: Int, tier: Int, chain: Int) {
    var chain: Int = 0
    var type = EnumChatFormatting.GREEN.toString() + "Start"

    val blockPos: BlockPos

    init {
        this.blockPos = BlockPos(x, y, z)
        this.chain = chain + 1

        if (type == 1) {
            this.type = EnumChatFormatting.RED.toString() + "Mob"
        } else if (type == 3 || type == 2) {
            this.type = EnumChatFormatting.GOLD.toString() + "Treasure"
        }
    }

    fun drawWaypoint(partialTicks: Float) {
        RenderUtils.drawWaypoint(partialTicks, Vector3f(x.toFloat(), y.toFloat(), z.toFloat()), type +
                EnumChatFormatting.YELLOW + ", " + EnumChatFormatting.AQUA + chain + "/4")
        RenderUtils.highlightBlock(Vector3f(x.toFloat(), y.toFloat(), z.toFloat()), 0.6f, partialTicks, Color(0, 255, 233))
    }
}
