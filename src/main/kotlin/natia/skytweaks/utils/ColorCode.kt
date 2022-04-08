package natia.skytweaks.utils

import com.google.common.base.Preconditions
import org.apache.commons.lang3.StringUtils

import java.awt.*

/**
 * Taken from SkyblockAddons under the MIT License
 * https://github.com/BiscuitDevelopment/SkyblockAddons
 */
enum class ColorCode private constructor(val code: Char, val isFormat: Boolean, private val jsonName: String?, rgb: Int = -1) {

    BLACK('0', 0x000000),
    DARK_BLUE('1', 0x0000AA),
    DARK_GREEN('2', 0x00AA00),
    DARK_AQUA('3', 0x00AAAA),
    DARK_RED('4', 0xAA0000),
    DARK_PURPLE('5', 0xAA00AA),
    GOLD('6', 0xFFAA00),
    GRAY('7', 0xAAAAAA),
    DARK_GRAY('8', 0x555555),
    BLUE('9', 0x5555FF),
    GREEN('a', 0x55FF55),
    AQUA('b', 0x55FFFF),
    RED('c', 0xFF5555),
    LIGHT_PURPLE('d', 0xFF55FF),
    YELLOW('e', 0xFFFF55),
    WHITE('f', 0xFFFFFF),
    MAGIC('k', true, "obfuscated"),
    BOLD('l', true),
    STRIKETHROUGH('m', true),
    UNDERLINE('n', true, "underlined"),
    ITALIC('o', true),
    RESET('r');

    private val toString: String
    private val color: Color?

    val rgb: Int
        get() = this.getColor()!!.rgb

    val isColor: Boolean
        get() = !this.isFormat && this != RESET

    val nextFormat: ColorCode
        get() = this.getNextFormat(ordinal)

    private constructor(code: Char, rgb: Int = -1) : this(code, false, rgb) {}

    private constructor(code: Char, isFormat: Boolean, rgb: Int = -1) : this(code, isFormat, null, rgb) {}

    init {
        this.toString = String(charArrayOf(ColorCode.COLOR_CHAR, code))
        this.color = if (this.isColor) Color(rgb) else null
    }

    fun getColor(): Color? {
        Preconditions.checkArgument(this.isColor, "Format has no color!")
        return this.color
    }

    fun getColor(alpha: Float): Color {
        return this.getColor(alpha.toInt())
    }

    fun getColor(alpha: Int): Color {
        return Color(this.color!!.red, this.color.green, this.color.blue, alpha)
    }

    fun getJsonName(): String? {
        return if (StringUtils.isEmpty(this.jsonName)) this.name.toLowerCase() else this.jsonName
    }

    private fun getNextFormat(ordinal: Int): ColorCode {
        val nextColor = ordinal + 1

        if (nextColor > values().size - 1) {
            return values()[0]
        } else if (!values()[nextColor].isColor) {
            return getNextFormat(nextColor)
        }

        return values()[nextColor]
    }

    override fun toString(): String {
        return this.toString
    }

    companion object {

        val COLOR_CHAR = '\u00a7'

        /**
         * Get the color represented by the specified code.
         *
         * @param code The code to search for.
         * @return The mapped color, or null if non exists.
         */
        fun getByChar(code: Char): ColorCode? {
            for (color in values()) {
                if (color.code == code)
                    return color
            }

            return null
        }
    }
}