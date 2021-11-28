package com.natia.secretmod.features.altC;

import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SkyTweaksConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuickTab {

    private boolean showUI = false;
    private int selected = 0;
    private Minecraft mc = Minecraft.getMinecraft();

    List<QuickTabItem> tabItems = new ArrayList<>();

    public QuickTab() {
        tabItems.add(new QuickTabItem(
                "Storage",
                "Access your Storage.",
                "/storage",
                new ResourceLocation("secretmod", "tabitems/storage.png"))
        );

        tabItems.add(new QuickTabItem(
                "Ender Chest",
                "Access your Ender Chest.",
                "/ec",
                new ResourceLocation("secretmod", "tabitems/ender_chest.png"))
        );

        tabItems.add(new QuickTabItem(
                "Warp Scrolls",
                "Access warp menu.",
                "/warp",
                new ResourceLocation("secretmod", "tabitems/travelscroll.png"))
        );

        tabItems.add(new QuickTabItem(
                "Wardrobe",
                "Access the Skyblock Wardrobe.",
                "/wardrobe",
                new ResourceLocation("secretmod", "tabitems/wardrobe.png"))
        );

        tabItems.add(new QuickTabItem(
                "Pets Menu",
                "Access the Pets Menu.",
                "/pets",
                new ResourceLocation("secretmod", "tabitems/pets_menu.png"))
        );

        tabItems.add(new QuickTabItem(
                "Bazaar",
                "Access the Bazaar.",
                "/bz",
                new ResourceLocation("secretmod", "tabitems/bazaar.png"))
        );

        tabItems.add(new QuickTabItem(
                "Auction House",
                "Access the Auction House.",
                "/ah",
                new ResourceLocation("secretmod", "tabitems/auction_house.png"))
        );
    }

    @SubscribeEvent
    public void render(TickEvent.RenderTickEvent event) {
        if (!SkyTweaksConfig.allowALTC) return;
        if (mc.currentScreen != null) return;

        if (showUI && !Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
            showUI = false;
            mc.thePlayer.sendChatMessage(tabItems.get(selected).commandExecuted);
            selected = 0;
        }

        /* Alt C UI */
        if (showUI) {
            ScaledResolution sr = new ScaledResolution(mc);

            int width = 400;
            int height = 100;

            int uiX = (sr.getScaledWidth() - width) / 2;
            int uiY = (sr.getScaledHeight() - height) / 2;
            /* selects */
            int textureWidth = 45;
            int textureHeight = 45;
            int itemX = -20;
            Gui.drawRect(uiX, uiY, uiX + width, uiY + height, new Color(18, 18, 18, 219).getRGB());
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            Object[] args = new Object[5];
            QuickTabItem itemSelected = null;
            for (QuickTabItem item : tabItems) {
                itemX += 50;
                mc.getTextureManager().bindTexture(item.texture);
                Gui.drawModalRectWithCustomSizedTexture(uiX + itemX, uiY + 30, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);

                /* current tab item is this tab Item. */
                if (selected == tabItems.indexOf(item)) {
                    itemSelected = item;
                    args = new Object[]{uiX + itemX, uiY + 30, (uiX + itemX) + textureWidth, (uiY + 30) + textureHeight, new Color(252, 255, 0, 114).getRGB()};
                }
            }
            assert itemSelected != null;
            Gui.drawRect((int) args[0], (int) args[1], (int) args[2], (int) args[3], (int) args[4]);
            SecretUtils.drawCenteredString(itemSelected.name, ((uiX + 200)) / 2, (uiY + 10) / 2, new Color(10, 192, 0).getRGB(), 2f);
            SecretUtils.drawCenteredString(itemSelected.description, ((uiX + 200)), (uiY + height) - 20, new Color(10, 232, 0).getRGB(), 1f);
        }
    }

    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent event) {
        if (!SkyTweaksConfig.allowALTC) return;
        if (mc.currentScreen != null) return;
        if (!Keyboard.getEventKeyState()) return;
        if (!Keyboard.isKeyDown(Keyboard.KEY_LMENU)) return;

        if (Keyboard.getEventKey() == Keyboard.KEY_C) {
            if (showUI) {
                if (selected < tabItems.size() - 1)
                    selected++;
                else
                    selected = 0;
            }
            else
                showUI = true;
        }
    }

}
