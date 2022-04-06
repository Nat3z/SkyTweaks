package natia.skytweaks.features.griffin

import natia.skytweaks.SecretUtils
import natia.skytweaks.SkyTweaks
import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.core.SkyblockTimer
import natia.skytweaks.utils.hypixel.Profiles
import net.minecraft.client.Minecraft
import net.minecraft.util.BlockPos
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Mouse

import java.util.*

class GriffinBurrowWaypoints {

    private val burrows = ArrayList<GriffinBurrow>()
    private val mc = Minecraft.getMinecraft()
    private var lastDugBurrow: BlockPos? = null

    internal var timer: SkyblockTimer? = null
    internal var setupTimer = false
    internal var fetchingBurrows = false

    fun worldLoad() {
        if (!SkyTweaksConfig.mythosWaypoints) return
        if (mc.theWorld == null || mc.thePlayer.worldScoreboard == null) return

        for (stack in SecretUtils.currentHotbar) {
            if (stack == null) continue
            if (StringUtils.stripControlCodes(stack!!.getDisplayName()).contains("Ancestral Spade")) {
                fetchBurrows()
                break
            }
        }
    }

    fun tick() {
        if (!SkyTweaksConfig.mythosWaypoints) return
        /*
        if (!setupTimer && mc.theWorld != null && !fetchingBurrows && SecretUtils.isInHub()) {
            setupTimer = true;
            timer = new SkyblockTimer(Stopwatch.createStarted(), EnumChatFormatting.GOLD + "Checking Mythos Waypoints", 30, false, () -> {
                setupTimer = false;
                fetchingBurrows = true;
                fetchBurrows();
            });
            TimersHook.addTimer(timer);
        }
        */
        if (Mouse.isButtonDown(mc.gameSettings.keyBindAttack.keyCode) || Mouse.isButtonDown(0)) {
            val pos = Minecraft.getMinecraft().objectMouseOver
            if (pos != null) {
                val bpos = pos.blockPos
                checkBurrow(bpos)
            }
        }
    }

    fun blockRender(event: DrawBlockHighlightEvent) {
        if (!SkyTweaksConfig.mythosWaypoints) return
        if (mc.theWorld == null) return

        if (!burrows.isEmpty() && SecretUtils.isValid && SecretUtils.isInHub) {
            try {
                burrows.forEach { griffinBurrow ->
                    if (!destroyedBurrows.contains(griffinBurrow.blockPos))
                        griffinBurrow.drawWaypoint(event.partialTicks)
                }
            } catch (ignored: ConcurrentModificationException) {

            }

        }
    }

    fun chat(event: ClientChatReceivedEvent) {
        if (!SkyTweaksConfig.mythosWaypoints) return
        val message = event.message.unformattedText

        if (message.contains("You dug out a Griffin Burrow") || message.contains("You finished the Griffin burrow chain!")) {
            if (lastDugBurrow != null) {
                destroyedBurrows.add(lastDugBurrow!!)
                burrows.removeIf { burrow -> burrow.blockPos == lastDugBurrow }
                lastDugBurrow = null
            }
        }
    }

    private fun checkBurrow(pos: BlockPos?) {
        if (!SkyTweaksConfig.mythosWaypoints) return
        if (mc.thePlayer == null || mc.theWorld == null) return
        if (pos == null) return

        val item = mc.thePlayer.heldItem
        val blockstate = mc.theWorld.getBlockState(pos)

        if (item != null && blockstate != null) {
            if (StringUtils.stripControlCodes(item.displayName).contains("Ancestral Spade")) {
                if (burrows.stream().anyMatch { burrow -> burrow.blockPos == pos }) {
                    lastDugBurrow = pos
                }
            }
        }
    }

    private fun fetchBurrows() {
        SecretUtils.sendMessage("Fetching Griffin Burrows....")
        Thread {
            SkyTweaks.LOGGER.info("Griffin Burrow Waypoints Setup:")
            val json = Profiles.getProfile(Profiles.getLatestProfile(mc.thePlayer.uniqueID.toString().replace("-", "")))
            SkyTweaks.LOGGER.info("- Established connection to the Hypixel API")
            val array = json.get("profile").asJsonObject.get("members")
                    .asJsonObject.get(mc.thePlayer.uniqueID.toString().replace("-", "")).asJsonObject
                    .get("griffin").asJsonObject.get("burrows").asJsonArray
            SkyTweaks.LOGGER.info("- Got Griffin Burrows.")
            if (array.size() <= 0) {
                SecretUtils.sendMessage("No Griffin Burrows were fetched from the API.")
            }
            SkyTweaks.LOGGER.info("- Scanning burrows..")
            destroyedBurrows.clear()
            burrows.clear()
            array.forEach { element ->
                val burrow = element.asJsonObject
                val x = burrow.get("x").asInt
                val y = burrow.get("y").asInt
                val z = burrow.get("z").asInt
                val type = burrow.get("type").asInt
                val tier = burrow.get("tier").asInt
                val chain = burrow.get("chain").asInt
                val griffinBurrow = GriffinBurrow(x, y, z, type, tier, chain)
                SkyTweaks.LOGGER.info("- Scanned and received burrow " + griffinBurrow.blockPos)

                if (!burrows.contains(griffinBurrow) && !destroyedBurrows.contains(griffinBurrow.blockPos)) {
                    SkyTweaks.LOGGER.info("- Added burrow " + griffinBurrow.blockPos)
                    burrows.add(griffinBurrow)
                }
            }

            if (burrows.isEmpty()) {
                SecretUtils.sendMessage("No Burrows Were Found! Try and switch lobbies and we'll try to catch on automagically!")
            } else {
                SecretUtils.sendMessage(burrows.size.toString() + " Burrows registered!")
            }

            SkyTweaks.LOGGER.info("Completed Burrow Fetching!")
            fetchingBurrows = false

            if (burrows.isNotEmpty() && SkyTweaksConfig.mythosWaypointOptimize) {
                val lastChain = burrows[0].chain
                var alreadyWarned = false
                val lastBurrows = ArrayList(burrows)
                for (burrow in burrows) {
                    if (burrow.chain != lastChain) {
                        alreadyWarned = true
                        if (burrow.chain == 1) {
                            alreadyWarned = false
                            SecretUtils.sendWarning("Detected that this is an uneven Griffin Burrow chain. Since the chain is already on phase 1, no actions will be preformed.")
                        } else {
                            SecretUtils.sendWarning("Detected that this is an uneven Griffin Burrow chain. To keep efficiency up, we have only highlighted even chains so you can finish those.")
                        }
                        break
                    }
                }

                if (alreadyWarned) {
                    burrows.clear()
                    for (burrow in lastBurrows) {
                        if (lastChain == burrow.chain) {
                            /* even burrow */
                            burrows.add(burrow)
                        }
                    }
                }
            }
        }.start()
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onDamageEvent(event: PlayerInteractEvent) {
        if (!SkyTweaksConfig.mythosWaypoints) return
        checkBurrow(event.pos)
    }

    companion object {
        var destroyedBurrows = ArrayList<BlockPos>()

        val instance = GriffinBurrowWaypoints()
    }
}

