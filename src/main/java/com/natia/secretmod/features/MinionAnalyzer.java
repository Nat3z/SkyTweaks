package com.natia.secretmod.features;

import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SecretModConfig;
import com.natia.secretmod.utils.ItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MinionAnalyzer {
    Minecraft mc = Minecraft.getMinecraft();
    private double moneyInMinion = 0;

    List<Integer> invalidSlots = new ArrayList<>();
    public MinionAnalyzer() {
        invalidSlots.add(3);
        invalidSlots.add(4);
        invalidSlots.add(5);
        invalidSlots.add(10);
        invalidSlots.add(19);
        invalidSlots.add(28);
        invalidSlots.add(37);
        invalidSlots.add(46);
        invalidSlots.add(48);
        invalidSlots.add(50);
        invalidSlots.add(53);
    }

    private boolean executedThisGui = false;

    @SubscribeEvent
    public void onGuiOpen(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!SecretModConfig.minionAnalyzer) return;
        if (mc.theWorld == null) return;
        if (!SecretUtils.isValid()) return;
        if (executedThisGui) return;
        moneyInMinion = 0;
        if (event.gui instanceof GuiChest) {
            GuiChest guiChest = (GuiChest) event.gui;
            ContainerChest container = (ContainerChest) guiChest.inventorySlots;
            IInventory inventory = container.getLowerChestInventory();
            executedThisGui = true;
            if (inventory.getDisplayName().getUnformattedText().contains(" Minion")) {
                for (Slot slot : container.inventorySlots) {
                    if (slot == null) continue;
                    if (slot.getStack() == null) continue;
                    if (invalidSlots.contains(slot.slotNumber)) continue;
                    if (slot.slotNumber > 53) break;

                    String type = ItemUtils.getItemType(slot.getStack());
                    if (type != null && SecretUtils.bazaarCached.get("products").getAsJsonObject().has(type)) {
                        double coins = SecretUtils.bazaarCached.get("products").getAsJsonObject()
                                .get(type).getAsJsonObject().get("buy_summary").getAsJsonArray().get(0)
                                .getAsJsonObject().get("pricePerUnit").getAsDouble();
                        moneyInMinion += coins * slot.getStack().stackSize;
                    }
                }
                moneyInMinion = Math.ceil(moneyInMinion);
            }
        }
    }

    @SubscribeEvent
    public void onClose(GuiOpenEvent event) {
        if (event.gui == null) {
            executedThisGui = false;
        }
    }

    @SubscribeEvent
    public void onRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!SecretModConfig.minionAnalyzer) return;
        if (moneyInMinion == 0) return;

        if (event.gui instanceof GuiChest) {
            ScaledResolution sr = new ScaledResolution(mc);
            int guiLeft = (sr.getScaledWidth() - 176) / 2;
            int guiTop = (sr.getScaledHeight() - 222) / 2;

            int x = guiLeft + 85;
            int y = guiTop + (int)6.6;
            DecimalFormat myFormatter = new DecimalFormat("###,###,###");
            String output = myFormatter.format(moneyInMinion);
            GL11.glTranslated(0, 0, 1);
            if (moneyInMinion != -1)
                Minecraft.getMinecraft().fontRendererObj.drawString("Minion Value: +" + output, x, y, Color.GREEN.getRGB(), true);
            else
                Minecraft.getMinecraft().fontRendererObj.drawString("Unable to get Minion Value.", x, y, Color.GRAY.getRGB(), true);
            GL11.glTranslated(0, 0, -1);
        }
    }

}
