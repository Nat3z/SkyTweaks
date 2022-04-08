package natia.skytweaks.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import natia.skytweaks.SecretUtils

object SuggestedName {

    fun getSuggested(approxItem: String): String {
        var approximatedItemName = ""
        if (!SecretUtils.bazaarCached.get("success").asBoolean) return approxItem.toUpperCase().replace(" ", "_")
        /* Base Extractions */
        if (approxItem.toUpperCase().replace(" ", "_") == "RAW_RABBIT") {
            return "RABBIT"
        }

        val products = SecretUtils.bazaarCached.get("products").asJsonObject
        var success = false
        for ((name) in products.entrySet()) {
            try {
                if (name.contains(approxItem.toUpperCase().replace(" ", "_")) && name.startsWith("ENCHANTED") && approxItem.toUpperCase().replace(" ", "_").startsWith("ENCHANTED")) {
                    approximatedItemName = name
                    success = true
                    break
                }

                val withoutEnhancer = approxItem.toUpperCase().replace(" ", "_").split("_".toRegex(), 2).toTypedArray()[1]
                if (name.contains(withoutEnhancer) && name.startsWith("ENCHANTED") && withoutEnhancer.startsWith("ENCHANTED")) {
                    approximatedItemName = name
                    success = true
                    break
                }
            } catch (ignored: Exception) {

            }

        }

        /* no effective suggestion. Just return the formatted string. */
        if (!success) {
            approximatedItemName = approxItem.toUpperCase().replace(" ", "_")
        }

        return approximatedItemName
    }

}
