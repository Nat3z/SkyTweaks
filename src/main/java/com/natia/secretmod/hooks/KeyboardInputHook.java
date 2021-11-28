package com.natia.secretmod.hooks;

import com.natia.secretmod.features.altC.QuickTab;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyboardInputHook {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onKey(InputEvent.KeyInputEvent event) {
    }
}
