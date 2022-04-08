package natia.skytweaks.gui.pets

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.gui.base.SkyblockButton
import natia.skytweaks.utils.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack

import java.awt.*

class Pet(val petItem: ItemStack, val isFavorite: Boolean, val level: Int, var slotNumber: Int,
          id: Int, x: Int, y: Int) : SkyblockButton(id, x, y, 30, 30, "") {

    override fun renderButton(mouseX: Int, mouseY: Int) {
        if (visible) {
            this.isMouseOver = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height

            if (isMouseOver && SkyTweaksConfig.customPetsMenuShowPetLore) {
                val sr = ScaledResolution(Minecraft.getMinecraft())
                RenderUtils.drawHoveringText(listOf(petItem.displayName),
                        0, 20,
                        sr.scaledWidth, sr.scaledHeight, -1, Minecraft.getMinecraft().fontRendererObj)
            }

            if (this.level >= 100)
                Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, Color(145, 14, 0, 255).rgb)
            else if (this.level >= 30)
                Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, Color(145, 127, 16, 255).rgb)
            else
                Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, Color(103, 99, 101, 255).rgb)

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            RenderUtils.renderItem(petItem, xPosition + 3, yPosition + 3, 1.5f)
        }
    }
}
