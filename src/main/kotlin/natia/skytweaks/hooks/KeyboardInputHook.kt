package natia.skytweaks.hooks

import natia.skytweaks.features.dungeons.NotifyCrush
import natia.skytweaks.features.waypoints.GlobalWaypoints
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.lwjgl.input.Keyboard

class KeyboardInputHook {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onKey(event: InputEvent.KeyInputEvent) {
        if (!Keyboard.getEventKeyState()) return
        GlobalWaypoints.instance.key()
        NotifyCrush.instance.key()
    }
}
