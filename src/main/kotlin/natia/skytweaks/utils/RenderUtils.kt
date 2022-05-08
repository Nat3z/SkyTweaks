package natia.skytweaks.utils

import mixin.natia.skytweaks.SkyTweaksConfig
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import org.lwjgl.opengl.GL11
import java.awt.*
import javax.vecmath.Vector3f


object RenderUtils {
    private val beaconBeam = ResourceLocation("textures/entity/beacon_beam.png")
    /**
     * @author NatiaDev
     * Renders color on block, acting as if it's an entirely different block
     */
    fun highlightBlock(coords: Vector3f, alpha: Float, partialTicks: Float, c: Color) {
        val player = Minecraft.getMinecraft().thePlayer
        val d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks.toDouble()
        val d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks.toDouble()
        val d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks.toDouble()

        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.color(0.0f, 0.0f, 0.0f, 1f)
        GlStateManager.enableBlend()
        GlStateManager.disableDepth()
        GlStateManager.depthMask(false)

        RenderUtils.drawFilledBoundingBox(Blocks.stone.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, BlockPos(coords.x.toDouble(), coords.y.toDouble(), coords.z.toDouble())).offset(-d0, -d1, -d2), alpha, c)
        GlStateManager.depthMask(true)
        GlStateManager.disableBlend()
        GlStateManager.enableDepth()
    }

    /**
     * Taken from NotEnoughUpdates under Creative Commons Attribution-NonCommercial 3.0
     * https://github.com/Moulberry/NotEnoughUpdates/blob/master/LICENSE
     * @author Moulberry
     */
    fun drawFilledBoundingBox(aabb: AxisAlignedBB, alphaMultiplier: Float, c: Color) {
        GlStateManager.enableBlend()
        GlStateManager.disableLighting()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.disableTexture2D()

        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer

        GlStateManager.color(c.red / 255f, c.green / 255f, c.blue / 255f, c.alpha / 255f * alphaMultiplier)

        //vertical
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        tessellator.draw()
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        tessellator.draw()


        GlStateManager.color(c.red / 255f * 0.8f, c.green / 255f * 0.8f, c.blue / 255f * 0.8f, c.alpha / 255f * alphaMultiplier)

        //x
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        tessellator.draw()
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        tessellator.draw()


        GlStateManager.color(c.red / 255f * 0.9f, c.green / 255f * 0.9f, c.blue / 255f * 0.9f, c.alpha / 255f * alphaMultiplier)
        //z
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        tessellator.draw()
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        tessellator.draw()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    /**
     * @author Mojang
     */
    fun drawLabel(pos: Vec3, text: String, color: Color, partialTicks: Float, shadow: Boolean = false, scale: Float = 1f) {
        GlStateManager.disableCull()
        GlStateManager.disableDepth()

        val mc = Minecraft.getMinecraft()
        val player = mc.thePlayer
        val x =
            pos.xCoord - player.lastTickPosX + (pos.xCoord - player.posX - (pos.xCoord - player.lastTickPosX)) * partialTicks
        val y =
            pos.yCoord - player.lastTickPosY + (pos.yCoord - player.posY - (pos.yCoord - player.lastTickPosY)) * partialTicks
        val z =
            pos.zCoord - player.lastTickPosZ + (pos.zCoord - player.posZ - (pos.zCoord - player.lastTickPosZ)) * partialTicks
        val renderManager = mc.renderManager
        val f1 = 0.0266666688
        val width = mc.fontRendererObj.getStringWidth(text) / 2
        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, z)
        GL11.glNormal3f(0f, 1f, 0f)
        GlStateManager.rotate(-renderManager.playerViewY, 0f, 1f, 0f)
        GlStateManager.rotate(renderManager.playerViewX, 1f, 0f, 0f)
        GlStateManager.scale(-f1, -f1, -f1)
        GlStateManager.scale(scale, scale, scale)
        GlStateManager.enableBlend()
        GlStateManager.disableLighting()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.enableTexture2D()
        mc.fontRendererObj.drawString(text, (-width).toFloat(), 0f, color.rgb, shadow)
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()

        GlStateManager.enableDepth()
        GlStateManager.enableCull()
    }


    fun drawPointers(vector: Vec3, color: Color) {
        val mc = Minecraft.getMinecraft()
        val pos = vector
        val sr = ScaledResolution(mc)

        val guiLeft = (sr.scaledWidth - 176) / 2.0
        val guiTop = (sr.scaledHeight - 222) / 2.0

        val x = guiLeft + 88.5
        val y = guiTop + 110
        val angle: Double = -(MathHelper.atan2(
            pos.xCoord - mc.thePlayer.posX,
            pos.zCoord - mc.thePlayer.posZ
        ) * 57.29577951308232) - mc.thePlayer.rotationYaw
        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, 0.0)
        GlStateManager.rotate(angle.toFloat(), 0f, 0f, 1f)
        GlStateManager.translate(-x, -y, 0.0)
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        val tes = Tessellator.getInstance()
        val wr = tes.worldRenderer
        GlStateManager.color(color.red.toFloat() / 255f, color.green.toFloat() / 255f, color.blue.toFloat() / 255f)
        GL11.glLineWidth(5f)
        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION)
        wr.pos(x + 10, y + 45, 0.0).endVertex()
        wr.pos(x, y, 0.0).endVertex()
        wr.pos(x - 10, y + 45, 0.0).endVertex()
        tes.draw()

        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }

    fun renderTexturedRect(size: Int, xPos: Int, yPos: Int, resource: ResourceLocation) {
        var x: Int = xPos
        var y: Int = yPos
        GlStateManager.disableLighting()
        GlStateManager.disableDepth()
        GlStateManager.enableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.color(1f, 1f, 1f, 1f)
        Minecraft.getMinecraft().textureManager.bindTexture(resource)
        GlStateManager.blendFunc(770, 771)
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_BLEND)
        GL11.glTranslated(0.0, 0.0, 1.0)
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0f, 0f, 16, 16, 16f, 16f)
        GlStateManager.color(1f, 1f, 1f, 1f)
        GL11.glTranslated(0.0, 0.0, -1.0)
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE)
        GlStateManager.enableLighting()
        GlStateManager.enableDepth()
        GlStateManager.disableAlpha()
    }

    /**
     * Renders a beacon beam. Recommended to place this under a BlockHighlight FML event.
     * @author NatiaDev
     */
    fun showBeam(loc: Vector3f, color: Color, partialTicks: Float) {
        val viewer = Minecraft.getMinecraft().renderViewEntity
        val viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks
        val viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks
        val viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks

        val pos = BlockPos(loc.x.toDouble(), loc.y.toDouble(), loc.z.toDouble())
        val x = pos.x - viewerX
        val y = pos.y - viewerY
        val z = pos.z - viewerZ

        GlStateManager.disableDepth()
        GlStateManager.disableCull()
        GlStateManager.disableTexture2D()
        RenderUtils.renderBeaconBeam(x, y + 1, z, color.rgb, 1.0f, partialTicks)
        GlStateManager.disableLighting()
        GlStateManager.enableTexture2D()
        GlStateManager.enableDepth()
        GlStateManager.enableCull()
    }

    /**
     * Taken from NotEnoughUpdates under Creative Commons Attribution-NonCommercial 3.0
     * https://github.com/Moulberry/NotEnoughUpdates/blob/master/LICENSE
     * @author Moulberry
     */
    private fun renderBeaconBeam(x: Double, y: Double, z: Double, rgb: Int, alphaMultiplier: Float, partialTicks: Float) {
        val height = 300
        val bottomOffset = 0
        val topOffset = bottomOffset + height

        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer

        Minecraft.getMinecraft().textureManager.bindTexture(beaconBeam)
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0f)
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0f)
        GlStateManager.disableLighting()
        GlStateManager.enableCull()
        GlStateManager.enableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0)
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)

        val time = Minecraft.getMinecraft().theWorld.totalWorldTime + partialTicks.toDouble()
        val d1 = MathHelper.func_181162_h(-time * 0.2 - MathHelper.floor_double(-time * 0.1).toDouble())

        val r = (rgb shr 16 and 0xFF) / 255f
        val g = (rgb shr 8 and 0xFF) / 255f
        val b = (rgb and 0xFF) / 255f
        val d2 = time * 0.025 * -1.5
        val d4 = 0.5 + Math.cos(d2 + 2.356194490192345) * 0.2
        val d5 = 0.5 + Math.sin(d2 + 2.356194490192345) * 0.2
        val d6 = 0.5 + Math.cos(d2 + Math.PI / 4.0) * 0.2
        val d7 = 0.5 + Math.sin(d2 + Math.PI / 4.0) * 0.2
        val d8 = 0.5 + Math.cos(d2 + 3.9269908169872414) * 0.2
        val d9 = 0.5 + Math.sin(d2 + 3.9269908169872414) * 0.2
        val d10 = 0.5 + Math.cos(d2 + 5.497787143782138) * 0.2
        val d11 = 0.5 + Math.sin(d2 + 5.497787143782138) * 0.2
        val d14 = -1.0 + d1
        val d15 = height.toDouble() * 2.5 + d14
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
        worldrenderer.pos(x + d4, y + topOffset, z + d5).tex(1.0, d15).color(r, g, b, 1.0f * alphaMultiplier).endVertex()
        worldrenderer.pos(x + d4, y + bottomOffset, z + d5).tex(1.0, d14).color(r, g, b, 1.0f).endVertex()
        worldrenderer.pos(x + d6, y + bottomOffset, z + d7).tex(0.0, d14).color(r, g, b, 1.0f).endVertex()
        worldrenderer.pos(x + d6, y + topOffset, z + d7).tex(0.0, d15).color(r, g, b, 1.0f * alphaMultiplier).endVertex()
        worldrenderer.pos(x + d10, y + topOffset, z + d11).tex(1.0, d15).color(r, g, b, 1.0f * alphaMultiplier).endVertex()
        worldrenderer.pos(x + d10, y + bottomOffset, z + d11).tex(1.0, d14).color(r, g, b, 1.0f).endVertex()
        worldrenderer.pos(x + d8, y + bottomOffset, z + d9).tex(0.0, d14).color(r, g, b, 1.0f).endVertex()
        worldrenderer.pos(x + d8, y + topOffset, z + d9).tex(0.0, d15).color(r, g, b, 1.0f * alphaMultiplier).endVertex()
        worldrenderer.pos(x + d6, y + topOffset, z + d7).tex(1.0, d15).color(r, g, b, 1.0f * alphaMultiplier).endVertex()
        worldrenderer.pos(x + d6, y + bottomOffset, z + d7).tex(1.0, d14).color(r, g, b, 1.0f).endVertex()
        worldrenderer.pos(x + d10, y + bottomOffset, z + d11).tex(0.0, d14).color(r, g, b, 1.0f).endVertex()
        worldrenderer.pos(x + d10, y + topOffset, z + d11).tex(0.0, d15).color(r, g, b, 1.0f * alphaMultiplier).endVertex()
        worldrenderer.pos(x + d8, y + topOffset, z + d9).tex(1.0, d15).color(r, g, b, 1.0f * alphaMultiplier).endVertex()
        worldrenderer.pos(x + d8, y + bottomOffset, z + d9).tex(1.0, d14).color(r, g, b, 1.0f).endVertex()
        worldrenderer.pos(x + d4, y + bottomOffset, z + d5).tex(0.0, d14).color(r, g, b, 1.0f).endVertex()
        worldrenderer.pos(x + d4, y + topOffset, z + d5).tex(0.0, d15).color(r, g, b, 1.0f * alphaMultiplier).endVertex()
        tessellator.draw()

        GlStateManager.disableCull()
        val d12 = -1.0 + d1
        val d13 = height + d12

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
        worldrenderer.pos(x + 0.2, y + topOffset, z + 0.2).tex(1.0, d13).color(r, g, b, 0.25f * alphaMultiplier).endVertex()
        worldrenderer.pos(x + 0.2, y + bottomOffset, z + 0.2).tex(1.0, d12).color(r, g, b, 0.25f).endVertex()
        worldrenderer.pos(x + 0.8, y + bottomOffset, z + 0.2).tex(0.0, d12).color(r, g, b, 0.25f).endVertex()
        worldrenderer.pos(x + 0.8, y + topOffset, z + 0.2).tex(0.0, d13).color(r, g, b, 0.25f * alphaMultiplier).endVertex()
        worldrenderer.pos(x + 0.8, y + topOffset, z + 0.8).tex(1.0, d13).color(r, g, b, 0.25f * alphaMultiplier).endVertex()
        worldrenderer.pos(x + 0.8, y + bottomOffset, z + 0.8).tex(1.0, d12).color(r, g, b, 0.25f).endVertex()
        worldrenderer.pos(x + 0.2, y + bottomOffset, z + 0.8).tex(0.0, d12).color(r, g, b, 0.25f).endVertex()
        worldrenderer.pos(x + 0.2, y + topOffset, z + 0.8).tex(0.0, d13).color(r, g, b, 0.25f * alphaMultiplier).endVertex()
        worldrenderer.pos(x + 0.8, y + topOffset, z + 0.2).tex(1.0, d13).color(r, g, b, 0.25f * alphaMultiplier).endVertex()
        worldrenderer.pos(x + 0.8, y + bottomOffset, z + 0.2).tex(1.0, d12).color(r, g, b, 0.25f).endVertex()
        worldrenderer.pos(x + 0.8, y + bottomOffset, z + 0.8).tex(0.0, d12).color(r, g, b, 0.25f).endVertex()
        worldrenderer.pos(x + 0.8, y + topOffset, z + 0.8).tex(0.0, d13).color(r, g, b, 0.25f * alphaMultiplier).endVertex()
        worldrenderer.pos(x + 0.2, y + topOffset, z + 0.8).tex(1.0, d13).color(r, g, b, 0.25f * alphaMultiplier).endVertex()
        worldrenderer.pos(x + 0.2, y + bottomOffset, z + 0.8).tex(1.0, d12).color(r, g, b, 0.25f).endVertex()
        worldrenderer.pos(x + 0.2, y + bottomOffset, z + 0.2).tex(0.0, d12).color(r, g, b, 0.25f).endVertex()
        worldrenderer.pos(x + 0.2, y + topOffset, z + 0.2).tex(0.0, d13).color(r, g, b, 0.25f * alphaMultiplier).endVertex()
        tessellator.draw()
    }

    /**
     * Taken from Danker's Skyblock Mod under GPL-3.0 License
     * @author Dankers
     * https://github.com/bowser0000/SkyblockMod/
     */
    fun renderItem(item: ItemStack, x: Int, y: Int, scale: Float) {

        GlStateManager.enableRescaleNormal()
        RenderHelper.enableGUIStandardItemLighting()
        GlStateManager.enableDepth()
        GlStateManager.pushMatrix()
        GlStateManager.translate(x.toDouble(), y.toDouble(), (-100f).toDouble())
        GlStateManager.scale(scale, scale, 1f)
        GL11.glTranslated(0.0, 0.0, 1.0)
        Minecraft.getMinecraft().renderItem.renderItemIntoGUI(item, 0, 0)
        GL11.glTranslated(0.0, 0.0, -1.0)

        GlStateManager.popMatrix()

        GlStateManager.disableDepth()
        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()
    }

    /**
     * Taken from NotEnoughUpdates under Creative Commons Attribution-NonCommercial 3.0
     * https://github.com/Moulberry/NotEnoughUpdates/blob/master/LICENSE
     * @author Moulberry
     */
    fun drawHoveringText(textLines: List<String>, mouseX: Int, mouseY: Int, screenWidth: Int, screenHeight: Int, maxTextWidth: Int, font: FontRenderer) {
        var textLines = textLines
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal()
            RenderHelper.disableStandardItemLighting()
            GlStateManager.disableLighting()
            GlStateManager.disableDepth()
            var tooltipTextWidth = 0

            for (textLine in textLines) {
                val textLineWidth = font.getStringWidth(textLine)

                if (textLineWidth > tooltipTextWidth) {
                    tooltipTextWidth = textLineWidth
                }
            }

            var needsWrap = false

            var titleLinesCount = 1
            var tooltipX = mouseX + 12
            if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
                tooltipX = mouseX - 16 - tooltipTextWidth
                if (tooltipX < 4)
                // if the tooltip doesn't fit on the screen
                {
                    if (mouseX > screenWidth / 2) {
                        tooltipTextWidth = mouseX - 12 - 8
                    } else {
                        tooltipTextWidth = screenWidth - 16 - mouseX
                    }
                    needsWrap = true
                }
            }

            if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
                tooltipTextWidth = maxTextWidth
                needsWrap = true
            }

            if (needsWrap) {
                var wrappedTooltipWidth = 0
                val wrappedTextLines = ArrayList<String>()
                for (i in textLines.indices) {
                    val textLine = textLines[i]
                    val wrappedLine = font.listFormattedStringToWidth(textLine, tooltipTextWidth)
                    if (i == 0) {
                        titleLinesCount = wrappedLine.size
                    }

                    for (line in wrappedLine) {
                        val lineWidth = font.getStringWidth(line)
                        if (lineWidth > wrappedTooltipWidth) {
                            wrappedTooltipWidth = lineWidth
                        }
                        wrappedTextLines.add(line)
                    }
                }
                tooltipTextWidth = wrappedTooltipWidth
                textLines = wrappedTextLines

                if (mouseX > screenWidth / 2) {
                    tooltipX = mouseX - 16 - tooltipTextWidth
                } else {
                    tooltipX = mouseX + 12
                }
            }

            var tooltipY = mouseY - 12
            var tooltipHeight = 8

            if (textLines.size > 1) {
                tooltipHeight += (textLines.size - 1) * 10
                if (textLines.size > titleLinesCount) {
                    tooltipHeight += 2 // gap between title lines and next lines
                }
            }

            if (tooltipY + tooltipHeight + 6 > screenHeight) {
                tooltipY = screenHeight - tooltipHeight - 6
            }

            val zLevel = 300
            val backgroundColor = -0xfeffff0
            RenderUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor)
            RenderUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor)
            RenderUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor)
            RenderUtils.drawGradientRect(zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor)
            RenderUtils.drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor)
            val borderColorStart = 0x505000FF
            val borderColorEnd = borderColorStart and 0xFEFEFE shr 1 or (borderColorStart and -0x1000000)
            RenderUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd)
            RenderUtils.drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd)
            RenderUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart)
            RenderUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd)

            for (lineNumber in textLines.indices) {
                val line = textLines[lineNumber]
                font.drawStringWithShadow(line, tooltipX.toFloat(), tooltipY.toFloat(), -1)

                if (lineNumber + 1 == titleLinesCount) {
                    tooltipY += 2
                }

                tooltipY += 10
            }

            GlStateManager.enableLighting()
            GlStateManager.enableDepth()
            RenderHelper.enableStandardItemLighting()
            GlStateManager.enableRescaleNormal()
        }
        GlStateManager.disableLighting()
    }

    /**
     * Taken from NotEnoughUpdates under Creative Commons Attribution-NonCommercial 3.0
     * https://github.com/Moulberry/NotEnoughUpdates/blob/master/LICENSE
     * @author Moulberry
     */
    fun drawGradientRect(zLevel: Int, left: Int, top: Int, right: Int, bottom: Int, startColor: Int, endColor: Int) {
        val startAlpha = (startColor shr 24 and 255).toFloat() / 255.0f
        val startRed = (startColor shr 16 and 255).toFloat() / 255.0f
        val startGreen = (startColor shr 8 and 255).toFloat() / 255.0f
        val startBlue = (startColor and 255).toFloat() / 255.0f
        val endAlpha = (endColor shr 24 and 255).toFloat() / 255.0f
        val endRed = (endColor shr 16 and 255).toFloat() / 255.0f
        val endGreen = (endColor shr 8 and 255).toFloat() / 255.0f
        val endBlue = (endColor and 255).toFloat() / 255.0f

        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.shadeModel(7425)

        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
        worldrenderer.pos(right.toDouble(), top.toDouble(), zLevel.toDouble()).color(startRed, startGreen, startBlue, startAlpha).endVertex()
        worldrenderer.pos(left.toDouble(), top.toDouble(), zLevel.toDouble()).color(startRed, startGreen, startBlue, startAlpha).endVertex()
        worldrenderer.pos(left.toDouble(), bottom.toDouble(), zLevel.toDouble()).color(endRed, endGreen, endBlue, endAlpha).endVertex()
        worldrenderer.pos(right.toDouble(), bottom.toDouble(), zLevel.toDouble()).color(endRed, endGreen, endBlue, endAlpha).endVertex()
        tessellator.draw()

        GlStateManager.shadeModel(7424)
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
    }

    /**
     * Taken from Skytils under AGPL-3.0
     * https://github.com/Skytils/SkytilsMod/blob/1.x/LICENSE.md
     * @author Sychic
     * @param partialTicks
     */
    fun drawWaypoint(partialTicks: Float, loc: Vector3f, waypointText: String) {
        val viewer = Minecraft.getMinecraft().renderViewEntity
        val viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks
        val viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks
        val viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks

        val pos = BlockPos(loc.x.toDouble(), loc.y.toDouble(), loc.z.toDouble())
        val x = pos.x - viewerX
        val y = pos.y - viewerY
        val z = pos.z - viewerZ
        val distSq = x * x + y * y + z * z

        GlStateManager.disableDepth()
        GlStateManager.disableCull()
        GlStateManager.disableTexture2D()
        if (distSq > 5 * 5) RenderUtils.renderBeaconBeam(x, y + 1, z, Color(0, 255, 233).rgb, 1.0f, partialTicks)
        RenderUtils.renderWaypointText(waypointText, pos, partialTicks)
        GlStateManager.disableLighting()
        GlStateManager.enableTexture2D()
        GlStateManager.enableDepth()
        GlStateManager.enableCull()
    }

    /**
     * Taken from NotEnoughUpdates under Creative Commons Attribution-NonCommercial 3.0
     * https://github.com/Moulberry/NotEnoughUpdates/blob/master/LICENSE
     * @author Moulberry
     */
    fun renderWaypointText(str: String, loc: BlockPos, partialTicks: Float) {
        GlStateManager.alphaFunc(516, 0.1f)

        GlStateManager.pushMatrix()

        val viewer = Minecraft.getMinecraft().renderViewEntity
        val viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks
        val viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks
        val viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks

        var x = loc.x - viewerX
        var y = loc.y.toDouble() - viewerY - viewer.eyeHeight.toDouble()
        var z = loc.z - viewerZ

        val distSq = x * x + y * y + z * z
        val dist = Math.sqrt(distSq)
        if (distSq > 144) {
            x *= 12 / dist
            y *= 12 / dist
            z *= 12 / dist
        }
        GlStateManager.translate(x, y, z)
        GlStateManager.translate(0f, viewer.eyeHeight, 0f)

        drawNametag(str + EnumChatFormatting.YELLOW + " (" + Math.round(dist) + "m" + ")")

        GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewX, 1.0f, 0.0f, 0.0f)

        GlStateManager.popMatrix()

        GlStateManager.disableLighting()
    }

    /**
     * Taken from NotEnoughUpdates under Creative Commons Attribution-NonCommercial 3.0
     * https://github.com/Moulberry/NotEnoughUpdates/blob/master/LICENSE
     */
    fun drawNametag(str: String) {
        val fontrenderer = Minecraft.getMinecraft().fontRendererObj
        val f = 1.6f
        val f1 = 0.016666668f * f
        GlStateManager.pushMatrix()
        GL11.glNormal3f(0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        GlStateManager.scale(-f1, -f1, f1)
        GlStateManager.disableLighting()
        GlStateManager.depthMask(false)
        GlStateManager.disableDepth()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        val i = 0

        val j = fontrenderer.getStringWidth(str) / 2
        GlStateManager.disableTexture2D()
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
        worldrenderer.pos((-j - 1).toDouble(), (-1 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
        worldrenderer.pos((-j - 1).toDouble(), (8 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
        worldrenderer.pos((j + 1).toDouble(), (8 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
        worldrenderer.pos((j + 1).toDouble(), (-1 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
        tessellator.draw()
        GlStateManager.enableTexture2D()
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127)
        GlStateManager.depthMask(true)

        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1)

        GlStateManager.enableDepth()
        GlStateManager.enableBlend()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.popMatrix()
    }

}
