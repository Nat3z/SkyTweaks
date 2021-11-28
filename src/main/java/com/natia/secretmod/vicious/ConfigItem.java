package com.natia.secretmod.vicious;

import java.lang.reflect.Field;

public class ConfigItem {
    private String name;
    private String description;
    private String category;
    private ConfigType vtype;
    private Field var;
    private ViciousConfig config;
    private String[] sliderOptions;
    private String subCategory;
    private boolean uayor;
    private boolean hidden;

    private int minAmount = 0;
    private int maxAmount = 0;

    public ConfigItem(Field variable, String name, String description, String subCategory, String category, ConfigType type, boolean uayor, String[] sliderOptions, boolean hidden, int minAmount, int maxAmount, ViciousConfig config) {
        this.name = name;
        this.hidden = hidden;
        this.description = description;
        this.category = category;
        vtype = type;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.config = config;
        var = variable;
        this.sliderOptions = sliderOptions;
        this.uayor = uayor;
        this.subCategory = subCategory;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public boolean isHidden() {
        return hidden;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String[] getSliderOptions() {
        return sliderOptions;
    }

    public Field getField() {
        return var;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ConfigType getType() {
        return vtype;
    }

    public String getCategory() {
        return category;
    }

    public Object getValue() {
        try {
            return var.get(config);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean shouldUseAtOwnRisk() {
        return uayor;
    }

    public ViciousConfig getConfig() {
        return config;
    }
}
