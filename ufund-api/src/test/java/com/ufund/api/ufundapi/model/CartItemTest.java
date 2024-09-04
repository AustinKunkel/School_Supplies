package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Cart Item class
 * 
 * @author Howard Kong
 */
@Tag("Model-tier")
public class CartItemTest {
    @Test
    public void testCreateCartItem() {
        // Setup
        int expectedId = 1;
        int expectedItemAmount = 100;
        String expectedItemName = "name";

        // Invoke
        CartItem cartItem = new CartItem(expectedId, expectedItemName, expectedItemAmount);

        // Analyze
        assertNotEquals(cartItem, null);
        assertEquals(expectedId, cartItem.getItemId());
        assertEquals(expectedItemAmount, cartItem.getItemAmount());
        assertEquals(expectedItemName, cartItem.getItemName());
    }

    @Test
    public void testSetItemId() {
        // Setup
        int id = 1;
        int itemAmount = 100;
        String itemName = "name";

        CartItem cartItem = new CartItem(id, itemName, itemAmount);

        int expectedId = 2;

        // Invoke
        cartItem.setItemId(expectedId);

        // Analyze
        assertEquals(expectedId, cartItem.getItemId());
    }

    @Test
    public void testSetItemAmount() {
        // Setup
        int id = 1;
        int itemAmount = 100;
        String itemName = "name";

        CartItem cartItem = new CartItem(id, itemName, itemAmount);

        int expectedItemAmount = 250;

        // Invoke
        cartItem.setItemAmount(expectedItemAmount);

        // Analyze
        assertEquals(expectedItemAmount, cartItem.getItemAmount());
    }

    @Test
    public void testSetItemName() {
        // Setup
        int id = 1;
        int itemAmount = 100;
        String itemName = "name";

        CartItem cartItem = new CartItem(id, itemName, itemAmount);

        String expectedItemName = "name changed";

        // Invoke
        cartItem.setItemName("name changed");

        // Analyze
        assertEquals(expectedItemName, cartItem.getItemName());
    }

    @Test
    public void testSetItemAmountZero() {
        // Setup
        int id = 1;
        int itemAmount = 100;
        String itemName = "name";

        CartItem cartItem = new CartItem(id, itemName, itemAmount);

        int newItemAmount = -10;
        int expectedItemAmount = 0;

        // Invoke
        cartItem.setItemAmount(newItemAmount);

        // Analyze
        assertEquals(expectedItemAmount, cartItem.getItemAmount());
    }

    @Test
    public void testIncrement() {
        // Setup
        int id = 1;
        int itemAmount = 100;
        String itemName = "name";

        CartItem cartItem = new CartItem(id, itemName, itemAmount);

        int expectedItemAmount = 101;

        // Invoke
        cartItem.incrementItemAmount();

        // Analyze
        assertEquals(expectedItemAmount, cartItem.getItemAmount());
    }

    @Test
    public void testDecrement() {
        // Setup
        int id = 1;
        int itemAmount = 100;
        String itemName = "name";

        CartItem cartItem = new CartItem(id, itemName, itemAmount);

        int expectedItemAmount = 99;

        // Invoke
        cartItem.decrementItemAmount();

        // Analyze
        assertEquals(expectedItemAmount, cartItem.getItemAmount());
    }
}
