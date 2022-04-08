package natia.skytweaks.utils.hypixel

import com.google.gson.JsonObject
import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.utils.WebUtils
import natia.skytweaks.SkyTweaks
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting

import java.util.concurrent.atomic.AtomicReference

object Profiles {
    fun getProfile(UUID: String): JsonObject {
        val returnValue = AtomicReference<JsonObject>()
        WebUtils.fetch("https://api.hypixel.net/skyblock/profile?key=" + SkyTweaksConfig.apiKey + "&profile=" + UUID, { res -> returnValue.set(res.asJson()) })

        return returnValue.get()
    }

    fun getLatestProfile(UUID: String): String {
        val key = SkyTweaksConfig.apiKey
        val player = Minecraft.getMinecraft().thePlayer
        val returnValue = AtomicReference("")
        // Get profiles
        SkyTweaks.LOGGER.info("Fetching profiles...")
        WebUtils.fetch("https://api.hypixel.net/skyblock/profiles?key=$key&uuid=$UUID", { res ->
            val profilesResponse = res.asJson()
            if (!profilesResponse.get("success").getAsBoolean()) {
                val reason = profilesResponse.get("cause").getAsString()
                player.addChatMessage(ChatComponentText(EnumChatFormatting.RED.toString() + "Failed with reason: " + reason))
            } else {
                if (profilesResponse.get("profiles").isJsonNull()) {
                    player.addChatMessage(ChatComponentText(EnumChatFormatting.RED.toString() + "This player doesn't appear to have played SkyBlock."))
                } else {
                    // Loop through profiles to find latest
                    SkyTweaks.LOGGER.info("Looping through profiles...")
                    var latestProfile = ""
                    var latestSave: Long = 0
                    val profilesArray = profilesResponse.get("profiles").getAsJsonArray()

                    for (profile in profilesArray) {
                        val profileJSON = profile.getAsJsonObject()
                        var profileLastSave: Long = 1
                        if (profileJSON.get("members").getAsJsonObject().get(UUID).getAsJsonObject().has("last_save")) {
                            profileLastSave = profileJSON.get("members").getAsJsonObject().get(UUID).getAsJsonObject().get("last_save").getAsLong()
                        }

                        if (profileLastSave > latestSave) {
                            latestProfile = profileJSON.get("profile_id").getAsString()
                            latestSave = profileLastSave
                        }
                        returnValue.set(latestProfile)
                    }
                }
            }
        })
        return returnValue.get()
    }
}
