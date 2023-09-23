package com.example.quickshopapp.viewholder;
public class Item {
    private String itemName;
    private int itemImageResource;

    public Item(String itemName, int itemImageResource) {
        this.itemName = itemName;
        this.itemImageResource = itemImageResource;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemImageResource() {
        return itemImageResource;
    }
}
