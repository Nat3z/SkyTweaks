package com.natia.secretmod.extensions;

import com.natia.secretmod.vicious.ViciousConfig;
import com.natia.secretmod.vicious.ViciousMod;

public class Extension extends ViciousMod {

    private String description;
    public Extension(ViciousConfig viciousConfig, String extensionName, String description) {
        super(viciousConfig, extensionName);
        System.out.println(extensionName + " has been registered as a Skyblock Secret Mod extension!");
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
