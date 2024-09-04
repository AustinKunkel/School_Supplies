package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Order Item class
 * 
 * @author Howard Kong
 * @author Austin Kunkel
 */
@Tag("Model-tier")
public class OrderItemTest {
    @Test
    public void testCreateOrderItem() {
        // Setup
        int expectedId = 1;
        int expectedItemAmount = 150;

        // Invoke
        OrderItem orderItem = new OrderItem(expectedId, "",expectedItemAmount);

        // Analyze
        assertNotEquals(orderItem, null);
        assertEquals(expectedId, orderItem.getItemId());
        assertEquals(expectedItemAmount, orderItem.getItemAmount());
    }

    @Test
    public void testSetItemId() {
        // Setup
        int id = 1;
        int itemAmount = 100;
        OrderItem orderItem = new OrderItem(id, "",itemAmount);

        int expectedId = 2;

        // Invoke
        orderItem.setItemId(expectedId);

        // Analyze
        assertEquals(expectedId, orderItem.getItemId());
    }

    @Test
    public void testSetItemAmount() {
        // Setup
        int id = 1;
        int itemAmount = 100;
        OrderItem orderItem = new OrderItem(id, "",itemAmount);

        int expectedItemAmount = 250;

        // Invoke
        orderItem.setItemAmount(expectedItemAmount);

        // Analyze
        assertEquals(expectedItemAmount, orderItem.getItemAmount());
    }

    @Test
    public void testSetItemAmountZero() {
        // Setup
        int id = 1;
        int itemAmount = 100;
        OrderItem orderItem = new OrderItem(id, "",itemAmount);

        int newItemAmount = -10;
        int expectedItemAmount = 0;

        // Invoke
        orderItem.setItemAmount(newItemAmount);

        // Analyze
        assertEquals(expectedItemAmount, orderItem.getItemAmount());
    }
}
