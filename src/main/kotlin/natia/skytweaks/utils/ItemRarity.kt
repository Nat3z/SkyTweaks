package natia.skytweaks.utils

/**
 * Taken from SkyblockAddons under the MIT License
 * https://github.com/BiscuitDevelopment/SkyblockAddons
 */
enum class ItemRarity private constructor(var loreName: String, var colorCode: ColorCode) {
    COMMON("COMMON", ColorCode.WHITE),
    UNCOMMON("UNCOMMON", ColorCode.GREEN),
    RARE("RARE", ColorCode.BLUE),
    EPIC("EPIC", ColorCode.DARK_PURPLE),
    LEGENDARY("LEGENDARY", ColorCode.GOLD),
    MYTHIC("MYTHIC", ColorCode.LIGHT_PURPLE),
    SUPREME("SUPREME", ColorCode.DARK_RED),

    SPECIAL("SPECIAL", ColorCode.RED),
    VERY_SPECIAL("VERY SPECIAL", ColorCode.RED)
}
