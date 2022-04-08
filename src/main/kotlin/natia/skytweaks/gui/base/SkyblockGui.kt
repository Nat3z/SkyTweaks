package natia.skytweaks.gui.base

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import org.lwjgl.input.Mouse

import java.util.ArrayList

open class SkyblockGui {
    private val mc = Minecraft.getMinecraft()
    var buttonList: MutableList<SkyblockButton> = ArrayList()
    var isOpened = false

    /**
     * When Gui is being rendered, this method is called.
     */
    open fun render(container: ContainerChest) {
        val scaledResolution = ScaledResolution(Minecraft.getMinecraft())
        val width = scaledResolution.scaledWidth
        val height = scaledResolution.scaledHeight
        val mouseX = Mouse.getX() * width / Minecraft.getMinecraft().displayWidth
        val mouseY = height - Mouse.getY() * height / Minecraft.getMinecraft().displayHeight - 1

        for (button in buttonList) {
            button.renderButton(mouseX, mouseY)
        }
    }

    /**
     * When someone clicks in your gui. Only use if you are changing how mouse Event is handled.
     */
    fun mouseClicked() {
        for (button in buttonList) {
            if (button.isMouseOver) {
                this.actionPreformed(button)
                break
            }
        }
    }

    /**
     * When someone clicks in your gui. This method is used as the callback for mouseEvent(). Recommended
     * if handling clicks.
     * @param button
     */
    open fun actionPreformed(button: SkyblockButton) {

    }

    /**
     * When the gui is closed, this method is called.
     */
    open fun onGuiClosed() {

    }

    protected fun clickSlotOnContainer(slotID: Int) {
        val guiChest = mc.currentScreen as GuiChest
        mc.playerController.windowClick(guiChest.inventorySlots.windowId, slotID, 0, 0, mc.thePlayer)
    }

}
