package mixin.natia.skytweaks.mixins;

import mixin.natia.skytweaks.SkyTweaksConfig;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiNewChat.class)
public class MixinNewChat {
    @Inject(method = "printChatMessageWithOptionalDeletion", at = @At(value = "INVOKE", target =
            "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V"), cancellable = true)
    public void printChatMessageWithOptionalDeletion_LOGGER(IChatComponent chatComponent, int chatLineId, CallbackInfo ci) {
        /* prevent logging chat messages */
        if (SkyTweaksConfig.stopLog4J)
            ci.cancel();
    }
}
