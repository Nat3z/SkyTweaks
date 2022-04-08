package natia.skytweaks.hooks

import natia.skytweaks.features.MinionAnalyzer
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class GuiScreenHook {

    @SubscribeEvent
    fun preBackground(event: GuiScreenEvent.BackgroundDrawnEvent) {
        MinionAnalyzer.instance.backgroundDrawn(event)
    }
}
