package natia.skytweaks.hooks

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.SecretUtils
import natia.skytweaks.commands.waypoints.WaypointCreateGui
import natia.skytweaks.features.RepartyHook
import natia.skytweaks.features.dungeons.BatPointer
import natia.skytweaks.features.griffin.GriffinBurrowWaypoints
import natia.skytweaks.features.slayers.VoidGloom
import natia.skytweaks.features.waypoints.GlobalWaypoints
import natia.skytweaks.utils.AsyncAwait
import natia.skytweaks.utils.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color
import javax.vecmath.Vector3f

class WorldHook {

    var alertAlreadyRunning = false
    @SubscribeEvent
    fun load(event: WorldEvent.Load) {
        RepartyHook.instance.worldLoad()
        VoidGloom.instance.worldLoad()
        GriffinBurrowWaypoints.instance.worldLoad()
        GlobalWaypoints.instance.worldLoad()
        BatPointer.instance.worldLoad()

        if (Minecraft.getMinecraft().theWorld == null) return
        if (alertAlreadyRunning) return
        alertAlreadyRunning = true
        AsyncAwait.start({
            alertAlreadyRunning = false
            if (Minecraft.getMinecraft().theWorld != null) {
                val worldTime = Minecraft.getMinecraft().theWorld.worldTime
                val days: Long = worldTime / 24000L
                if (SkyTweaksConfig.serverDayNotifier) SecretUtils.sendMessage("The lobby you are currently in is on ${EnumChatFormatting.BOLD}${EnumChatFormatting.RED}Day $days")
            }
        }, 500)
        //TerminalHighlight.getInstance().worldLoad();
    }

    @SubscribeEvent
    fun blockDrawn(event: DrawBlockHighlightEvent) {
        GriffinBurrowWaypoints.instance.blockRender(event)
        //TerminalHighlight.getInstance().blockRender(event);
    }

    @SubscribeEvent(
        priority = EventPriority.HIGHEST
    )
    fun onWorldRender(event: RenderWorldLastEvent) {
        VoidGloom.instance.blockRender(event)
        GlobalWaypoints.instance.blockRender(event.partialTicks)

        if (Minecraft.getMinecraft().currentScreen is WaypointCreateGui) {
            val vec = Minecraft.getMinecraft().objectMouseOver ?: return
            val blockPos = vec.blockPos ?: return

            RenderUtils.highlightBlock(
                Vector3f(blockPos.x.toFloat(), blockPos.y.toFloat(), blockPos.z.toFloat()), .7f, event.partialTicks,
                Color.CYAN
            )
        }
    }
}
