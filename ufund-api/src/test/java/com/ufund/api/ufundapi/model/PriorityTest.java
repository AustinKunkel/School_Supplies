package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Priority class
 * 
 * @author Howard Kong
 * @author Austin Kunkel
 */
@Tag("Model-tier")
public class PriorityTest {
    @Test
    public void testPriority() {
        // Setup
        int expectedPriorityNumber = 2;
        Priority expectedPriority = Priority.MID;

        // Invoke
        Priority priority = Priority.getPriority(expectedPriorityNumber);
        int priorityNumber = priority.getNumber();

        // Analyze
        assertEquals(expectedPriority, priority);
        assertEquals(expectedPriorityNumber, priorityNumber);
    }
}
