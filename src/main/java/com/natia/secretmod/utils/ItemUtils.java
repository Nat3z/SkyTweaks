package com.natia.secretmod.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemUtils {

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
