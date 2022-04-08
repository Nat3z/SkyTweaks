package mixin.natia.skytweaks.mixins;

import mixin.natia.skytweaks.SkyTweaksConfig;
import natia.skytweaks.SecretUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderManager.class)
public class MixinRenderManager {
    
    @Inject(method = "shouldRender(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;DDD)Z", at = @At("HEAD"), cancellable = true)
    public void shouldRender(Entity entityIn, ICamera camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir)
    {
        if (SecretUtils.INSTANCE.isValid() && SkyTweaksConfig.dontRenderPlayersInHub) {
            if (SecretUtils.INSTANCE.isInHub()) {
                if (entityIn instanceof EntityPlayer && entityIn != Minecraft.getMinecraft().thePlayer && !SecretUtils.INSTANCE.isNPC(entityIn)) {
                    cir.setReturnValue(false);
                }
            }
        }

    }

}
