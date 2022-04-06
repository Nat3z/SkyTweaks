package natia.skytweaks.core

import natia.skytweaks.utils.ItemDiff
import net.minecraftforge.fml.common.eventhandler.Event

class ItemPickupEvent(val itemDiff: ItemDiff) : Event() {
    @JvmName("getItemDiff1")
    fun getItemDiff(): ItemDiff {
        return itemDiff
    }
}
