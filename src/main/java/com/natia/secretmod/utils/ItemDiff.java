package com.natia.secretmod.utils;

/**
 * made by BiscuitDevelopment's SkyblockAddons
 * MIT License
 */
public class ItemDiff {
    /**
     * How long items in the log should be displayed before they are removed in ms
     */
    public static final long LIFESPAN = 5000;

    /** The item's display name. */
    private final String displayName;

    /** The changed amount. */
    private int amount;

    private long timestamp;
    private Location location;

    /**
     * @param displayName The item's display name.
     * @param amount      The changed amount.
     */
    public ItemDiff(String displayName, int amount, Location location) {
        this.displayName = displayName;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
        this.location = location;
    }

    /**
     * Update the changed amount of the item.
     *
     * @param amount Amount to be added
     */
    public void add(int amount) {
        this.amount += amount;
        if (this.amount == 0) {
            this.timestamp -= LIFESPAN;
        } else {
            this.timestamp = System.currentTimeMillis();
        }
    }

    /**
     * @return Amount of time in ms since the ItemDiff was created.
     */
    public long getLifetime() {
        return System.currentTimeMillis() - timestamp;
    }

    /**
     * @return Display name of item
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return Amount of item
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @return Location of where item was picked up.
     */
    public Location getLocation() {
        return this.location;
    }
}
