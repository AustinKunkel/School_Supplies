package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Gift;
import com.ufund.api.ufundapi.model.Order;
import com.ufund.api.ufundapi.model.User;

/**
 * Interface for Gift item object persistence
 * 
 * @author Howard Kong
 * @author
 * @author
 * @author
 */
public interface GiftDAO {
    /**
     * Retrieve a {@linkplain Gift gift} with the given id
     * 
     * @param id The id of the {@link Gift gift} to get
     * @return a {@link Gift gift} object with the matching id
     *         null if no {@link Gift gift} with a matching id is found
     * @throws IOException if an issue with underlying storage
     */
    Gift getGift(int id) throws IOException;

    /**
     * Retrieve all {@linkplain Gift gifts}
     * 
     * @return An array of {@link Gift gift} objects, may be empty
     * @throws IOException if an issue with underlying storage
     */
    Gift[] getGifts() throws IOException;

    /**
     * Retrieve all {@linkplain Gift gifts} sorted in a particular order
     * 
     * @param sort How gifts should be sorted
     * @return An array of {@link Gift gifts} sorted, may be empty
     * @throws IOException if an issue with underlying storage
     */
    Gift[] getSortedGifts(String sort) throws IOException;

    /**
     * Find all {@linkplain Gift gifts} whose name contains the given text
     * 
     * @param containsText The text to match against
     * @return An array of {@link Gift gifts} whose names contains the given text, may be empty
     * @throws IOException if an issue with underlying storage
     */
    Gift[] findGifts(String containsText) throws IOException;

    /**
     * Find all {@linkplain Gift gifts} whose priority matches the given priority number
     * 
     * @param priorityNumber The number of the priority
     * @return An array of {@link Gift gifts} whose priority matches the given number, may be empty
     * @throws IOException if an issue with underlying storage
     */
    Gift[] findGifts(int priorityNumber) throws IOException;

    /**
     * Create and save a {@linkplain Gift gift}
     * 
     * @param gift {@linkplain Gift gift} object to be created and saved
     *             The id of the gift object is ignored and a new uniqe id is assigned
     * @return new {@link Gift gift} if successful
     *         false otherwise 
     * @throws IOException if an issue with underlying storage
     */
    Gift createGift(Gift gift) throws IOException;

    /**
     * Update and save a {@linkplain Gift gift}
     * 
     * @param {@link Gift gift} object to be updated and saved
     * @return updated {@link Gift gift} if successful
     *         null if {@link Gift gift} could not be found
     * @throws IOException if underlying storage cannot be accessed
     */
    Gift updateGift(Gift item) throws IOException;

    /**
     * Delete a {@linkplain Gift gift} with the given id
     * 
     * @param id The id of the {@link Gift gift}
     * @return true if the {@link Gift gift} was deleted
     *         false if gift with the given id does not exist
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteGift(int id) throws IOException;

    /**
     * 
     * @param id id of gift to be removed
     * @param amount amount of gift to be removed
     * @return true if gift successfully removed
     * false otherwise
     * 
     * @throws IOException if storage cannot be accessed
     */
    boolean removeGiftQuantity(int id, int amount) throws IOException;
}
