package natia.skytweaks.utils

import java.util.concurrent.atomic.AtomicReference

object MojangAPI {

    fun uuidToUsername(uuid: String): String {
        val username = AtomicReference("")
        WebUtils.fetch("https://api.mojang.com/user/profiles/$uuid/names", { res -> username.set(res.asJsonArray().get(res.asJsonArray().size() - 1).getAsJsonObject().get("name").getAsString()) })

        return username.get()
    }

    fun usernameToUUID(username: String): String {
        val uuid = AtomicReference("")
        WebUtils.fetch("https://api.mojang.com/users/profiles/minecraft/$username", { res -> uuid.set(res.asJson().get("id").getAsString()) })

        return uuid.get()
    }

}
