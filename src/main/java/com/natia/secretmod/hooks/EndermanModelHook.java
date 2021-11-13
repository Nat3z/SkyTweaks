package com.natia.secretmod.hooks;

import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SecretModConfig;
import com.natia.secretmod.features.slayers.VoidGloom;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

public class EndermanModelHook {

    private static ResourceLocation empty_enderman = new ResourceLocation("secretmod", "entity/enderman-empty.png");
    private static ResourceLocation hit_state_enderman = new ResourceLocation("secretmod", "entity/enderman-hit.png");
    private static ResourceLocation damage_state_enderman = new ResourceLocation("secretmod", "entity/enderman-damage.png");


    /*public static void changeEndermanColor() {
        if (!SecretModConfig.seraphHelper) return;
        if (VoidGloom.slayerHealth.isEmpty()) return;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        String unformattedSlayer = StringUtils.stripControlCodes(VoidGloom.slayerHealth);

        if (unformattedSlayer.contains("Hits")) {
            Color color = new Color(SecretModConfig.seraphColorHIT);
            SecretUtils.bindColor(color);
        } else {
            Color color = new Color(SecretModConfig.seraphColorDAMAGE);
            SecretUtils.bindColor(color);
        }

    }*/

    public static void overrideTexture(CallbackInfoReturnable<ResourceLocation> cir) {
        if (!SecretModConfig.seraphHelper) return;
        if (!SecretModConfig.seraphColorize) return;
        if (VoidGloom.slayerHealth.isEmpty()) return;

        String unformattedSlayer = StringUtils.stripControlCodes(VoidGloom.slayerHealth);
        if (unformattedSlayer.contains("Hits")) {
            cir.setReturnValue(hit_state_enderman);
        } else {
            cir.setReturnValue(damage_state_enderman);
        }
    }
    /*public static void post() {
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }*/

}
