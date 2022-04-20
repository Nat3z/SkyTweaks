package natia.skytweaks.hooks

import natia.skytweaks.features.DamagePerSecond
import natia.skytweaks.features.SummonsAlert
import natia.skytweaks.features.TimersHook
import natia.skytweaks.features.dungeons.BatPointer
import natia.skytweaks.features.fishing.WormCounter
import natia.skytweaks.features.fishing.WormTimer
import natia.skytweaks.features.slayers.RNGesusBar
import natia.skytweaks.features.slayers.VoidGloom
import net.minecraft.client.Minecraft
import net.minecraftforge.client.GuiIngameForge
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class RenderHook {

    @SubscribeEvent
    fun render(event: RenderGameOverlayEvent.Post) {
        /* prevent labymod from fucking everything up */
        if (Minecraft.getMinecraft().ingameGUI is GuiIngameForge) {
            if (event.type == RenderGameOverlayEvent.ElementType.EXPERIENCE || event.type == RenderGameOverlayEvent.ElementType.JUMPBAR) {
                VoidGloom.instance.render()
                TimersHook.instance.render()
                WormCounter.instance.render()
                WormTimer.instance.render()
                RNGesusBar.instance.render()
                DamagePerSecond.instance.render()
                BatPointer.instance.render()
//                SummonsAlert.instance.render()
            }
        }
    }

}
