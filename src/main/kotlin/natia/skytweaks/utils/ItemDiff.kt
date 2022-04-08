package natia.skytweaks.utils

/**
 * made by BiscuitDevelopment's SkyblockAddons
 * MIT License
 */
class ItemDiff
/**
 * @param displayName The item's display name.
 * @param amount      The changed amount.
 */
(
        /** The item's display name.  */
        /**
         * @return Display name of item
         */
        val displayName: String, amount: Int,
        /**
         * @return Location of where item was picked up.
         */
        val location: Location) {

    /** The changed amount.  */
    /**
     * @return Amount of item
     */
    var amount: Int = 0
        private set
        public get

    private var timestamp: Long = 0

    /**
     * @return Amount of time in ms since the ItemDiff was created.
     */
    val lifetime: Long
        get() = System.currentTimeMillis() - timestamp

    init {
        this.amount = amount
        this.timestamp = System.currentTimeMillis()
    }

    /**
     * Update the changed amount of the item.
     *
     * @param amount Amount to be added
     */
    fun add(amount: Int) {
        this.amount += amount
        if (this.amount == 0) {
            this.timestamp -= LIFESPAN
        } else {
            this.timestamp = System.currentTimeMillis()
        }
    }

    companion object {
        /**
         * How long items in the log should be displayed before they are removed in ms
         */
        val LIFESPAN: Long = 5000
    }
}
