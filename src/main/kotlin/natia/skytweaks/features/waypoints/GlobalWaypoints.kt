package natia.skytweaks.features.waypoints

import com.google.common.base.Stopwatch
import jdk.nashorn.internal.objects.Global
import natia.skytweaks.SecretUtils
import natia.skytweaks.SecretUtils.parsable
import natia.skytweaks.SecretUtils.toVec3
import natia.skytweaks.SkyTweaks
import natia.skytweaks.utils.AsyncAwait
import natia.skytweaks.utils.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.lwjgl.input.Keyboard
import java.awt.Color
import java.util.concurrent.TimeUnit
import javax.vecmath.Vector3f

class GlobalWaypoints {
    val mc = Minecraft.getMinecraft()
    val waypoints: MutableList<Waypoint> = ArrayList()

    fun blockRender(partialTicks: Float) {
        for (wp in waypoints) {
            val posWP = wp.position.toVec3()

            RenderUtils.drawWaypoint(
                partialTicks, Vector3f(posWP.xCoord.toFloat(), posWP.yCoord.toFloat(), posWP.zCoord.toFloat()),
                wp.name
            )
            RenderUtils.highlightBlock(Vector3f(posWP.xCoord.toFloat(), posWP.yCoord.toFloat(), posWP.zCoord.toFloat()), .5f, partialTicks, Color.CYAN)
        }
    }

    // [MVP+] Nat3z: @SkyTweaks-WP={NAME OF WAYPOINT}|[x,y,z]
    // Party > [MVP+] Nat3z: @SkyTweaks-WP={NAME OF WAYPOINT}|[x,y,z]

    var targetWaypoint: Waypoint? = null

    var timeSinceLastWaypoint = -500
    var waypointStopwatch = Stopwatch.createUnstarted()
    fun chat(event: ClientChatReceivedEvent) {
        if (!waypointStopwatch.isRunning) waypointStopwatch.start()

        try {
            if (!event.message.unformattedText.contains(": ")) return
            val message = event.message.unformattedText.split(": ", limit = 2)[1]
            // user sending
            if (message.startsWith("@SkyTweaks-WP=") && message.endsWith("]")) {
                SkyTweaks.LOGGER.info(message)
                if (waypointStopwatch.elapsed(TimeUnit.SECONDS).toInt() - timeSinceLastWaypoint < 2) {
                    SecretUtils.sendWarning("Too many waypoints are being sent. Please wait 2 seconds.")
                }

                val userSending = event.message.unformattedText.split(": ")[0].split(" ")[
                        event.message.unformattedText.split(": ")[0].split(" ").size - 1
                ]
                if (userSending == mc.thePlayer.name) return
                timeSinceLastWaypoint = waypointStopwatch.elapsed(TimeUnit.SECONDS).toInt()

                val name = message.split("@SkyTweaks-WP=")[1].split("|[")[0]
                val xNotParsed = message.split("@SkyTweaks-WP=")[1].split("|[")[1]
                    .split(",")[0]
                val yNotParsed = message.split("@SkyTweaks-WP=")[1].split("|[")[1]
                    .split(",")[1]
                val zNotParsed = message.split("@SkyTweaks-WP=")[1].split("|[")[1]
                    .split(",")[2].replace("]", "")
                var x = 0
                var y = 0
                var z = 0
                if (xNotParsed.parsable) x = Integer.parseInt(xNotParsed)
                if (yNotParsed.parsable) y = Integer.parseInt(yNotParsed)
                if (zNotParsed.parsable) z = Integer.parseInt(zNotParsed)

                if (xNotParsed.startsWith("-")) x * -1
                if (yNotParsed.startsWith("-")) y * -1
                if (zNotParsed.startsWith("-")) z * -1

                SecretUtils.playLoudSound("random.orb", 0.5f)
                SecretUtils.sendMessage("A waypoint has been shared by " +
                        "${EnumChatFormatting.BOLD}${EnumChatFormatting.RED}$userSending${EnumChatFormatting.RESET}${EnumChatFormatting.YELLOW} called ${EnumChatFormatting.BOLD}${EnumChatFormatting.AQUA}${name}${EnumChatFormatting.RESET}${EnumChatFormatting.YELLOW} at $x, $y, $z" +
                        "\nDo you want to add this waypoint? \n" +
                        "    ${EnumChatFormatting.GREEN}[Y]es     ${EnumChatFormatting.RED}[N]o")
                val createdWaypoint = Waypoint(BlockPos(x, y, z), name)
                targetWaypoint = createdWaypoint

                AsyncAwait.start({
                    if (targetWaypoint != createdWaypoint) return@start
                    targetWaypoint = null
                    SecretUtils.sendWarning("Waypoint request removed.")
                }, 10000)
                event.isCanceled = true
            }
        } catch (ex: Exception) {
            event.isCanceled = true
            SecretUtils.sendWarning("An error occurred when parsing a user's SkyTweaks Waypoint.")
        }
    }

    fun key() {
        if (targetWaypoint == null) return
        if (Keyboard.getEventKey() == Keyboard.KEY_Y) {
            SecretUtils.sendMessage("Waypoint has been added!")
            waypoints.add(targetWaypoint!!)
            targetWaypoint = null
        } else if (Keyboard.getEventKey() == Keyboard.KEY_N) {
            SecretUtils.sendWarning("Waypoint has been denied.")
            targetWaypoint = null
        }
    }

    fun worldLoad() {
        waypoints.clear()
    }

    companion object {
        val instance = GlobalWaypoints()
    }
}