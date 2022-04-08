package natia.skytweaks.hooks

import natia.skytweaks.features.RepartyHook
import natia.skytweaks.features.griffin.GriffinBurrowWaypoints
import natia.skytweaks.features.slayers.VoidGloom
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class WorldHook {

    @SubscribeEvent
    fun load(event: WorldEvent.Load) {
        RepartyHook.instance.worldLoad()
        VoidGloom.instance.worldLoad()
        GriffinBurrowWaypoints.instance.worldLoad()
        //TerminalHighlight.getInstance().worldLoad();
    }

    @SubscribeEvent
    fun blockDrawn(event: DrawBlockHighlightEvent) {
        GriffinBurrowWaypoints.instance.blockRender(event)
        //TerminalHighlight.getInstance().blockRender(event);
    }

    @SubscribeEvent
    fun onWorldRender(event: RenderWorldLastEvent) {
        VoidGloom.instance.blockRender(event)
    }
}
