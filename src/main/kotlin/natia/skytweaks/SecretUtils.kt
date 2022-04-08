package natia.skytweaks

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Iterables
import com.google.common.collect.Lists
import com.google.common.collect.Multimap
import com.google.gson.JsonObject
import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.core.ItemPickupEvent
import natia.skytweaks.utils.*
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.item.ItemStack
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.tileentity.TileEntitySign
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge

import java.awt.*
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import java.util.regex.Pattern
import java.util.stream.Collectors

object SecretUtils {

    private var previousInventory: List<ItemStack>? = null
    private val SKYBLOCK_MENU_SLOT = 8
    var itemPickupLog: Multimap<String, ItemDiff>? = ArrayListMultimap.create<String, ItemDiff>()
    var bazaarCached = JsonObject()
    var CURRENT_SIGN: TileEntitySign? = null

    var preparedupate = false

    val isValid: Boolean
        get() {
            if (Minecraft.getMinecraft().currentServerData != null &&
                Minecraft.getMinecraft().currentServerData.serverIP != null &&
                Minecraft.getMinecraft().currentServerData.serverIP.contains("hypixel.net")) {
                if (Minecraft.getMinecraft().thePlayer.worldScoreboard.getObjectiveInDisplaySlot(1) == null) return false
                val sbTitle = EnumChatFormatting.getTextWithoutFormattingCodes(Minecraft.getMinecraft().thePlayer.worldScoreboard.getObjectiveInDisplaySlot(1).displayName)
                if (sbTitle.startsWith("SKYBLOCK")) {
                    return true
                }
            }
            return false
        }

    /**
     * Made By BiscuitDevelopment for SkyblockAddons
     * @author BiscuitDevelopment
     *
     * @param item
     * @return
     */
    private val PURSE_REGEX = Pattern.compile("(?:Purse|Piggy): (?<coins>[0-9.]*)(?: .*)?")
    val coins: Double
        get() {
            for (line in scoreboardLines) {
                val stripped = keepScoreboardCharacters(StringUtils.stripControlCodes(line))
                val matcher = PURSE_REGEX.matcher(stripped)
                if (matcher.matches()) {
                    try {
                        return java.lang.Double.parseDouble(matcher.group("coins"))
                    } catch (ignored: NumberFormatException) {
                        return 0.0
                    }

                }
            }
            return 0.0
        }
    /**
     * Made By BiscuitDevelopment for SkyblockAddons
     * @author BiscuitDevelopment
     *
     * @param item
     * @return
     */
    private val SCOREBOARD_CHARACTERS = Pattern.compile("[^a-z A-Z:0-9/'.]")

    val scoreboardLines: List<String>
        get() {
            val lines = ArrayList<String>()
            val scoreboard = Minecraft.getMinecraft().theWorld.scoreboard ?: return lines

            val objective = scoreboard.getObjectiveInDisplaySlot(1) ?: return lines

            var scores = scoreboard.getSortedScores(objective)

            val list = scores.stream()
                    .filter { input ->
                        input != null && input.playerName != null && !input.playerName
                                .startsWith("#")
                    }
                    .collect(Collectors.toList())

            if (list.size > 15)
                scores = Lists.newArrayList(Iterables.skip(list, scores.size - 15))
            else
                scores = list

            for (score in scores) {
                val team = scoreboard.getPlayersTeam(score.playerName)
                lines.add(ScorePlayerTeam.formatPlayerName(team, score.playerName))
            }

            return lines
        }

    val isInDungeons: Location
        get() {
            try {
                if (isValid) {
                    val scoreboard = scoreboardLines

                    for (s in scoreboard) {
                        val sCleaned = cleanSB(s)
                        if (sCleaned.contains("Dungeon Cleared"))
                            return Location.THE_CATACOMBS
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return Location.NONE
        }

    val generalFolder: File
        get() {
            val folder = File(Minecraft.getMinecraft().mcDataDir.toString() + "\\Skyblock Secret Mod\\")
            if (!folder.exists()) folder.mkdir()
            return folder
        }

    private val cachedStats = ArrayList<Double>()
    private val enchantsAdditivesEnderman = HashMap<String, Double>()
    private val enchantAdditives = HashMap<String, Double>()

    /**
     * Allows you to get the user's current hotbar.
     * @author NatiaDev
     * @return
     */
    val currentHotbar: List<ItemStack>
        get() {
            val stack = ArrayList<ItemStack>()
            for (i in 0..7) {
                stack.add(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i))
            }

            return stack
        }

    val isInHub: Boolean
        get() {
            try {
                if (SecretUtils.isValid) {
                    val scoreboard = SecretUtils.scoreboardLines

                    for (s in scoreboard) {
                        val sCleaned = SecretUtils.cleanSB(s)
                        if (sCleaned.contains("Village") || sCleaned.contains("None") || sCleaned.contains("Colosseum") || sCleaned.contains("Wizard") || sCleaned.contains("Coal") || sCleaned.contains("High Level") || sCleaned.contains("Auction House") || sCleaned.contains("Graveyard") || sCleaned.contains("Bazaar") || sCleaned.contains("Crypt") || sCleaned.contains("Forest") || sCleaned.contains("Wilderness") || sCleaned.contains("Mountain") || sCleaned.contains("Dark") || sCleaned.contains("Farm") || sCleaned.contains("Ruins") || sCleaned.contains("Blacksmith"))
                            return true
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return false
        }

    fun sendMessage(message: String) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(ChatComponentText(EnumChatFormatting.AQUA.toString() + "[SkyTweaks] " + EnumChatFormatting.YELLOW + message))
    }

    fun sendWarning(message: String) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(ChatComponentText(EnumChatFormatting.AQUA.toString() + "[SkyTweaks] " + EnumChatFormatting.RED + message))
    }

    fun updateBazaarCache() {
        WebUtils.fetch("https://api.hypixel.net/skyblock/bazaar") { res ->
            if (res.asJson().get("success").getAsBoolean()) {
                bazaarCached = res.asJson()
            }
        }
    }

    fun keepScoreboardCharacters(text: String): String {
        return SCOREBOARD_CHARACTERS.matcher(text).replaceAll("")
    }

    fun cleanSB(scoreboard: String): String {
        val nvString = StringUtils.stripControlCodes(scoreboard).toCharArray()
        val cleaned = StringBuilder()

        for (c in nvString) {
            if (c.toInt() > 20 && c.toInt() < 127)
                cleaned.append(c)
        }

        return cleaned.toString()
    }

    /**
     * Created by BiscuitDevelopement
     * https://github.com/BiscuitDevelopment/SkyblockAddons
     * MIT License
     */
    fun isNPC(entity: Entity): Boolean {
        if (entity !is EntityOtherPlayerMP) {
            return false
        }

        val entityLivingBase = entity as EntityLivingBase

        return entity.getUniqueID().version() == 2 && entityLivingBase.health == 20.0f && !entityLivingBase.isPlayerSleeping
    }

    fun playLoudSound(sound: String, pitch: Float) {
        Minecraft.getMinecraft().thePlayer.playSound(sound, 1f, pitch)
    }

    /**
     * Made by BiscuitDevelopemnt's SkyblockAddons
     * MIT License
     * @param currentInventory
     */
    fun getInventoryDiff(currentInventory: Array<ItemStack>) {
        val currentInv = Arrays.asList(*currentInventory)
        val pickuplist = arrayOfNulls<ItemStack>(36)
        val newInventory = copyInventory(currentInventory)
        val previousInventoryMap = HashMap<String, Int>()
        val newInventoryMap = HashMap<String, Int>()

        if (previousInventory != null) {

            for (i in newInventory.indices) {
                if (i == SKYBLOCK_MENU_SLOT) { // Skip the SkyBlock Menu slot all together (which includes the Quiver Arrow now)
                    continue
                }

                val previousItem = previousInventory!![i]
                val newItem = newInventory[i]

                if (previousItem != null) {
                    val amount = (previousInventoryMap as java.util.Map<String, Int>).getOrDefault(previousItem.displayName, 0) + previousItem.stackSize
                    previousInventoryMap[previousItem.displayName] = amount
                }

                if (newItem != null) {
                    if (newItem.displayName.contains(" " + EnumChatFormatting.DARK_GRAY + "x")) {
                        val newName = newItem.displayName.substring(0, newItem.displayName.lastIndexOf(" "))
                        newItem.setStackDisplayName(newName) // This is a workaround for merchants, it adds x64 or whatever to the end of the name.
                    }
                    val amount = (newInventoryMap as java.util.Map<String, Int>).getOrDefault(newItem.displayName, 0) + newItem.stackSize
                    newInventoryMap[newItem.displayName] = amount
                }
            }

            val inventoryDifference = LinkedList<ItemDiff>()
            val keySet = HashSet(previousInventoryMap.keys)
            keySet.addAll(newInventoryMap.keys)

            keySet.forEach { key ->
                val previousAmount = (previousInventoryMap as java.util.Map<String, Int>).getOrDefault(key, 0)
                val newAmount = (newInventoryMap as java.util.Map<String, Int>).getOrDefault(key, 0)
                val diff = newAmount - previousAmount
                if (diff > 0) {
                    inventoryDifference.add(ItemDiff(key, diff, Location.currentLocation))
                }
            }

            // Add changes to already logged changes of the same item, so it will increase/decrease the amount
            // instead of displaying the same item twice
            if (itemPickupLog != null)
                for (diff in inventoryDifference) {
                    val itemDiffs = itemPickupLog!!.get(diff.displayName)
                    if (itemDiffs.size <= 0) {
                        itemPickupLog!!.put(diff.displayName, diff)
                        MinecraftForge.EVENT_BUS.post(ItemPickupEvent(diff))
                    } else {
                        var added = false
                        for (loopDiff in itemDiffs) {
                            if (diff.amount < 0 && loopDiff.amount < 0 || diff.amount > 0 && loopDiff.amount > 0) {
                                loopDiff.add(diff.amount)
                                MinecraftForge.EVENT_BUS.post(ItemPickupEvent(loopDiff))
                                added = true
                            }
                        }
                        if (!added) {
                            itemPickupLog!!.put(diff.displayName, diff)
                            MinecraftForge.EVENT_BUS.post(ItemPickupEvent(diff))
                        }
                    }
                }

        }

        previousInventory = newInventory
    }

    /**
     * Made by Biscuit from SkyblockAddons
     * MIT License
     * @param inventory
     * @return
     */
    fun copyInventory(inventory: Array<ItemStack>): List<ItemStack> {
        val copy = ArrayList<ItemStack>(inventory.size)
        for (item in inventory) {
            copy.add(ItemStack.copyItemStack(item))
        }
        return copy
    }

    fun setPreviousInventory(previousInventory: List<ItemStack>) {
        SecretUtils.previousInventory = previousInventory
    }

    /**
     * @author NatiaDev
     * Gets all blocks in a certain block radius. (e.g. -100 5 10, -200 5 50)
     * @return blocklist
     */
    fun getBlocksInBox(world: World, pos1: BlockPos, pos2: BlockPos): Map<Block, BlockPos> {
        val blocks = HashMap<Block, BlockPos>()

        for (blockPos in BlockPos.getAllInBox(pos1, pos2)) {
            blocks[world.getBlockState(blockPos).block] = blockPos
        }

        return blocks
    }

    /**
     * Generates an Empty Runnable. Useful for SkyblockTimers
     * @author NatiaDev
     */
    fun generateEmptyUnit(): () -> Unit {
        return  {

        }
    }

    /**
     * @author NatiaDev
     * Simulates a title and sends it to client.
     * @param message
     * @param color
     */
    fun sendTitleCentered(message: String, color: Int) {

        val sr = ScaledResolution(Minecraft.getMinecraft())

        val height = sr.scaledHeight
        val width = sr.scaledWidth
        drawCenteredString(message, width / 2 / 3, (height * 0.450).toInt() / 3, color, 3.0)
    }

    fun withinRange(range: BlockPos, pos1: BlockPos, pos2: BlockPos): Boolean {
        return pos1.x >= pos2.x && pos1.x <= range.x &&
                pos1.z >= pos2.z && pos1.z <= range.z
    }

    /**
     * Allows you to bind a color to entities, blocks, whatever you want!
     * @author NatiaDev
     * @param color
     */
    fun bindColor(color: Color) {
        GlStateManager.color(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
    }

    /**
     * Draws a centered string on the screen based on text length
     * @author NatiaDev ext. Minecraft
     * @param text
     * @param x
     * @param y
     * @param color
     * @param scale
     */
    fun drawCenteredString(text: String, x: Int, y: Int, color: Int, scale: Double) {
        GlStateManager.pushMatrix()
        GlStateManager.scale(scale, scale, 1.0)
        Minecraft.getMinecraft().fontRendererObj.drawString(text,
                (x - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) / 2).toFloat(),
                y.toFloat(), color, true)
        GlStateManager.popMatrix()
    }

    fun calcArbitraryDamage(strength: Double, critDamage: Double, additiveMultipliers: Double, weaponDamage: Double): Double {
        return (5 + weaponDamage) * (1 + strength / 100) * (1 + critDamage / 100) * (1 + additiveMultipliers / 100)
    }

    fun addEnchantAdditives() {
        if (enchantsAdditivesEnderman.isEmpty()) {
            /* if someone can make this more optimized, please do :D */
            (enchantsAdditivesEnderman as java.util.Map<String, Double>).putIfAbsent("Ender Slayer I", 0.12)
            (enchantsAdditivesEnderman as java.util.Map<String, Double>).putIfAbsent("Ender Slayer II", 0.24)
            (enchantsAdditivesEnderman as java.util.Map<String, Double>).putIfAbsent("Ender Slayer III", 0.36)
            (enchantsAdditivesEnderman as java.util.Map<String, Double>).putIfAbsent("Ender Slayer IV", 0.48)
            (enchantsAdditivesEnderman as java.util.Map<String, Double>).putIfAbsent("Ender Slayer V", 0.60)
            (enchantsAdditivesEnderman as java.util.Map<String, Double>).putIfAbsent("Ender Slayer VI", 0.72)
            (enchantsAdditivesEnderman as java.util.Map<String, Double>).putIfAbsent("Ender Slayer VII", 0.84)

            (enchantAdditives as java.util.Map<String, Double>).putIfAbsent("First Strike I", 0.25)
            (enchantAdditives as java.util.Map<String, Double>).putIfAbsent("First Strike II", 0.5)
            (enchantAdditives as java.util.Map<String, Double>).putIfAbsent("First Strike III", 0.75)
            (enchantAdditives as java.util.Map<String, Double>).putIfAbsent("First Strike IV", 1.0)
            (enchantAdditives as java.util.Map<String, Double>).putIfAbsent("First Strike V", 1.25)

            (enchantAdditives as java.util.Map<String, Double>).putIfAbsent("Sharpness I", 0.05)
            (enchantAdditives as java.util.Map<String, Double>).putIfAbsent("Sharpness II", 0.1)
            (enchantAdditives as java.util.Map<String, Double>).putIfAbsent("Sharpness III", 0.15)
            (enchantAdditives as java.util.Map<String, Double>).putIfAbsent("Sharpness IV", 0.2)
            (enchantAdditives as java.util.Map<String, Double>).putIfAbsent("Sharpness V", 0.25)
            (enchantAdditives as java.util.Map<String, Double>).putIfAbsent("Sharpness VI", 0.3)
            (enchantAdditives as java.util.Map<String, Double>).putIfAbsent("Sharpness VII", 0.35)
        }
    }

    fun calcCurrentDamage(name: String, entityDamaged: Entity): Double {
        val mc = Minecraft.getMinecraft()
        if (cachedStats.isEmpty()) {
            val latest = AtomicReference("")
            WebUtils.fetch("https://sky.shiiyu.moe/api/v2/profile/$name") { res ->
                val profileData = res.asJson().getAsJsonObject("profiles").getAsJsonObject(latest.get()).get("data").getAsJsonObject()

                val critDamage = profileData.getAsJsonObject("stats").get("crit_damage").getAsDouble()
                val strength = profileData.getAsJsonObject("stats").get("strength").getAsDouble()
                val additive = profileData.getAsJsonObject("stats").get("damage_increase").getAsDouble()

                cachedStats.add(strength)
                cachedStats.add(critDamage)
                cachedStats.add(additive)
            }
        }
        var itemdamage = 0.0;
        var itemcritDamage = 0.0
        var itemStrength = 0.0
        val itemAdditive = doubleArrayOf(0.0)

        for (line1 in ItemUtils.getLore(mc.thePlayer.heldItem)) {
            val line = StringUtils.stripControlCodes(line1)

            if (line.startsWith("Damage:")) {
                itemdamage = try {
                    java.lang.Double.parseDouble(line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].replace("+", ""))
                } catch (ex: NumberFormatException) {
                    0.0
                }

            } else if (line.startsWith("Strength")) {
                try {
                    itemStrength = java.lang.Double.parseDouble(line.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1].replace("+", ""))
                } catch (ex: NumberFormatException) {
                    itemStrength = 0.0
                }

            } else if (line.startsWith("Crit Damage")) {
                try {
                    itemcritDamage = java.lang.Double.parseDouble(line.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2].replace("+", "").replace("%", ""))
                } catch (ex: NumberFormatException) {
                    itemcritDamage = 0.0
                }

            } else if (line.contains(",")) {

                val enchants = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (enchant1 in enchants) {
                    var enchant = enchant1
                    if (enchant.startsWith(" ")) enchant = enchant1.replaceFirst(" ".toRegex(), "")
                    val enchantFinal = StringUtils.stripControlCodes(enchant)

                    if (entityDamaged is EntityEnderman)
                        enchantsAdditivesEnderman.forEach { (theEnchant, additive) ->
                            if (enchantFinal.startsWith(theEnchant)) {
                                itemAdditive[0] += additive
                            }
                        }


                    enchantAdditives.forEach { (theEnchant, additive) ->
                        if (theEnchant.contains("First Strike") && SkyTweaksConfig.assumeFirstStrike) {
                            if (enchantFinal.startsWith(theEnchant)) {
                                itemAdditive[0] += additive
                            }
                        } else if (!theEnchant.contains("First Strike"))
                            if (enchantFinal.startsWith(theEnchant)) {
                                itemAdditive[0] += additive
                            }
                    }
                }
            }
        }

        if (StringUtils.stripControlCodes(mc.thePlayer.heldItem.displayName).contains("Fabled")) {
            itemAdditive[0] += 1.0
        }
        return calcArbitraryDamage(cachedStats[0] + itemStrength, cachedStats[1] + itemcritDamage, cachedStats[2] + itemAdditive[0], itemdamage)

    }

}
