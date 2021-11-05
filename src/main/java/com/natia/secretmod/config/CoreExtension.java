package com.natia.secretmod.config;

import com.natia.secretmod.extensions.Extension;
import com.natia.secretmod.vicious.ViciousConfig;
import com.natia.secretmod.vicious.ViciousMod;

public class CoreExtension extends Extension {
    public CoreExtension() {
        super(new SecretModConfig(), "Core Extension", "Required to activate Skyblock Secret Mod and corresponding extensions.");
    }
}
