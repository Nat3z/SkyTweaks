package com.natia.secretmod.core;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockRenderingHook {

    public static float globalPartialTicks;
    public static BlockPos skullBlockPos;

    @SubscribeEvent
    public void partialTicks(DrawBlockHighlightEvent event) {
        globalPartialTicks = event.partialTicks;
    }

    @SubscribeEvent
    public void drawBlock(RenderBlockOverlayEvent event) {
        if (event.blockForOverlay.getBlock().equals(Blocks.skull)) {
            skullBlockPos = event.blockPos;
        }
    }

}
