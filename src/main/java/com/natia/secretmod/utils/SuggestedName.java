package com.natia.secretmod.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.natia.secretmod.SecretUtils;

import java.util.Map;

import static com.natia.secretmod.utils.WebUtils.fetch;

public class SuggestedName {

    public static String getSuggested(String approxItem) {
        String approximatedItemName = "";
        if (!SecretUtils.bazaarCached.get("success").getAsBoolean()) return approxItem.toUpperCase().replace(" ", "_");
        /* Base Extractions */
        if (approxItem.toUpperCase().replace(" ", "_").equals("RAW_RABBIT")) {
            return "RABBIT";
        }

        JsonObject products = SecretUtils.bazaarCached.get("products").getAsJsonObject();
        boolean success = false;
        for(Map.Entry<String, JsonElement> entry : products.entrySet()) {
            String name = entry.getKey();
            try {
                if (name.contains(approxItem.toUpperCase().replace(" ", "_"))
                        && (name.startsWith("ENCHANTED") && approxItem.toUpperCase().replace(" ", "_").startsWith("ENCHANTED"))) {
                    approximatedItemName = name;
                    success = true;
                    break;
                }

                String withoutEnhancer = approxItem.toUpperCase().replace(" ", "_").split("_", 2)[1];
                if (name.contains(withoutEnhancer)
                        && (name.startsWith("ENCHANTED") && withoutEnhancer.startsWith("ENCHANTED"))) {
                    approximatedItemName = name;
                    success = true;
                    break;
                }
            } catch (Exception ignored) {

            }
        }

        /* no effective suggestion. Just return the formatted string. */
        if (!success) {
            approximatedItemName = approxItem.toUpperCase().replace(" ", "_");
        }

        return approximatedItemName;
    }

}
