package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the User class
 * 
 * @author Howard Kong
 */
@Tag("Model-tier")
public class UserTest {
    @Test
    public void testCreateUser() {
        // Setup
        int expectedId = 99;
        String expectedUsername = "User";
        String expectedPassword = "User";
        String expectedEmail = "user@gmail.com";
        List<CartItem> expectedCart = null;
        List<Order> expectedOrders = null;

        // Invoke
        User user = new User(expectedId, expectedUsername, expectedPassword, expectedEmail, expectedCart, expectedOrders);

        // Analyze
        assertNotEquals(user, null);
        assertEquals(expectedId, user.getId());
        assertEquals(expectedUsername, user.getUsername());
        assertEquals(expectedPassword, user.getPassword());
        assertEquals(expectedEmail, user.getEmail());
        assertEquals(expectedCart, user.getCart());
        assertEquals(expectedOrders, user.getOrders());
    }

    @Test
    public void testSetUsername() {
        // Setup
        int id = 99;
        String username = "User";
        String password = "User";
        String email = "user@gmail.com";
        List<CartItem> cart = null;
        List<Order> orders = null;
        User user = new User(id, username, password, email, cart, orders);

        String expectedUsername = "Username";

        // Invoke
        user.setUsername(expectedUsername);

        // Analyze
        assertEquals(expectedUsername, user.getUsername());
    }

    @Test
    public void testSetPassword() {
        // Setup
        int id = 99;
        String username = "User";
        String password = "User";
        String email = "user@gmail.com";
        List<CartItem> cart = null;
        List<Order> orders = null;
        User user = new User(id, username, password, email, cart, orders);

        String expectedPassword = "password";

        // Invoke
        user.setPassword(expectedPassword);

        // Analyze
        assertEquals(expectedPassword, user.getPassword());
    }

    @Test
    public void testSetEmail() {
        // Setup
        int id = 99;
        String username = "User";
        String password = "User";
        String email = "user@gmail.com";
        List<CartItem> cart = null;
        List<Order> orders = null;
        User user = new User(id, username, password, email, cart, orders);

        String expectedEmail = "user@yahoo.com";

        // Invoke
        user.setEmail(expectedEmail);

        // Analyze
        assertEquals(expectedEmail, user.getEmail());
    }

    @Test
    public void testSetCart() {
        // Setup
        int id = 99;
        String username = "User";
        String password = "User";
        String email = "user@gmail.com";
        List<CartItem> cart = null;
        List<Order> orders = null;
        User user = new User(id, username, password, email, cart, orders);

        List<CartItem> expectedCart = new ArrayList<>();
        expectedCart.add(new CartItem(1, "test", 100));

        // Invoke
        user.setCart(expectedCart);

        // Analyze
        assertEquals(expectedCart, user.getCart());
    }

    @Test
    public void testSetPurchases() {
        // Setup
        int id = 99;
        String username = "User";
        String password = "User";
        String email = "user@gmail.com";
        List<CartItem> cart = null;
        List<Order> orders = null;
        User user = new User(id, username, password, email, cart, orders);

        List<Order> expectedOrders = new ArrayList<>();
        List<OrderItem> expectedOrderItems = new ArrayList<>();

        expectedOrderItems.add(new OrderItem(2, "",50));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        expectedOrders.add(new Order(LocalDateTime.parse("2024-02-26 00:00:00", formatter), expectedOrderItems));

        // Invoke
        user.setOrders(expectedOrders);

        // Analyze
        assertEquals(expectedOrders, user.getOrders());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String username = "User";
        String password = "User";
        String email = "user@gmail.com";
        List<CartItem> cart = null;
        List<Order> orders = null;

        String expectedString = String.format(User.STRING_FORMAT, id, username, email);
        User user = new User(id, username, password, email, cart, orders);

        // Invoke
        String actualString = user.toString();

        // Analyze
        assertEquals(expectedString, actualString);
    }
}