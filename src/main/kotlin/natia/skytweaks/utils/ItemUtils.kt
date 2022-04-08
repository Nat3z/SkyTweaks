package natia.skytweaks.utils

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.StringUtils
import net.minecraftforge.common.util.Constants

import java.util.ArrayList
import java.util.Collections
import java.util.regex.Matcher
import java.util.regex.Pattern

object ItemUtils {

    /*
     * Taken from BiscuitDevelopment's SkyblockAddons under MIT License.
     * https://github.com/BiscuitDevelopment/SkyblockAddons/
     */
    private val RARITY_PATTERN = Pattern.compile("(§[0-9a-f]§l§ka§r )?([§0-9a-fk-or]+)(?<rarity>[A-Z]+)")

    /*
     * Taken from BiscuitDevelopment's SkyblockAddons under MIT License.
     * https://github.com/BiscuitDevelopment/SkyblockAddons/
     *
     * Gets the Item's Rarity through Regex.
     */
    fun getRarity(item: ItemStack?): ItemRarity? {
        if (item == null || !item.hasTagCompound()) {
            return null
        }

        val display = item.getSubCompound("display", false)

        if (display == null || !display.hasKey("Lore")) {
            return null
        }

        val lore = display.getTagList("Lore", Constants.NBT.TAG_STRING)

        // Determine the item's rarity
        for (i in 0 until lore.tagCount()) {
            val currentLine = lore.getStringTagAt(i)

            val rarityMatcher = RARITY_PATTERN.matcher(currentLine)
            if (rarityMatcher.find()) {
                val rarity = rarityMatcher.group("rarity")

                for (itemRarity in ItemRarity.values()) {
                    if (rarity.startsWith(itemRarity.loreName)) {
                        return itemRarity
                    }
                }
            }
        }

        // If the item doesn't have a valid rarity, return null
        return null
    }

    /**
     * Made By BiscuitDevelopment for SkyblockAddons
     * @author BiscuitDevelopment
     *
     * @param itemStack
     * @return
     */
    fun getLore(itemStack: ItemStack): List<String> {
        if (itemStack.hasTagCompound() && itemStack.tagCompound.hasKey("display", 10)) {
            val display = itemStack.tagCompound.getCompoundTag("display")

            if (display.hasKey("Lore", 9)) {
                val lore = display.getTagList("Lore", 8)

                val loreAsList = ArrayList<String>()
                for (lineNumber in 0 until lore.tagCount()) {
                    loreAsList.add(lore.getStringTagAt(lineNumber))
                }

                return loreAsList
            }
        }
        return emptyList()
    }

    /**
     * Made By BiscuitDevelopment for SkyblockAddons
     * @author BiscuitDevelopment
     *
     * @param item
     * @return
     */
    fun getItemType(item: ItemStack?): String? {
        if (item == null) {
            throw NullPointerException("Item cannot be null.")
        } else if (!item.hasTagCompound()) {
            return null
        }

        val skyBlockData = item.getSubCompound("ExtraAttributes", false)

        if (skyBlockData != null) {
            val itemId = skyBlockData.getString("id")

            if (itemId != "") {
                return itemId
            }
        }

        return null
    }

    fun getBazaarType(item: ItemStack?): String? {
        if (item == null) {
            throw NullPointerException("Item cannot be null.")
        } else if (!item.hasTagCompound()) {
            return null
        }
        val skyBlockData = item.getSubCompound("ExtraAttributes", false)
        if (skyBlockData != null) {
            val itemId = skyBlockData.getString("id")
            if (itemId != "") {
                return itemId
            }
        } else {
            if (StringUtils.stripControlCodes(item.displayName).contains("Bale") && !StringUtils.stripControlCodes(item.displayName).contains("Tied"))
                return StringUtils.stripControlCodes(
                        item.displayName.replace(" ".toRegex(), "_")
                                .replace("'".toRegex(), "")
                                .replace("-".toRegex(), "_")
                                .toUpperCase()
                                .replace("HAY_BALE".toRegex(), "HAY_BLOCK")
                ).toUpperCase()
            else if (StringUtils.stripControlCodes(item.displayName).contains("Wart"))
                return StringUtils.stripControlCodes(
                        item.displayName.replace(" ".toRegex(), "_")
                                .replace("'".toRegex(), "")
                                .replace("-".toRegex(), "_")
                                .toUpperCase()
                                .replace("WART".toRegex(), "STALK")
                ).toUpperCase()
            else if (StringUtils.stripControlCodes(item.displayName).contains("Experience"))
                return StringUtils.stripControlCodes(
                        item.displayName.replace(" ".toRegex(), "_")
                                .replace("'".toRegex(), "")
                                .replace("-".toRegex(), "_")
                                .toUpperCase()
                                .replace("EXPERIENCE".toRegex(), "EXP")
                ).toUpperCase()
            else if (StringUtils.stripControlCodes(item.displayName).contains("Mushroom Block"))
                return StringUtils.stripControlCodes(
                        item.displayName.replace(" ".toRegex(), "_")
                                .replace("'".toRegex(), "")
                                .replace("-".toRegex(), "_")
                                .toUpperCase()
                                .replace("_BLOCK".toRegex(), "")
                ).toUpperCase()
            else if (StringUtils.stripControlCodes(item.displayName).contains("Jerry Box"))
                return "JERRY_BOX_" + StringUtils.stripControlCodes(item.displayName).split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].toUpperCase()

            return StringUtils.stripControlCodes(
                    item.displayName.replace(" ".toRegex(), "_")
                            .replace("'".toRegex(), "")
                            .replace("-".toRegex(), "_")
                            .toUpperCase()
                            .replace("_DRAGON".toRegex(), "")
            )
        }

        return null
    }
}
