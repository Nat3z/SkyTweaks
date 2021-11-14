package com.natia.secretmod.features.dungeons;

import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SecretModConfig;
import com.natia.secretmod.utils.Location;
import com.natia.secretmod.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3f;
import java.awt.*;

public class TerminalHighlight {

    private Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onBlockRender(DrawBlockHighlightEvent event) {
        if (!SecretModConfig.terminalHighlight) return;
        if (Location.getCurrentLocation() != Location.THE_CATACOMBS || SecretUtils.isInDungeons() != Location.THE_CATACOMBS) return;
        BlockPos playerPos = new BlockPos(mc.thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ);

        SecretUtils.getBlocksInBox(mc.theWorld,
                new BlockPos(playerPos.getX() + 80, playerPos.getY() + 80, playerPos.getZ() + 80),
                new BlockPos(playerPos.getX() - 80, playerPos.getY() - 80, playerPos.getZ() - 80))
        .forEach((block, pos) -> {
            if (block.equals(Blocks.command_block)) {
                for (EntityArmorStand armorStand : mc.theWorld.getEntitiesWithinAABB(EntityArmorStand.class,
                        new AxisAlignedBB(pos.getX() + 10, pos.getY() + 10, pos.getZ() + 10,
                        pos.getX() - 10, pos.getY() - 10, pos.getZ() - 10))) {
                    if (StringUtils.stripControlCodes(armorStand.getName()).contains("Inactive Terminal")) {
                        RenderUtils.highlightBlock(new Vector3f(pos.getX(), pos.getY(), pos.getZ()), 0.5f, event.partialTicks, new Color(SecretModConfig.terminalHighlightColorINACTIVE));
                        break;
                    } else if (StringUtils.stripControlCodes(armorStand.getName()).contains("Terminal Active")) {
                        RenderUtils.highlightBlock(new Vector3f(pos.getX(), pos.getY(), pos.getZ()), 0.5f, event.partialTicks, new Color(SecretModConfig.terminalHighlightColorACTIVE));
                        break;
                    }
                }
            }
        });
    }
}
