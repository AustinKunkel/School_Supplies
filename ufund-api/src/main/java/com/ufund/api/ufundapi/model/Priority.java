package com.ufund.api.ufundapi.model;

/**
 * Represents a enumerated type/level of Priority associated with a number
 * 
 * @author Howard Kong
 */
public enum Priority {
    NONE(0),
    LOW(1),
    MID(2),
    HIGH(3),
    SEVERE(4);

    private int priorityNumber;

    /**
     * Assign priority number with each type
     * @param priorityNumber The priority number assigned
     */
    private Priority(int priorityNumber) {
        this.priorityNumber = priorityNumber;
    }

    /**
     * Retrieve the number of the enumerated type 
     * @return The number of the enumerated type
     */
    public int getNumber() {
        return priorityNumber;
    }

    /**
     * Get a priority level based on the priority number provided
     * @param priorityNumber
     * @return The Priority level associated with the number
     */
    public static Priority getPriority(int priorityNumber) {
        for (Priority priority : Priority.values()) {
            if (priority.getNumber() == priorityNumber)
                return priority;
        }

        return Priority.NONE;
    }
}
