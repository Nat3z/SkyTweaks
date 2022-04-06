package natia.skytweaks.core

import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.client.event.RenderBlockOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class BlockRenderingHook {

    @SubscribeEvent
    fun partialTicks(event: DrawBlockHighlightEvent) {
        globalPartialTicks = event.partialTicks
    }

    @SubscribeEvent
    fun drawBlock(event: RenderBlockOverlayEvent) {
        if (event.blockForOverlay.block == Blocks.skull) {
            skullBlockPos = event.blockPos
        }
    }

    companion object {

        var globalPartialTicks: Float = 0.toFloat()
        var skullBlockPos: BlockPos? = null
    }

}
