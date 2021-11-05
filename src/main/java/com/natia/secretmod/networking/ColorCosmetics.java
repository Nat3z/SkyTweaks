package com.natia.secretmod.networking;

import static com.natia.secretmod.utils.WebUtils.fetch;

public class ColorCosmetics {
    public ColorCosmetics(String targetColor) {
        fetch("https://secretmod-hypixel.herokuapp.com/colors?color=" + targetColor, res -> {
            if (!res.asString().equals("-1")) {
                String colors = res.asJson().get("color").getAsString();
                colors = colors.replace("[(<splitter>)", "\n");
                users = colors;
            }
        });
    }

    public String users;
}
