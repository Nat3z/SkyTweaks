package natia.skytweaks.hooks

import com.google.common.base.Stopwatch
import natia.skytweaks.SecretUtils
import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.features.bazaar.Notifier
import natia.skytweaks.features.griffin.GriffinBurrowWaypoints
import natia.skytweaks.features.slayers.VoidGloom
import natia.skytweaks.utils.AsyncAwait
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.item.ItemStack
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

import java.util.Arrays
import java.util.concurrent.TimeUnit

class TickHook {
    private var ticks: Long = 0
    private val mc = Minecraft.getMinecraft()
    private val tickEventWatch = Stopwatch.createStarted()

    @SubscribeEvent(
        priority = EventPriority.HIGHEST
    )
    fun onTick(event: TickEvent.ClientTickEvent) {
        /* Tick Hook */
        VoidGloom.instance.tick()
        GriffinBurrowWaypoints.instance.tick()

        Notifier.instance.tick();

        if (mc.theWorld == null) return

        if (scheduledScreen != null) {
            mc.displayGuiScreen(scheduledScreen)
            scheduledScreen = null
            return
        }
        val p = mc.thePlayer
        ticks++
        // checks every 15 ticks (yes i am inspired by sba)
        if (ticks % 15 == 0L) {
            SecretUtils.getInventoryDiff(p.inventory.mainInventory)
            lastInventory = p.inventory.mainInventory.toMutableList()

            if (tickEventWatch.elapsed(TimeUnit.SECONDS) >= 10 && SkyTweaksConfig.bazaarCaching) {
                tickEventWatch.reset()
                if (!tickEventWatch.isRunning) tickEventWatch.start()

                Thread {
                    SecretUtils.updateBazaarCache()
                }.start()
            }
        }
    }

    var lastInventory: MutableList<ItemStack> = ArrayList()

    @SubscribeEvent
    fun onSwitch(event: WorldEvent.Load) {
        if (lastInventory.isEmpty()) return
        SecretUtils.setPreviousInventory(lastInventory)
    }

    companion object {
        var scheduledScreen: GuiScreen? = null
        @JvmStatic
        fun scheduleGui(screen: GuiScreen) {
            scheduledScreen = screen
        }
    }
}
