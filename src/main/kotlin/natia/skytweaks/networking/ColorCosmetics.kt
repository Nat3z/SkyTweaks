package natia.skytweaks.networking

import natia.skytweaks.utils.WebUtils.fetch

class ColorCosmetics(targetColor: String) {

    var users: String = ""

    init {
        fetch("https://skytweaks-web.vercel.app/colors?color=$targetColor") { res ->
            if (res.asString() != "-1") {
                var colors = res.asJson().get("color").asString
                colors = colors.replace("[(<splitter>)", "\n")
                users = colors
            }
        }
    }
}
