package natia.skytweaks.features.waypoints

import net.minecraft.util.BlockPos

class Waypoint(val position: BlockPos, val name: String) {

    fun toShareable() = "@SkyTweaks-WP=${name}|[${position.x},${position.y},${position.z}]"
}