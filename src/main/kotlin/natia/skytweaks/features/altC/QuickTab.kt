package natia.skytweaks.features.altC

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.SecretUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard

import java.awt.*
import java.util.ArrayList

class QuickTab {

    private var showUI = false
    private var selected = 0
    private val mc = Minecraft.getMinecraft()

    internal var tabItems: MutableList<QuickTabItem> = ArrayList()

    init {
        tabItems.add(QuickTabItem(
                "Storage",
                "Access your Storage.",
                "/storage",
                ResourceLocation("secretmod", "tabitems/storage.png"))
        )

        tabItems.add(QuickTabItem(
                "Ender Chest",
                "Access your Ender Chest.",
                "/ec",
                ResourceLocation("secretmod", "tabitems/ender_chest.png"))
        )

        tabItems.add(QuickTabItem(
                "Warp Scrolls",
                "Access warp menu.",
                "/warp",
                ResourceLocation("secretmod", "tabitems/travelscroll.png"))
        )

        tabItems.add(QuickTabItem(
                "Wardrobe",
                "Access the Skyblock Wardrobe.",
                "/wardrobe",
                ResourceLocation("secretmod", "tabitems/wardrobe.png"))
        )

        tabItems.add(QuickTabItem(
                "Pets Menu",
                "Access the Pets Menu.",
                "/pets",
                ResourceLocation("secretmod", "tabitems/pets_menu.png"))
        )

        tabItems.add(QuickTabItem(
                "Bazaar",
                "Access the Bazaar.",
                "/bz",
                ResourceLocation("secretmod", "tabitems/bazaar.png"))
        )

        tabItems.add(QuickTabItem(
                "Auction House",
                "Access the Auction House.",
                "/ah",
                ResourceLocation("secretmod", "tabitems/auction_house.png"))
        )
    }

    @SubscribeEvent
    fun render(event: TickEvent.RenderTickEvent) {
        if (!SkyTweaksConfig.allowALTC) return
        if (mc.currentScreen != null) return

        if (showUI && !Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
            showUI = false
            mc.thePlayer.sendChatMessage(tabItems[selected].commandExecuted)
            selected = 0
        }

        /* Alt C UI */
        if (showUI) {
            val sr = ScaledResolution(mc)

            val width = 400
            val height = 100

            val uiX = (sr.scaledWidth - width) / 2
            val uiY = (sr.scaledHeight - height) / 2
            /* selects */
            val textureWidth = 45
            val textureHeight = 45
            var itemX = -20
            Gui.drawRect(uiX, uiY, uiX + width, uiY + height, Color(18, 18, 18, 219).rgb)
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            var args = arrayOfNulls<Any>(5)
            var itemSelected: QuickTabItem? = null
            for (item in tabItems) {
                itemX += 50
                mc.textureManager.bindTexture(item.texture)
                Gui.drawModalRectWithCustomSizedTexture(uiX + itemX, uiY + 30, 0f, 0f, textureWidth, textureHeight, textureWidth.toFloat(), textureHeight.toFloat())

                /* current tab item is this tab Item. */
                if (selected == tabItems.indexOf(item)) {
                    itemSelected = item
                    args = arrayOf<Any?>(uiX + itemX, uiY + 30, uiX + itemX + textureWidth, uiY + 30 + textureHeight, Color(252, 255, 0, 114).rgb)
                }
            }
            assert(itemSelected != null)
            Gui.drawRect(args[0] as Int, args[1] as Int, args[2] as Int, args[3] as Int, args[4] as Int)
            SecretUtils.drawCenteredString(itemSelected!!.name, (uiX + 200) / 2, (uiY + 10) / 2, Color(10, 192, 0).rgb, 2.0)
            SecretUtils.drawCenteredString(itemSelected.description, uiX + 200, uiY + height - 20, Color(10, 232, 0).rgb, 1.0)
        }
    }

    @SubscribeEvent
    fun key(event: InputEvent.KeyInputEvent) {
        if (!SkyTweaksConfig.allowALTC) return
        if (mc.currentScreen != null) return
        if (!Keyboard.getEventKeyState()) return
        if (!Keyboard.isKeyDown(Keyboard.KEY_LMENU)) return

        if (Keyboard.getEventKey() == Keyboard.KEY_C) {
            if (showUI) {
                if (selected < tabItems.size - 1)
                    selected++
                else
                    selected = 0
            } else
                showUI = true
        }
    }

}
