package com.natia.secretmod.utils;

import net.minecraft.util.EnumChatFormatting;

/**
 * Taken from SkyblockAddons under the MIT License
 * https://github.com/BiscuitDevelopment/SkyblockAddons
 */
public enum ItemRarity {
    COMMON("COMMON", ColorCode.WHITE),
    UNCOMMON("UNCOMMON", ColorCode.GREEN),
    RARE("RARE", ColorCode.BLUE),
    EPIC("EPIC", ColorCode.DARK_PURPLE),
    LEGENDARY("LEGENDARY", ColorCode.GOLD),
    MYTHIC("MYTHIC", ColorCode.LIGHT_PURPLE),
    SUPREME("SUPREME", ColorCode.DARK_RED),

    SPECIAL("SPECIAL", ColorCode.RED),
    VERY_SPECIAL("VERY SPECIAL", ColorCode.RED);

    public String loreName;
    public ColorCode colorCode;

    ItemRarity(String loreName, ColorCode colorCode) {
        this.loreName = loreName;
        this.colorCode = colorCode;
    }
}
