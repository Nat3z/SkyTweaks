package natia.skytweaks.features.dungeons

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.SecretUtils
import natia.skytweaks.utils.Location
import natia.skytweaks.utils.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.passive.EntityBat
import java.awt.Color

class BatPointer {

    val mc = Minecraft.getMinecraft()
    var closestBat: EntityBat? = null
    fun tick() {
        if (Location.currentLocation != Location.THE_CATACOMBS) return
        val world = mc.theWorld
        for (e in world.getEntitiesWithinAABB(EntityBat::class.java, mc.thePlayer.entityBoundingBox.expand(25.0, 10.0, 25.0))) {
            closestBat = e
            break
        }
    }

    fun render() {
        if (closestBat == null) return
        if (!closestBat?.isEntityAlive!!) {
            closestBat = null
            return
        }

        RenderUtils.drawPointers(closestBat?.positionVector!!, Color(SkyTweaksConfig.pointBatsColor))
    }

    fun worldLoad() {
        closestBat = null
    }

    companion object {
        val instance = BatPointer()
    }
}