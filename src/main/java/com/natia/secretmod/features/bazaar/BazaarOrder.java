package com.natia.secretmod.features.bazaar;

public class BazaarOrder {
    private float amount;
    private String item;
    private double coins;

    public BazaarOrder(float amount, String item, double coins) {
        this.amount = amount;
        this.item = item;
        this.coins = coins;
    }

    public double getCoins() {
        return coins;
    }

    public float getAmount() {
        return amount;
    }

    public String getItem() {
        return item;
    }
}
