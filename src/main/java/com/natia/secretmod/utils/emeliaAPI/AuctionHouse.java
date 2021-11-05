package com.natia.secretmod.utils.emeliaAPI;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.natia.secretmod.utils.WebUtils.fetch;

public class AuctionHouse {

    public static String authorizable = "gMoThEA8RFDMj@c?KqT@asJ";

    /**
     * Gets Auction House Info
     */
    public static JsonArray getAuction(String query) {
        AtomicReference<JsonArray> array = new AtomicReference<>(new JsonArray());
        fetch("http://localhost:3000/ah?token=" + authorizable + "&" + query, res -> {
            if (res.asJson().get("success").getAsBoolean()) {
                array.set(res.asJson().get("auctions").getAsJsonArray());
            }
        });
        return array.get();
    }

    public static JsonObject getLowest(JsonArray auctions) {
        AtomicInteger lowest = new AtomicInteger();
        AtomicReference<Double> minLowest = new AtomicReference<>(10000000000000d);
        AtomicInteger currAuction = new AtomicInteger(-1);
        auctions.forEach(jsonElement -> {
            currAuction.getAndIncrement();
            if (jsonElement.getAsJsonObject().get("starting_bid").getAsDouble() < minLowest.get()) {
                lowest.set(currAuction.get());
                minLowest.set(jsonElement.getAsJsonObject().get("starting_bid").getAsDouble());
            }
        });

        return auctions.get(lowest.get()).getAsJsonObject();
    }

    public static JsonObject getSecondLowest(JsonArray auctions) {
        AtomicInteger lowest = new AtomicInteger(9);
        AtomicInteger lastLowest = new AtomicInteger(9);

        AtomicReference<Double> minLowest = new AtomicReference<>(10000000000000d);
        AtomicInteger currAuction = new AtomicInteger(-1);
        auctions.forEach(jsonElement -> {
            currAuction.getAndIncrement();
            if (jsonElement.getAsJsonObject().get("starting_bid").getAsDouble() < minLowest.get()) {
                lastLowest.set(lowest.get());
                lowest.set(currAuction.get());
                minLowest.set(jsonElement.getAsJsonObject().get("starting_bid").getAsDouble());
            }
        });

        return auctions.get(lastLowest.get()).getAsJsonObject();
    }

}
