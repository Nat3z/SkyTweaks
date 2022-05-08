package natia.skytweaks.gui.bazaar

import natia.skytweaks.gui.base.SkyblockGui
import net.minecraft.client.Minecraft
import net.minecraft.inventory.ContainerChest

class BazaarOverlay : SkyblockGui() {
    val mc = Minecraft.getMinecraft()

    init {
//        this.buttonList.add(BazaarPopup())
//        this.buttonList.add(BazaarLeaderboard())
    }
    override fun render(container: ContainerChest) {
        super.render(container)
    }
}