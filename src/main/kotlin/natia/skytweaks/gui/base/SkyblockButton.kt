package natia.skytweaks.gui.base

import jline.internal.Nullable
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation

import java.awt.*

open class SkyblockButton {

    var xPosition: Int = 0
    var yPosition: Int = 0
    var width: Int = 0
    var height: Int = 0
    var id: Int = 0
    var displayText: String
    var visible: Boolean = false
    private var textureWidth: Int = 0
    private var textureHeight: Int = 0
    @Nullable
    var texture: ResourceLocation? = null

    var mouseX = 0
    var mouseY = 0

    var isMouseOver = false

    constructor(id: Int, x: Int, y: Int, width: Int, height: Int, texture: ResourceLocation, textureWidth: Int, textureHeight: Int, displayText: String) {
        this.xPosition = x
        this.visible = true
        this.yPosition = y
        this.width = width
        this.height = height
        this.textureWidth = textureWidth
        this.textureHeight = textureHeight
        this.texture = texture
        this.id = id
        this.displayText = displayText
    }

    constructor(id: Int, x: Int, y: Int, width: Int, height: Int, displayText: String) {
        this.xPosition = x
        this.visible = true
        this.yPosition = y
        this.width = width
        this.height = height
        this.textureWidth = 0
        this.textureHeight = 0
        this.texture = null
        this.id = id
        this.displayText = displayText
    }

    open fun renderButton(mouseX: Int, mouseY: Int) {
        if (visible) {
            this.isMouseOver = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height
            this.mouseX = mouseX
            this.mouseY = mouseY

            if (texture == null) {
                if (isMouseOver) {
                    Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, Color(148, 146, 142, 40).rgb)
                } else {
                    Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, Color(68, 116, 179, 100).rgb)
                }
            } else {
                Minecraft.getMinecraft().textureManager.bindTexture(texture)
                Gui.drawModalRectWithCustomSizedTexture(xPosition, yPosition, 0f, 0f, width + textureWidth, height + textureHeight, textureWidth.toFloat(), textureHeight.toFloat())
            }

            drawCenteredString(displayText, xPosition + width / 2,
                    yPosition + height / 2 - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2,
                    Color.white.rgb, 1.0)
        }
    }

    private fun drawCenteredString(text: String, x: Int, y: Int, color: Int, scale: Double) {
        GlStateManager.pushMatrix()
        GlStateManager.scale(scale, scale, 1.0)
        Minecraft.getMinecraft().fontRendererObj.drawString(text,
                ((x / scale).toInt() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) / 2).toFloat(),
                (y / scale).toInt().toFloat(), color, true)
        GlStateManager.popMatrix()
    }
}
