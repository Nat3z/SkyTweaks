package mixin.natia.skytweaks.mixins;

import natia.skytweaks.hooks.mixinhooks.MinecraftHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.main.GameConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {


    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(GameConfiguration gameConfig, CallbackInfo ci) {
        MinecraftHook.INSTANCE.checkUpdates(gameConfig, ci);
    }

    /* shutdown */
    @Redirect(method = "shutdownMinecraftApplet", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;unloadSounds()V"))
    public void shutdownMinecraftApplet_unloadSounds(SoundHandler soundHandler) {
        MinecraftHook.INSTANCE.shutdownTask();
    }
}
