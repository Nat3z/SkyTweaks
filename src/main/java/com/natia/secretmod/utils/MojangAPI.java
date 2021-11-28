package com.natia.secretmod.utils;

import java.util.concurrent.atomic.AtomicReference;

import static com.natia.secretmod.utils.WebUtils.fetch;

public class MojangAPI {

    public static String uuidToUsername(String uuid) {
        AtomicReference<String> username = new AtomicReference<>("");
        fetch("https://api.mojang.com/user/profiles/" + uuid + "/names", res -> {
            username.set(res.asJsonArray().get(res.asJsonArray().size() - 1).getAsJsonObject().get("name").getAsString());
        });

        return username.get();
    }

    public static String usernameToUUID(String username) {
        AtomicReference<String> uuid = new AtomicReference<>("");
        fetch("https://api.mojang.com/users/profiles/minecraft/" + username, res -> {
            uuid.set(res.asJson().get("id").getAsString());
        });

        return uuid.get();
    }

}
