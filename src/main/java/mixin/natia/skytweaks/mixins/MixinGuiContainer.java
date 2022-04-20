package mixin.natia.skytweaks.mixins;

import natia.skytweaks.gui.GuiHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer {

    @Shadow public Container inventorySlots;
    boolean checkedChest = false;
    final Minecraft mc = Minecraft.getMinecraft();
    @Inject(method = "drawScreen", at = @At("RETURN"), cancellable = true)
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        /*if (checkedChest) return;

        if (mc.currentScreen instanceof GuiChest) {
            final List<ItemStack> itemStacks = this.inventorySlots.inventoryItemStacks;
            GuiChest guiChest = (GuiChest) mc.currentScreen;

            ContainerChest container = (ContainerChest) guiChest.inventorySlots;
            String name = StringUtils.stripControlCodes(container.getLowerChestInventory().getName());

            if (name.equalsIgnoreCase("Chest")) {
                itemStacks.forEach(itemStack -> {
                    if (itemStack != null && itemStack.getDisplayName() != null) {

                    }
                });

                checkedChest = true;
            }
        }*/
    }
    @Inject(method = "drawSlot", at = @At("RETURN"))
    public void drawSlot_RETURN(Slot slotIn, CallbackInfo ci) {
        if (inventorySlots instanceof ContainerChest) {
            GuiHook.Companion.getTradeGui().drawSlots((ContainerChest) inventorySlots);
        }
    }
}
