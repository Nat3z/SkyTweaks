package natia.skytweaks.features.slayers

import com.google.common.base.Stopwatch
import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.utils.AsyncAwait
import natia.skytweaks.utils.Location
import natia.skytweaks.utils.RenderUtils
import natia.skytweaks.SecretUtils
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.util.*
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.util.Constants

import javax.vecmath.Vector3f
import java.awt.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class VoidGloom {

    internal var mc = Minecraft.getMinecraft()

    private val range = BlockPos(30, 0, 30)

    private var sendTitle = false
    private var title = ""

    var beaconDown = false
    var holdingBeacon = false
    var doingVoidgloom = false

    private var yangGlyphsSpawned = 0

    private val titleWatch = Stopwatch.createUnstarted()

    private val checkIfVoidgloomNearby = Stopwatch.createUnstarted()

    fun blockRender(event: RenderWorldLastEvent) {
        val playerPos = BlockPos(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ)
        val world = Minecraft.getMinecraft().theWorld ?: return
        if (!SkyTweaksConfig.seraphHelper) return
        /* is in void sepulture */
        val foundBeacon = AtomicBoolean(false)
        if (SkyTweaksConfig.forceSeraph || Location.currentLocation === Location.VOID_SEPULTURE || Location.currentLocation === Location.THE_END) {
            /* check voidglooms */
            for (e in world.getEntitiesWithinAABB(Entity::class.java, mc.thePlayer.entityBoundingBox.expand(15.0, 10.0, 15.0))) {
                val name = StringUtils.stripControlCodes(e.name)
                if (name.contains("Voidgloom Seraph")) {
                    /* formatted slayer health */
                    slayerHealth = e.name
                    doingVoidgloom = true
                }
                /* within reasonable distance of player */
                if (e is EntityEnderman) {
                    if (e.heldBlockState.block != null && e.heldBlockState.block == Blocks.beacon) {
                        holdingBeacon = true
                    }
                }

            }

            if (this.doingVoidgloom) {
                /*
                * Taken From HyAddons under GNU General Public License v3.0
                * author: jxxe
                */
                yangGlyphsSpawned = 0
                if (SkyTweaksConfig.skullHighlights) {
                    for (e in world.getEntitiesWithinAABB(EntityArmorStand::class.java, mc.thePlayer.entityBoundingBox.expand(30.0, 10.0, 30.0))) {
                        if (e.getEquipmentInSlot(4) != null) {
                            val item = e.getEquipmentInSlot(4)
                            if (item.item === Items.skull) {
                                val nbt = item.tagCompound
                                if (nbt != null && nbt.hasKey("SkullOwner")) {
                                    var texture = nbt.getCompoundTag("SkullOwner").getCompoundTag("Properties").getTagList("textures", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0).getString("Value")
                                    texture = String(Base64.getDecoder().decode(texture))
                                    if (texture.contains("eb07594e2df273921a77c101d0bfdfa1115abed5b9b2029eb496ceba9bdbb4b3")) {
                                        yangGlyphsSpawned++
//                                        RenderUtils.drawOutlinedHitbox(e.entityBoundingBox, SkyTweaksConfig.skullHighlightColor, event.partialTicks)
                                        val vec = Vector3f(e.position.x.toFloat(), e.position.y + 1f, e.position.z.toFloat())
                                        RenderUtils.highlightBlock(vec, 0.5f, event.partialTicks, Color(SkyTweaksConfig.skullHighlightColor))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            /*
             * everything under here is by me (NatiaDev)
             */

            SecretUtils.getBlocksInBox(world,
                    BlockPos(playerPos.x + 15, playerPos.y + 10, playerPos.z + 15),
                    BlockPos(playerPos.x - 15, playerPos.y - 10, playerPos.z - 15))

                    .forEach { (block, pos) ->
                        if (block == Blocks.beacon) {
                            if (SkyTweaksConfig.yangGlyphHighlights) {
                                beaconDown = true
                                foundBeacon.set(true)
                                holdingBeacon = false

                                if (!sendTitle && (SkyTweaksConfig.yangGlyphHighlightType.equals("Both") || SkyTweaksConfig.yangGlyphHighlightType.equals("Notification Only"))) {
                                    title = "Yang Glyph!"
                                    sendTitle = true
                                }
                                /* checks beacon highlight type */
                                if (SkyTweaksConfig.yangGlyphHighlightType.equals("Both") || SkyTweaksConfig.yangGlyphHighlightType.equals("Highlight Only")) {
                                    val vec = Vector3f(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
                                    RenderUtils.showBeam(vec, Color(SkyTweaksConfig.yangGlyphHighlightColor), event.partialTicks)
                                    RenderUtils.highlightBlock(vec, 0.5f, event.partialTicks, Color(SkyTweaksConfig.yangGlyphHighlightColor))
                                }
                            }
                        }
                    }

            if (!foundBeacon.get() && beaconDown)
                beaconDown = false
        }
    }

    fun render() {
        if (mc.currentScreen != null) return
        if (!SkyTweaksConfig.seraphHelper) return
        if (slayerHealth != "") {
            if (StringUtils.stripControlCodes(slayerHealth).endsWith(" 0❤")) {
                slayerHealth = ""
                return
            }
            val hudElement = SkyTweaksConfig.voidgloomHUD
            /* boss health */
            mc.fontRendererObj.drawString(EnumChatFormatting.BOLD.toString() + "BOSS: ", hudElement.x.toFloat(), hudElement.y.toFloat(), Color.white.rgb, true)
            mc.fontRendererObj.drawString(slayerHealth, (hudElement.x + 40).toFloat(), hudElement.y.toFloat(), Color.white.rgb, true)
            /* beacon down */
            mc.fontRendererObj.drawString(EnumChatFormatting.DARK_AQUA.toString() + "" + EnumChatFormatting.BOLD + "BEACON DOWN: ", hudElement.x.toFloat(), (hudElement.y + 10).toFloat(), Color.white.rgb, true)
            mc.fontRendererObj.drawString(if (beaconDown) EnumChatFormatting.GREEN.toString() + "YES" else EnumChatFormatting.RED.toString() + "NO", (hudElement.x + 110).toFloat(), (hudElement.y + 10).toFloat(), Color.white.rgb, true)
            /* holding beacon */
            mc.fontRendererObj.drawString(EnumChatFormatting.DARK_AQUA.toString() + "" + EnumChatFormatting.BOLD + "BEACON HELD: ", hudElement.x.toFloat(), (hudElement.y + 20).toFloat(), Color.white.rgb, true)
            mc.fontRendererObj.drawString(if (holdingBeacon) EnumChatFormatting.GREEN.toString() + "YES" else EnumChatFormatting.RED.toString() + "NO", (hudElement.x + 110).toFloat(), (hudElement.y + 20).toFloat(), Color.white.rgb, true)
            /* yang glyphs spawned */
            mc.fontRendererObj.drawString(EnumChatFormatting.DARK_PURPLE.toString() + "" + EnumChatFormatting.BOLD + "SKULLS NEARBY: ", hudElement.x.toFloat(), (hudElement.y + 30).toFloat(), Color.white.rgb, true)
            mc.fontRendererObj.drawString(if (yangGlyphsSpawned > 0) EnumChatFormatting.GREEN.toString() + "YES" else EnumChatFormatting.RED.toString() + "NO", (hudElement.x + 110).toFloat(), (hudElement.y + 30).toFloat(), Color.white.rgb, true)
            /* is in hit phase */
            val unformattedSlayer = StringUtils.stripControlCodes(slayerHealth)
            mc.fontRendererObj.drawString(EnumChatFormatting.RED.toString() + "" + EnumChatFormatting.BOLD + "HIT PHASE: ", hudElement.x.toFloat(), (hudElement.y + 40).toFloat(), Color.white.rgb, true)
            mc.fontRendererObj.drawString(if (unformattedSlayer.contains("Hits")) EnumChatFormatting.GREEN.toString() + "YES" else EnumChatFormatting.RED.toString() + "NO", (hudElement.x + 110).toFloat(), (hudElement.y + 40).toFloat(), Color.white.rgb, true)
        }

        if (sendTitle && !titleWatch.isRunning) {
            titleWatch.start()
        }

        if (titleWatch.isRunning && titleWatch.elapsed(TimeUnit.SECONDS) <= 2) {
            SecretUtils.sendTitleCentered(title, Color.red.rgb)
        } else if (sendTitle) {
            sendTitle = false
            titleWatch.reset()
            title = ""
        }
    }

    fun tick() {
        if (mc.theWorld == null) return

        if (!SkyTweaksConfig.seraphHelper) return

        if (Location.currentLocation === Location.VOID_SEPULTURE || Location.currentLocation === Location.THE_END) {
            AsyncAwait.until({
                var foundVoidgloom = false
                for (entity in mc.theWorld.getEntitiesWithinAABB(EntityArmorStand::class.java, mc.thePlayer.entityBoundingBox.expand(30.0, 10.0, 30.0))) {
                    val name = StringUtils.stripControlCodes(entity.name)
                    if (name.contains("Voidgloom Seraph")) {
                        foundVoidgloom = true
                        break
                    }
                }

                /* did not find nearby voidgloom. reset vgloom util variables. */
                if (!foundVoidgloom) {
                    sendTitle = false
                    slayerHealth = ""
                    yangGlyphsSpawned = 0
                    doingVoidgloom = false
                    holdingBeacon = false
                    beaconDown = false
                }
            }, 6, checkIfVoidgloomNearby)
        }
    }

    fun worldLoad() {
        if (titleWatch.isRunning) titleWatch.reset()
        title = ""
        slayerHealth = ""
        if (checkIfVoidgloomNearby.isRunning) checkIfVoidgloomNearby.stop()
        yangGlyphsSpawned = 0

        sendTitle = false
        doingVoidgloom = false
        holdingBeacon = false
        beaconDown = false
    }

    companion object {

        val instance = VoidGloom()

        var slayerHealth = ""
    }


}
