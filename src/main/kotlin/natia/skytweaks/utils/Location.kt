package natia.skytweaks.utils

import natia.skytweaks.SecretUtils

enum class Location private constructor(private val locationName: String) {
    VILLAGE("Village"),
    PRIVATE_ISLAND("Your Island"),
    THE_CATACOMBS("The Catacombs"),
    VOID_SEPULTURE("Void Sepulture"),
    THE_END("The End"),
    NONE("None");

    fun asName(): String {
        return locationName
    }

    override fun toString(): String {
        return locationName
    }

    companion object {

        fun getAsLocation(locationName: String): Location {
            for (location in values()) {
                if (location.asName().contains(locationName)) {
                    return location
                }
            }

            return NONE
        }

        val currentLocation: Location
            get() {
                try {
                    if (SecretUtils.isValid) {
                        val scoreboard = SecretUtils.scoreboardLines

                        for (s in scoreboard) {
                            val sCleaned = SecretUtils.cleanSB(s)
                            for (location in values()) {
                                if (sCleaned.contains(location.toString()))
                                    return location
                            }
                        }
                    }

                } catch (e: Exception) {

                }

                return Location.NONE
            }
    }
}
