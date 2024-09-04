package com.ufund.api.ufundapi.model;

import java.util.logging.Logger;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Gift item
 * 
 * @author Howard Kong
 * @author
 * @author
 * @author
 */
public class Gift implements Comparable<Gift> {
    
    private static final Logger LOG = Logger.getLogger(Gift.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Gift [id = %d, name = %s, description = %s, price = %f, priority = %s, amount needed = %d]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("description") private String description;
    @JsonProperty("price") private double price;
    @JsonProperty("priority") private Priority priority;
    @JsonProperty("amount_needed") private int amountNeeded;

    /**
     * Create a gift with the given fields
     * @param id The id of the gift
     * @param name The name of the gift
     * @param description The description of the gift
     * @param price The price of the gift
     * @param priority The priority of the gift
     * @param amountNeeded The amount of the gift needed
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Gift(@JsonProperty("id") int id, 
                @JsonProperty("name") String name,
                @JsonProperty("description") String description,
                @JsonProperty("price") double price,
                @JsonProperty("priority") Priority priority,
                @JsonProperty("amount_needed") int amountNeeded) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.priority = priority;
        this.amountNeeded = amountNeeded;
    }

    /**
     * Retrieve the id of the gift
     * @return The id of the gift
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieve the name of the gift
     * @return The name of the gift
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve the description of the gift
     * @return The description of the gift
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieve the price of the gift
     * @return The price of the gift
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Retrieve the priority of the gift
     * @return The priority of the gift
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Retrieve the amount needed of the gift
     * @return The amount of the gift needed
     */
    public int getAmountNeeded() {
        return amountNeeded;
    }

    /**
     * Set the name of the gift - necessary for JSON object to Java object deserialization
     * @param name The name of the gift
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the description of the gift - necessary for JSON object to Java object deserialization
     * @param description The description of the gift
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the price of the gift - necessary for JSON object to Java object deserialization
     * @param price The price of the gift
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Set the priority of the gift - necessary for JSON object to Java object deserialization
     * @param priority The priority of the gift
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Set the amount of the gift needed - necessary for JSON object to Java object deserialization
     * @param amountNeeded The amount of the gift needed
     */
    public void setAmountNeeded(int amountNeeded) {
        this.amountNeeded = amountNeeded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, id, name, description, price, priority.toString(), amountNeeded);
    }

    /**
     * Compare the priority of gifts
     * @param otherGift The other gift item to compare with
     */
    @Override
    public int compareTo(Gift otherGift) {
        int thisPriority = priority.getNumber();
        int otherPriority = otherGift.getPriority().getNumber();
   
        return (thisPriority < otherPriority) ? -1 : 
                    (thisPriority > otherPriority) ? 1 : 0;
    }
}