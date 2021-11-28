package com.natia.secretmod.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemUtils {

    /*
     * Taken from BiscuitDevelopment's SkyblockAddons under MIT License.
     * https://github.com/BiscuitDevelopment/SkyblockAddons/
     */
    private static final Pattern RARITY_PATTERN = Pattern.compile("(§[0-9a-f]§l§ka§r )?([§0-9a-fk-or]+)(?<rarity>[A-Z]+)");
    /*
     * Taken from BiscuitDevelopment's SkyblockAddons under MIT License.
     * https://github.com/BiscuitDevelopment/SkyblockAddons/
     *
     * Gets the Item's Rarity through Regex.
     */
    public static ItemRarity getRarity(ItemStack item) {
        if (item == null || !item.hasTagCompound())  {
            return null;
        }

        NBTTagCompound display = item.getSubCompound("display", false);

        if (display == null || !display.hasKey("Lore")) {
            return null;
        }

        NBTTagList lore = display.getTagList("Lore", Constants.NBT.TAG_STRING);

        // Determine the item's rarity
        for (int i = 0; i < lore.tagCount(); i++) {
            String currentLine = lore.getStringTagAt(i);

            Matcher rarityMatcher = RARITY_PATTERN.matcher(currentLine);
            if (rarityMatcher.find()) {
                String rarity = rarityMatcher.group("rarity");

                for (ItemRarity itemRarity : ItemRarity.values()) {
                    if (rarity.startsWith(itemRarity.loreName)) {
                        return itemRarity;
                    }
                }
            }
        }

        // If the item doesn't have a valid rarity, return null
        return null;
    }
    /**
     * Made By BiscuitDevelopment for SkyblockAddons
     * @author BiscuitDevelopment
     *
     * @param itemStack
     * @return
     */
    public static List<String> getLore(ItemStack itemStack) {
        if(itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("display", 10)) {
            NBTTagCompound display = itemStack.getTagCompound().getCompoundTag("display");

            if (display.hasKey("Lore", 9)) {
                NBTTagList lore = display.getTagList("Lore", 8);

                List<String> loreAsList = new ArrayList<String>();
                for (int lineNumber = 0; lineNumber < lore.tagCount(); lineNumber++) {
                    loreAsList.add(lore.getStringTagAt(lineNumber));
                }

                return loreAsList;
            }
        }
        return Collections.emptyList();
    }

    /**
     * Made By BiscuitDevelopment for SkyblockAddons
     * @author BiscuitDevelopment
     *
     * @param item
     * @return
     */
    public static String getItemType(ItemStack item) {
        if (item == null) {
            throw new NullPointerException("Item cannot be null.");
        }
        else if (!item.hasTagCompound()) {
            return null;
        }

        NBTTagCompound skyBlockData = item.getSubCompound("ExtraAttributes", false);

        if (skyBlockData != null) {
            String itemId = skyBlockData.getString("id");

            if (!itemId.equals("")) {
                return itemId;
            }
        }

        return null;
    }

    public static String getBazaarType(ItemStack item) {
        if (item == null) {
            throw new NullPointerException("Item cannot be null.");
        }
        else if (!item.hasTagCompound()) {
            return null;
        }
        NBTTagCompound skyBlockData = item.getSubCompound("ExtraAttributes", false);
        if (skyBlockData != null) {
            String itemId = skyBlockData.getString("id");
            if (!itemId.equals("")) {
                return itemId;
            }
        } else {
            if (StringUtils.stripControlCodes(item.getDisplayName()).contains("Bale") && !StringUtils.stripControlCodes(item.getDisplayName()).contains("Tied"))
                return StringUtils.stripControlCodes(
                        item.getDisplayName().replaceAll(" ", "_")
                                .replaceAll("'", "")
                                .replaceAll("-", "_")
                                .toUpperCase()
                                .replaceAll("HAY_BALE", "HAY_BLOCK")
                ).toUpperCase();

            else if (StringUtils.stripControlCodes(item.getDisplayName()).contains("Wart"))
                return StringUtils.stripControlCodes(
                        item.getDisplayName().replaceAll(" ", "_")
                                .replaceAll("'", "")
                                .replaceAll("-", "_")
                                .toUpperCase()
                                .replaceAll("WART", "STALK")
                ).toUpperCase();

            else if (StringUtils.stripControlCodes(item.getDisplayName()).contains("Experience"))
                return StringUtils.stripControlCodes(
                        item.getDisplayName().replaceAll(" ", "_")
                                .replaceAll("'", "")
                                .replaceAll("-", "_")
                                .toUpperCase()
                                .replaceAll("EXPERIENCE", "EXP")
                ).toUpperCase();

            else if (StringUtils.stripControlCodes(item.getDisplayName()).contains("Mushroom Block"))
                return StringUtils.stripControlCodes(
                        item.getDisplayName().replaceAll(" ", "_")
                                .replaceAll("'", "")
                                .replaceAll("-", "_")
                                .toUpperCase()
                                .replaceAll("_BLOCK", "")
                ).toUpperCase();

            else if (StringUtils.stripControlCodes(item.getDisplayName()).contains("Jerry Box"))
                return "JERRY_BOX_" + StringUtils.stripControlCodes(item.getDisplayName()).split(" ")[0].toUpperCase();

            return
                    StringUtils.stripControlCodes(
                            item.getDisplayName().replaceAll(" ", "_")
                                    .replaceAll("'", "")
                                    .replaceAll("-", "_")
                                    .toUpperCase()
                                    .replaceAll("_DRAGON", "")
                    );
        }

        return null;
    }
}
