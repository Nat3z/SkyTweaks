package natia.skytweaks.features

import com.google.common.base.Stopwatch
import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.SecretUtils
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

import java.awt.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class DamagePerSecond {
    internal var mc = Minecraft.getMinecraft()
    internal var damagesDealt: MutableList<Double> = ArrayList()

    internal var damageDetecter = Stopwatch.createUnstarted()

    @SubscribeEvent
    fun onDamageEvent(event: LivingAttackEvent) {
        if (!SkyTweaksConfig.damageMeter) return
        /* damaged by player */
        if (event.source == null) return
        if (event.source.entity == null) return

        if (event.source.entity.isEntityEqual(mc.thePlayer)) {
            Thread {
                val approxDamageDealt = Math.round(SecretUtils.calcCurrentDamage(mc.thePlayer.name, event.entity)).toDouble()
                damagesDealt.add(approxDamageDealt)
            }.start()
        }
    }

    fun render() {
        if (!SkyTweaksConfig.damageMeter) return
        val element = SkyTweaksConfig.damageMeterHud
        val averageDamageDealt = doubleArrayOf(0.0)
        damagesDealt.forEach { damagesDealt -> averageDamageDealt[0] += damagesDealt }
        /* prevent dividing by 0 */
        if (averageDamageDealt[0] == 0.0) return
        /* Reset damage every 3s */
        if (!damageDetecter.isRunning) damageDetecter.start()
        if (damageDetecter.elapsed(TimeUnit.SECONDS) >= 3) {
            damageDetecter.reset()
            damagesDealt.clear()
        } else {
            averageDamageDealt[0] = averageDamageDealt[0] / damagesDealt.size
            mc.fontRendererObj.drawString(EnumChatFormatting.RED.toString() + "Damage per Second:" + EnumChatFormatting.DARK_RED + " ~" + averageDamageDealt[0], element.x.toFloat(), element.y.toFloat(), Color.WHITE.rgb, true)
        }
    }

    companion object {

        val instance = DamagePerSecond()
    }
}
