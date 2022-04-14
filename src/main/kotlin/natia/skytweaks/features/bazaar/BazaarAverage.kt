package natia.skytweaks.features.bazaar

class BazaarAverage(val id: String, val displayName: String) {
    val orders: MutableList<Double> = ArrayList()


    fun putPrice(price: Double) {
        orders.add(price)
    }

    fun calcAverage(): Double {
        var everything = 0.0
        orders.forEach {
            everything += it
        }
        return everything / orders.size
    }
}