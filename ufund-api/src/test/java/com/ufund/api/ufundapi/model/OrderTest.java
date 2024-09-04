package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Order class
 * 
 * @author Christopher Brooks
 */
@Tag("Model-tier")

// OrderTest fix to not rely on fixed values, now should work for any OrderID.
public class OrderTest {
    
    @Test
    public void testGetOrderID(){
        // Setup

        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        Order orderTest = new Order(LocalDateTime.now(), orderItems);
        Order orderTest2 = new Order(LocalDateTime.now(), orderItems);

        // Invoke
        int orderID = orderTest.getOrderId();
        int orderID2 = orderTest2.getOrderId();
        // Each time you create a new order, it's given an ID incremented by 1, starting from 0.

        // Analyze
        // Test if the first order is one greater than the previous, showing it incremented by one.
        assertEquals(orderID + 1, orderID2);
        
        // Test if orderis is greater then previous
        boolean result = orderID2 > orderID; 
        assertTrue(result); 
    }

    @Test
    public void testGetOrderDate() {
        // Setup
        LocalDateTime time = LocalDateTime.now();
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        Order orderTest = new Order(time, orderItems);

        // Invoke
        LocalDateTime orderTime = orderTest.getOrderDate();

        // Analyze
        assertEquals(time, orderTime);
    }

    @Test
    public void testSetOrderDate() {
        // Setup
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime timeRandom = LocalDateTime.of(2000, 1 ,1, 1, 1 ,1); // Set the order date to something random
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        Order orderTest = new Order(timeRandom, orderItems);

        // Invoke
        orderTest.setOrderDate(timeNow); // change to timeNow

        // Analyze
        assertEquals(timeNow, orderTest.getOrderDate());
    }

    @Test
    public void testSetOrderItems() {
        // Setup

        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        orderItems.add(new OrderItem(1,"", 1));
        Order orderTest = new Order(LocalDateTime.now(), orderItems);

        // Invoke
        orderTest.setOrderItems(orderItems);

        // Analyze
        assertEquals(orderItems, orderTest.getOrderItems());
    }
}