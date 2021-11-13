package com.natia.secretmod.mixins;

import com.mojang.authlib.GameProfile;
import com.natia.secretmod.config.SecretModConfig;
import com.natia.secretmod.core.BlockRenderingHook;
import com.natia.secretmod.utils.RenderUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.vecmath.Vector3f;
import java.awt.*;
import java.util.UUID;

@Mixin(TileEntitySkullRenderer.class)
public abstract class MixinSkullRenderer extends TileEntitySpecialRenderer<TileEntitySkull> {

    @Inject(method = "renderTileEntityAt", at = @At("HEAD"))
    public void renderTileEntityAt(TileEntitySkull te, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {
        if (!SecretModConfig.yangGlyphHighlights) return;
        if (BlockRenderingHook.skullBlockPos == null) return;

        /* better variable names */
        Vector3f coords = new Vector3f((float)x, (float)y, (float)z);
        GameProfile profile = te.getPlayerProfile();

        if (profile != null) {
            UUID uuid = EntityPlayer.getUUID(profile);
            /* humanoid type */
            if (te.getSkullType() == 3) {
                RenderUtils.highlightBlock(coords, 0.8f, BlockRenderingHook.globalPartialTicks, new Color(SecretModConfig.yangGlyphHighlightColor));
            }
        }
    }

}
