package natia.skytweaks.utils

import natia.skytweaks.SecretUtils
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.util.BlockPos
import net.minecraft.util.StringUtils

class SkyblockMob(mobName: String, entity: Class<out Entity>) {

    var health: String? = null
        private set
    var entity: Entity? = null
        private set

    init {
        val entities = Minecraft.getMinecraft().theWorld.getEntities(entity) { e ->
            val playerPos = BlockPos(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ)
            assert(e != null)
            SecretUtils.withinRange(ENTITYRANGE, e!!.position, playerPos)
        }
        for (entity1 in entities) {
            for (e in Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand::class.java, entity1.entityBoundingBox.expand(15.0, 10.0, 15.0))) {
                if (StringUtils.stripControlCodes(e.name).contains(mobName)) {
                    health = e.name
                    this.entity = entity1
                    break
                }
            }
        }
    }

    fun foundMob(): Boolean {
        return health != null && entity != null
    }

    companion object {

        var ENTITYRANGE = BlockPos(30, 15, 30)
    }
}
