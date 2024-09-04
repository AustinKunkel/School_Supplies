package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Gift class
 * 
 * @author Howard Kong
 */
@Tag("Model-tier")
public class GiftTest {
    @Test
    public void testCreateGift() {
        // Setup
        int expectedId = 99;
        String expectedName = "Wifi Routers";
        String expectDescription = "Yummy internet";
        double expectedPrice = 150.00;
        Priority expectedPriority = Priority.MID;
        int expectedPriorityNumber = 2;
        int expectedAmountNeeded = 100;

        // Invoke
        Gift gift = new Gift(expectedId, expectedName, expectDescription, expectedPrice, expectedPriority, expectedAmountNeeded);

        // Analyze
        assertNotEquals(gift, null);
        assertEquals(expectedId, gift.getId());
        assertEquals(expectedName, gift.getName());
        assertEquals(expectDescription, gift.getDescription());
        assertEquals(expectedPrice, gift.getPrice());
        assertEquals(expectedPriority, gift.getPriority());
        assertEquals(expectedPriorityNumber, expectedPriority.getNumber());
        assertEquals(expectedAmountNeeded, gift.getAmountNeeded());
    }

    @Test
    public void testSetName() {
        // Setup
        int id = 99;
        String name = "Wifi Routers";
        String description = "Yummy internet";
        double price = 150.00;
        Priority priority = Priority.MID;
        int amountNeeded = 100;
        Gift gift = new Gift(id, name, description, price, priority, amountNeeded);

        String expectedName = "Ethernet Cables";

        // Invoke
        gift.setName(expectedName);

        // Analyze
        assertEquals(expectedName, gift.getName());
    }

    @Test
    public void testSetDescription() {
        // Setup
        int id = 99;
        String name = "Wifi Routers";
        String description = "Yummy internet";
        double price = 150.00;
        Priority priority = Priority.MID;
        int amountNeeded = 100;
        Gift gift = new Gift(id, name, description, price, priority, amountNeeded);

        String expectedDescription = "Internet";

        // Invoke
        gift.setDescription(expectedDescription);

        // Analyze
        assertEquals(expectedDescription, gift.getDescription());
    }

    @Test
    public void testSetPrice() {
        // Setup
        int id = 99;
        String name = "Wifi Routers";
        String description = "Yummy internet";
        double price = 150.00;
        Priority priority = Priority.MID;
        int amountNeeded = 100;
        Gift gift = new Gift(id, name, description, price, priority, amountNeeded);

        double expectedPrice = 500.00;

        // Invoke
        gift.setPrice(expectedPrice);

        // Analyze
        assertEquals(expectedPrice, gift.getPrice());
    }

    @Test
    public void testSetPriority() {
        // Setup
        int id = 99;
        String name = "Wifi Routers";
        String description = "Yummy internet";
        double price = 150.00;
        int priorityNumber = 2;
        int amountNeeded = 100;
        Gift gift = new Gift(id, name, description, price, Priority.getPriority(priorityNumber), amountNeeded);

        int expectedpriority = 1;

        // Invoke
        gift.setPriority(Priority.getPriority(expectedpriority));

        // Analyze
        assertEquals(expectedpriority, gift.getPriority().getNumber());
        assertEquals(Priority.getPriority(expectedpriority), gift.getPriority());
    }

    @Test
    public void testSetAmountNeeded() {
        // Setup
        int id = 99;
        String name = "Wifi Routers";
        String description = "Yummy internet";
        double price = 150.00;
        Priority priority = Priority.MID;
        int amountNeeded = 100;
        Gift gift = new Gift(id, name, description, price, priority, amountNeeded);

        int expectedAmountNeeded = 100;

        // Invoke
        gift.setAmountNeeded(expectedAmountNeeded);

        // Analyze
        assertEquals(expectedAmountNeeded, gift.getAmountNeeded());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String name = "Wifi Routers";
        String description = "Yummy internet";
        double price = 150.00;
        int priorityNumber = 2;
        int amountNeeded = 100;

        String expectedString = String.format(Gift.STRING_FORMAT, id, name, description, price, Priority.getPriority(priorityNumber), amountNeeded);
        Gift gift = new Gift(id, name, description, price, Priority.getPriority(priorityNumber), amountNeeded);

        // Invoke
        String actualString = gift.toString();

        // Analyze
        assertEquals(expectedString, actualString);
    }
}