package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Gift item in a User cart
 * 
 * @author Howard Kong
 */
public class CartItem {
    @JsonProperty("item_id") private int itemId;
    @JsonProperty("name") private String itemName;
    @JsonProperty("amount") private int itemAmount; // Can not be less than 0

    /**
     * Create a cart item
     * @param itemId The id of the gift
     * @param itemName The name of the gift
     * @param itemAmount The amount of the gift in the cart
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public CartItem(@JsonProperty("item_id") int itemId,
                    @JsonProperty("name") String name,
                    @JsonProperty("amount") int itemAmount) {
        this.itemId = itemId;
        this.itemName = name;
        this.itemAmount = itemAmount;
    }

    /**
     * Retrieve the id of the item
     * @return The id of the item
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Retrieve the amount of the item
     * @return The amount of the item
     */
    public int getItemAmount() {
        return itemAmount;
    }

    /**
     * Retrieve the name of item
     * @return The name of the item
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Set the id of the item
     * @param itemId The id of the item
     */
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /**
     * Set the amount of the item
     * @param itemAmount The amount of the item
     */
    public void setItemAmount(int itemAmount) {
        if (itemAmount < 0)
            itemAmount = 0;

        this.itemAmount = itemAmount;
    }

    /**
     * Set the name of the item
     * @param itemName The name of the item
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Increment the amount of the item
     */
    public void incrementItemAmount() {
        this.itemAmount++;
    }
    
    /**
     * Decrement the amount of the item
     */
    public void decrementItemAmount() {
        this.itemAmount--;

        if (this.itemAmount < 0)
            this.itemAmount = 0;
    }

    /**
     * Check if another object is the same cart item
     * @param obj Other object to compare 
     * @return True if item ids are the same
     *         False otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CartItem))
            return false;  

        CartItem otherItem = (CartItem) obj;
        return this.itemId == otherItem.getItemId();
    }
}
