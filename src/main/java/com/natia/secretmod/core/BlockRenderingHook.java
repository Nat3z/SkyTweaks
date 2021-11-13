package com.natia.secretmod.core;

import com.natia.secretmod.config.SecretModConfig;
import com.natia.secretmod.utils.RenderUtils;
import net.minecraft.block.BlockSkull;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3f;
import java.awt.*;

public class BlockRenderingHook {

    public static float globalPartialTicks;
    public static BlockPos skullBlockPos;

    @SubscribeEvent
    public void partialTicks(DrawBlockHighlightEvent event) {
        globalPartialTicks = event.partialTicks;
        RenderUtils.highlightBlock(new Vector3f(0, 10, 0), 0.8f, event.partialTicks, new Color(SecretModConfig.yangGlyphHighlightColor));
    }

    @SubscribeEvent
    public void drawBlock(RenderBlockOverlayEvent event) {
        if (event.blockForOverlay.getBlock().equals(Blocks.skull)) {
            skullBlockPos = event.blockPos;
        }
    }

}
