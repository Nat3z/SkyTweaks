package com.natia.secretmod.features.altC;

import net.minecraft.util.ResourceLocation;

public class QuickTabItem {

    public String name;
    public String description;
    public String commandExecuted;
    public ResourceLocation texture;

    public QuickTabItem(String name, String description, String commandExecuted, ResourceLocation texture) {
        this.name = name;
        this.description = description;
        this.commandExecuted = commandExecuted;
        this.texture = texture;
    }
}
