package natia.skytweaks.utils

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import jdk.nashorn.internal.parser.JSONParser
import natia.skytweaks.SkyTweaks
import scala.util.parsing.json.JSONObject


object SuggestedName {
    var bazaarConversions =
        JsonParser().parse((SkyTweaks::class.java.getResource("/bz_conversion.json"))?.readText()).asJsonObject
    var bazaarConversionsReversed =
        JsonParser().parse((SkyTweaks::class.java.getResource("/item_to_bz_conversion.json"))?.readText()).asJsonObject

    fun getSuggested(approxItem: String): String {
        if (bazaarConversionsReversed.has(approxItem))
            return bazaarConversionsReversed.get(approxItem).asString
        else
            return ""
    }

}
