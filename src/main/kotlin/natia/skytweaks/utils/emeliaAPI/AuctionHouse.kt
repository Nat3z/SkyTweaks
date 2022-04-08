package natia.skytweaks.utils.emeliaAPI

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import natia.skytweaks.utils.WebUtils

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

object AuctionHouse {

    var authorizable = "gMoThEA8RFDMj@c?KqT@asJ"

    /**
     * Gets Auction House Info
     */
    fun getAuction(query: String): JsonArray {
        val array = AtomicReference(JsonArray())
        WebUtils.fetch("http://localhost:3000/ah?token=$authorizable&$query", { res ->
            if (res.asJson().get("success").getAsBoolean()) {
                array.set(res.asJson().get("auctions").getAsJsonArray())
            }
        })
        return array.get()
    }

    fun getLowest(auctions: JsonArray): JsonObject {
        val lowest = AtomicInteger()
        val minLowest = AtomicReference(10000000000000.0)
        val currAuction = AtomicInteger(-1)
        auctions.forEach { jsonElement ->
            currAuction.getAndIncrement()
            if (jsonElement.asJsonObject.get("starting_bid").asDouble < minLowest.get()) {
                lowest.set(currAuction.get())
                minLowest.set(jsonElement.asJsonObject.get("starting_bid").asDouble)
            }
        }

        return auctions.get(lowest.get()).asJsonObject
    }

    fun getSecondLowest(auctions: JsonArray): JsonObject {
        val lowest = AtomicInteger(9)
        val lastLowest = AtomicInteger(9)

        val minLowest = AtomicReference(10000000000000.0)
        val currAuction = AtomicInteger(-1)
        auctions.forEach { jsonElement ->
            currAuction.getAndIncrement()
            if (jsonElement.asJsonObject.get("starting_bid").asDouble < minLowest.get()) {
                lastLowest.set(lowest.get())
                lowest.set(currAuction.get())
                minLowest.set(jsonElement.asJsonObject.get("starting_bid").asDouble)
            }
        }

        return auctions.get(lastLowest.get()).asJsonObject
    }

}
