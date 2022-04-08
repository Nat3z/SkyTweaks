package natia.skytweaks.hooks

import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent

class KeyboardInputHook {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onKey(event: InputEvent.KeyInputEvent) {
    }
}
