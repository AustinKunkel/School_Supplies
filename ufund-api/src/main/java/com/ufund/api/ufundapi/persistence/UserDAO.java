package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;

import com.ufund.api.ufundapi.model.Order;
import com.ufund.api.ufundapi.model.OrderItem;
import com.ufund.api.ufundapi.model.User;

/**
 * Interface for User object persistence
 * 
 * @author Howard Kong
 * @author
 * @author
 * @author
 */
public interface UserDAO {
    /**
     * Retrieve a {@linkplain User user} with the given id
     * 
     * @param id The id of the {@link User user} to get
     * @return a {@link User user} object with the matching id
     *         null if no {@link User user} with a matching id is found
     * @throws IOException if an issue with underlying storage
     */
    User getUser(int id) throws IOException;

    /**
     * Retrieve a {@linkplain User user} with the given email
     * 
     * @param id The email of the {@link User user} to get
     * @return a {@link User user} object with the matching email
     *         null if no {@link User user} with a matching email is found
     * @throws IOException if an issue with underlying storage
     */
    User getUser(String email) throws IOException;

    /**
     * Retrieve all {@linkplain User users}
     * 
     * @return An array of {@link User user} objects, may be empty
     * @throws IOException if an issue with underlying storage
     */
    User[] getUsers() throws IOException;
    
    /**
     * Find all {@linkplain User users} whose name contains the given text
     * 
     * @param containsText The text to match against
     * @return An array of {@link User users} whose names contains the given text, may be empty
     * @throws IOException if an issue with underlying storage
     */
    User[] findUsers(String containsText) throws IOException;

    /**
     * Create and save a {@linkplain User user}
     * 
     * @param user {@linkplain User user} object to be created and saved
     *             The id of the user object is ignored and a new uniqe id is assigned
     * @return new {@link User user} if successful
     *         false otherwise
     * @throws IOException if an issue with underlying storage
     */
    User createUser(User user) throws IOException;

    /**
     * Update and save a {@linkplain User user}
     * 
     * @param {@link User user} object to be updated and saved
     * @return updated {@link User user} if successful
     *         null if {@link User user} could not be found
     * @throws IOException if underlying storage cannot be accessed
     */
    User updateUser(User user) throws IOException;

    /**
     * Delete a {@linkplain User user} with the given id
     * 
     * @param id The id of the {@link User user}
     * @return true if the {@link User user} was deleted
     *         false if user with the given id does not exist
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteUser(String email) throws IOException;

    /**
     * Get a total of the user's contribution
     * @param orders the orders that the user has
     * @return the double of total of the user's contribution
     * @throws IOException
     */
    Double getUserTotalContributed(List<Order> orders, GiftDAO giftDAO) throws IOException;


}
