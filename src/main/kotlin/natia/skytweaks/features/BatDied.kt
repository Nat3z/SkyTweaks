package natia.skytweaks.features

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.SecretUtils
import natia.skytweaks.utils.Location
import net.minecraft.client.Minecraft
import net.minecraft.entity.passive.EntityBat
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class BatDied {

    private var mc: Minecraft = Minecraft.getMinecraft()

    @SubscribeEvent
    fun onEntityDeath(event: LivingDeathEvent) {
        if (mc.theWorld == null) return
        if (!SkyTweaksConfig.batdead) return
        if (!SecretUtils.isValid) return
        if (SecretUtils.isInDungeons != Location.THE_CATACOMBS) return
        if (event.entity !is EntityBat) return

        SecretUtils.sendWarning("A bat was killed in your dungeon!")
    }

    companion object {
        val instance: BatDied = BatDied()
    }


}