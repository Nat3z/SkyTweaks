package com.natia.secretmod.gui.pets;

import com.natia.secretmod.config.SkyTweaksConfig;
import com.natia.secretmod.gui.base.SkyblockButton;
import com.natia.secretmod.utils.ItemUtils;
import com.natia.secretmod.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

import java.awt.*;
import java.util.Collections;

public class Pet extends SkyblockButton {

    private boolean favorite;
    private ItemStack stack;
    private int level;
    public int slotNumber;

    public Pet(ItemStack stack, boolean favorite, int level, int slotNumber,
                int id, int x, int y) {
        super(id, x, y, 30, 30, "");
        this.slotNumber = slotNumber;
        this.favorite = favorite;
        this.stack = stack;
        this.level = level;
    }

    public ItemStack getPetItem() {
        return stack;
    }

    public int getLevel() {
        return level;
    }

    public boolean isFavorite() {
        return favorite;
    }

    @Override
    public void renderButton(int mouseX, int mouseY) {
        if (visible) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            if (hovered && SkyTweaksConfig.customPetsMenuShowPetLore) {
                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                RenderUtils.drawHoveringText(Collections.singletonList(stack.getDisplayName()),
                        0, 20,
                        sr.getScaledWidth(), sr.getScaledHeight(), -1, Minecraft.getMinecraft().fontRendererObj);
            }

            if (this.level >= 100)
                Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, new Color(145, 14, 0, 255).getRGB());
            else if (this.level >= 30)
                Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, new Color(145, 127, 16, 255).getRGB());
            else
                Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, new Color(103, 99, 101, 255).getRGB());

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            RenderUtils.renderItem(getPetItem(), xPosition + 3, yPosition + 3, 1.5f);
        }
    }
}
