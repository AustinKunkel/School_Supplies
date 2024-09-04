package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.model.Gift;
import com.ufund.api.ufundapi.model.Order;
import com.ufund.api.ufundapi.persistence.GiftDAO;

/**
 * Handles the REST API requests for the Gift resource
 * 
 * {@literal @}RestController Spring annotation identifies this class as a REST API method handler to the Spring framework
 * 
 * @author Howard Kong
 * @author Christopher Brooks
 * @author
 * @author
 */

@RestController
@RequestMapping("gifts")
public class GiftController {

    private static final Logger LOG = Logger.getLogger(GiftController.class.getName());
    private GiftDAO giftDAO;

    /**
     * Create a REST API controller to respond to requests
     * 
     * @param giftDAO The {@link GiftDAO Gift Data Access Object} to perform CRUD operations
     *                This dependency is injected by the Spring Framework
     */
    public GiftController(GiftDAO giftDAO){
        this.giftDAO = giftDAO;
    }

    /**
     * Respond to the GET request for a {@linkplain Gift gift} for the given id
     * 
     * @param id The id used to locate the {@link Gift gift}
     * @return ResponseEntity with {@link Gift gift} object and HTTP status of OK if found  
     *         ResponseEntity with HTTP status of NOT_FOUND if not found
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Gift> getGift(@PathVariable int id) {
        LOG.info("GET /gifts/" + id);
        try {
            Gift gift = giftDAO.getGift(id);

            if (gift != null)
                return new ResponseEntity<Gift>(gift, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Respond to the GET request for all {@linkplain Gift gifts}
     * 
     * @return ResponseEntity with array of {@link Gift gift} objects (may be empty) and HTTP status of OK
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Gift[]> getAllGifts() {
        LOG.info("GET /gifts");

        try{
            Gift[] allGifts = giftDAO.getGifts();
            return new ResponseEntity<Gift[]>(allGifts, HttpStatus.OK);
        }
        catch(IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Respond to the GET request for all {@linkplain Gift gifts} sorted by priority
     * 
     * @return ResponseEntity with array of {@link Gift gift} objects (may be empty) and HTTP status of OK
     *         ResponseEntity with HTTP status of BAD_REQUEST
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * 
     * Example: Get all gifts sorted by highest priority
     * GET http://localhost:8080/gifts/?sort=highest
     */
    @GetMapping(value="/", params="sort")
    public ResponseEntity<Gift[]> getAllGiftsByPriority(@RequestParam("sort") String sort) {
        LOG.info("GET /gifts");

        try{
            sort = sort.strip().toLowerCase();
            if (!sort.equals("highest") && !sort.equals("lowest"))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            Gift[] sortedGifts = giftDAO.getSortedGifts(sort);

            return new ResponseEntity<Gift[]>(sortedGifts, HttpStatus.OK);
        }
        catch(IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Respond to the GET request for all {@linkplain Gift gifts} whose name contains the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link Gift gifts}
     * @return ResponseEntity with array of {@link Gift gift} objects (may be empty) and HTTP status of OK
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     *
     * Example: Find all gifts that contain the text "ma"
     * GET http://localhost:8080/gifts/?name=ma
     */
    @GetMapping(value="/", params="name")
    public ResponseEntity<Gift[]> searchGifts(@RequestParam("name") String name) {
        LOG.info("GET /gifts/?name=" + name);

        try{
            Gift[] matchingGifts = giftDAO.findGifts(name);
            return new ResponseEntity<Gift[]>(matchingGifts, HttpStatus.OK);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Respond to the GET request for all {@linkplain Gift gifts} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link Gift gifts}
     * @return ResponseEntity with array of {@link Gift gift} objects (may be empty) and HTTP status of OK
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     *
     * Example: Find all gifts that of priority level 3 ("HIGH")
     * GET http://localhost:8080/gifts/?priority=3
     */
    @GetMapping(value="/", params="priority")
    public ResponseEntity<Gift[]> searchGifts(@RequestParam("priority") int priorityNumber) {
        LOG.info("GET /gifts/?priority=" + priorityNumber);

        try{
            Gift[] matchingGifts = giftDAO.findGifts(priorityNumber);
            return new ResponseEntity<Gift[]>(matchingGifts, HttpStatus.OK);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create a {@linkplain Gift gift} with the provided gift object
     * 
     * @param gift - The {@link Gift gift} to create
     * @return ResponseEntity with created {@link Gift gift} object and HTTP status of CREATED
     *         ResponseEntity with HTTP status of CONFLICT if {@link Gift gift} object already exists
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Gift> createGift(@RequestBody Gift gift) {
        LOG.info("POST /gifts/" + gift.getId());
        try {
            Gift newGift = giftDAO.createGift(gift);

            if (newGift != null)
                return new ResponseEntity<Gift>(newGift, HttpStatus.CREATED);
            else
                return new ResponseEntity<Gift>(newGift, HttpStatus.CONFLICT);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update the {@linkplain Gift gift} with the provided {@linkplain Gift gift} object, if it exists
     * 
     * @param gift The {@link Gift gift} to update
     * @return ResponseEntity with updated {@link Gift gift} object and HTTP status of OK if updated
     *         ResponseEntity with HTTP status of NOT_FOUND if not found
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Gift> updateGift(@RequestBody Gift gift) {
        LOG.info("PUT /gifts/" + gift.getId());
        try {
            Gift updatedGift = giftDAO.updateGift(gift);

            if (updatedGift != null)
                return new ResponseEntity<Gift>(updatedGift, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a {@linkplain Gift gift} with the given id
     * 
     * @param id The id of the {@link Gift gift} to delete
     * @return ResponseEntity HTTP status of OK if deleted
     *         ResponseEntity with HTTP status of NOT_FOUND if not found
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Gift> deleteGift(@PathVariable int id) {
        LOG.info("DELETE /gifts/" + id);

        try {
            boolean deleted = giftDAO.deleteGift(id);

            if (deleted == true)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
