package com.natia.secretmod.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class FetchResponse {
    String res;
    public FetchResponse(String res) {
        this.res = res;
    }

    public String asString() {
        return this.res;
    }

    public JsonObject asJson() {
        Gson gson = new Gson();
        return gson.fromJson(res, JsonObject.class);
    }

    public JsonArray asJsonArray() {
        Gson gson = new Gson();
        return gson.fromJson(res, JsonArray.class);
    }
}
