package com.natia.secretmod.core;

import com.natia.secretmod.utils.ItemDiff;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ItemPickupEvent extends Event {
    private ItemDiff stack;
    public ItemPickupEvent(ItemDiff stack) {
        this.stack = stack;
    }

    public ItemDiff getItemDiff() {
        return stack;
    }
}
