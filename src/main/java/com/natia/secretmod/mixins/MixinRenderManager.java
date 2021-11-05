package com.natia.secretmod.mixins;

import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SecretModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(RenderManager.class)
public class MixinRenderManager {

    @Inject(method = "shouldRender(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;DDD)Z", at = @At("HEAD"), cancellable = true)
    public void shouldRender(Entity entityIn, ICamera camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir)
    {
        if (SecretUtils.isValid() && SecretModConfig.dontRenderPlayersInHub) {
            List<String> scoreboardlines = SecretUtils.getScoreboardLines();
            for (String sc : scoreboardlines) {
                String sCleaned = SecretUtils.cleanSB(sc);
                if (sCleaned.contains("Village") || sCleaned.contains("Bazaar") || sCleaned.contains("Auction House")) {
                    if (entityIn instanceof EntityPlayer && entityIn != Minecraft.getMinecraft().thePlayer && !SecretUtils.isNPC(entityIn)) {
                        cir.setReturnValue(false);
                        break;
                    }
                }
            }
        }

    }

}
