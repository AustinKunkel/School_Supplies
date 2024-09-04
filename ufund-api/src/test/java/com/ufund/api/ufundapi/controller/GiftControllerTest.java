package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Gift;
import com.ufund.api.ufundapi.model.Priority;
import com.ufund.api.ufundapi.persistence.GiftDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Gift Controller class
 * 
 * @author Howard Kong
 */
@Tag("Controller-tier")
public class GiftControllerTest {
    private GiftController giftController;
    private GiftDAO mockGiftDAO;

    /**
     * Before each test, create a new Gift Controller object and inject a mock Gift DAO
     */
    @BeforeEach
    public void setupGiftController() {
        mockGiftDAO = mock(GiftDAO.class);
        giftController = new GiftController(mockGiftDAO);
    }

    @Test
    public void testGetGift() throws IOException {
        // Setup
        Gift gift = new Gift(99, "Backpacks", "A handy tool to store materials", 100.00, Priority.MID, 100);
        // When the same id is passed in, our mock Gift DAO will return the Gift object
        when(mockGiftDAO.getGift(gift.getId())).thenReturn(gift);

        // Invoke
        ResponseEntity<Gift> response = giftController.getGift(gift.getId());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gift, response.getBody());
    }

    @Test
    public void testGetGiftNotFound() throws Exception {
        // Setup
        int giftId = 99;
        // When the same id is passed in, our mock Gift DAO will return null, simulating no gift found
        when(mockGiftDAO.getGift(giftId)).thenReturn(null);

        // Invoke
        ResponseEntity<Gift> response = giftController.getGift(giftId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetGiftHandleException() throws Exception {
        int giftId = 99;
        // When getGift is called on the Mock Gift DAO, throw an IOException
        doThrow(new IOException()).when(mockGiftDAO).getGift(giftId);

        // Invoke
        ResponseEntity<Gift> response = giftController.getGift(giftId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateGift() throws IOException {
        // Setup
        Gift gift = new Gift(99, "Wifi Routers", "Internet access is essential for providing quality education and accessing resources", 150.99, Priority.MID, 500);
        // when createGift is called, return true simulating successful creation and save
        when(mockGiftDAO.createGift(gift)).thenReturn(gift);

        // Invoke
        ResponseEntity<Gift> response = giftController.createGift(gift);

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(gift, response.getBody());
    }

    @Test
    public void testCreateGiftFailed() throws IOException {
        // Setup
        Gift gift = new Gift(99, "Notebooks", "A handy tool to jot down notes", 1.50, Priority.MID, 100);
        // when createGift is called, return false simulating failed creation and save
        when(mockGiftDAO.createGift(gift)).thenReturn(null);

        // Invoke
        ResponseEntity<Gift> response = giftController.createGift(gift);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateGiftHandleException() throws IOException {
        // Setup
        Gift gift = new Gift(99, "Dance Studio Faculty", "Children need to exercise and explore their creative and artistic sides", 2500.00, Priority.MID, 10);
        // When createGift is called on the Mock Gift DAO, throw an IOException
        doThrow(new IOException()).when(mockGiftDAO).createGift(gift);

        // Invoke
        ResponseEntity<Gift> response = giftController.createGift(gift);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateGift() throws IOException {
        // Setup
        Gift gift = new Gift(99, "Wifi Routers", "Internet access is essential for providing quality education and accessing resources", 150.99, Priority.MID, 500);
        // when updateGift is called, return true simulating successful update and save
        when(mockGiftDAO.updateGift(gift)).thenReturn(gift);
        ResponseEntity<Gift> response = giftController.updateGift(gift);
        gift.setName("Ethernet Cables");

        // Invoke
        response = giftController.updateGift(gift);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gift, response.getBody());
    }

    @Test
    public void testUpdateGiftFailed() throws IOException {
        // Setup
        Gift gift = new Gift(99, "Playground Court", "Children need a place to social and play around", 1000.00, Priority.MID, 30);
        // when updateGift is called, return null simulating unsuccessful update and save
        when(mockGiftDAO.updateGift(gift)).thenReturn(null);

        // Invoke
        ResponseEntity<Gift> response = giftController.updateGift(gift);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateGiftHandleException() throws IOException {
        // Setup
        Gift gift = new Gift(99, "Playground Court", "Children need a place to social and play around", 1000.00, Priority.MID, 30);
        // When updateGift is called on the Mock Gift DAO, throw an IOException
        doThrow(new IOException()).when(mockGiftDAO).updateGift(gift);

        // Invoke
        ResponseEntity<Gift> response = giftController.updateGift(gift);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetGifts() throws IOException {
        // Setup
        Gift[] gifts = new Gift[2];
        gifts[0] = new Gift(99, "Folders", "A handy tool for storing papers", 3.99, Priority.MID, 1000);
        gifts[1] = new Gift(100, "Colored Pencils", "Children need to explore their artistic sides", 54.99, Priority.MID, 100);
        // When getGifts is called, return the gifts created above
        when(mockGiftDAO.getGifts()).thenReturn(gifts);

        // Invoke
        ResponseEntity<Gift[]> response = giftController.getAllGifts();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gifts, response.getBody());
    }

    @Test
    public void testGetGiftsHandleException() throws IOException {
        // Setup
        // When getGifts is called on the Mock Gift DAO, throw an IOException
        doThrow(new IOException()).when(mockGiftDAO).getGifts();

        // Invoke
        ResponseEntity<Gift[]> response = giftController.getAllGifts();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchGifts() throws IOException {
        // Setup
        String searchString = "e";
        Gift[] gifts = new Gift[2];
        gifts[0] = new Gift(99, "Folders", "A handy tool for storing papers", 3.99, Priority.MID, 1000);
        gifts[1] = new Gift(100, "Colored Pencils", "Children need to explore their artistic sides", 54.99, Priority.MID, 100);
        // When findGifts is called with the search string, return the two gifts above
        when(mockGiftDAO.findGifts(searchString)).thenReturn(gifts);

        // Invoke
        ResponseEntity<Gift[]> response = giftController.searchGifts(searchString);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gifts, response.getBody());
    }

    @Test
    public void testSearchGiftsHandleException() throws IOException {
        // Setup
        String searchString = "an";
        // When findGifts is called on the Mock Gift DAO, throw an IOException
        doThrow(new IOException()).when(mockGiftDAO).findGifts(searchString);

        // Invoke
        ResponseEntity<Gift[]> response = giftController.searchGifts(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchGiftsPriority() throws IOException {
        // Setup
        int priorityNumber = 3;
        Gift[] gifts = new Gift[2];
        gifts[0] = new Gift(99, "Folders", "A handy tool for storing papers", 3.99, Priority.MID, 1000);
        gifts[1] = new Gift(100, "Colored Pencils", "Children need to explore their artistic sides", 54.99, Priority.MID, 100);
        // When findGifts is called with the search string, return the two gifts above
        when(mockGiftDAO.findGifts(priorityNumber)).thenReturn(gifts);

        // Invoke
        ResponseEntity<Gift[]> response = giftController.searchGifts(priorityNumber);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gifts, response.getBody());
    }

    @Test
    public void testSearchGiftsPriorityHandleException() throws IOException {
        // Setup
        int priorityNumber = 1;
        // When findGifts is called on the Mock Gift DAO, throw an IOException
        doThrow(new IOException()).when(mockGiftDAO).findGifts(priorityNumber);

        // Invoke
        ResponseEntity<Gift[]> response = giftController.searchGifts(priorityNumber);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetAllGiftsByPriorityHighest() throws IOException {
        // Setup
        String sortPriority = "highest";
        Gift[] gifts = new Gift[2];
        gifts[0] = new Gift(99, "Folders", "A handy tool for storing papers", 3.99, Priority.MID, 1000);
        gifts[1] = new Gift(100, "Colored Pencils", "Children need to explore their artistic sides", 54.99, Priority.MID, 100);
        // When getSortedGifts is called, return the two gifts above
        when(mockGiftDAO.getSortedGifts(sortPriority)).thenReturn(gifts);

        // Invoke
        ResponseEntity<Gift[]> response = giftController.getAllGiftsByPriority(sortPriority);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gifts, response.getBody());
    }

    @Test
    public void testGetAllGiftsByPriorityLowest() throws IOException {
        // Setup
        String sortPriority = "lowest";
        Gift[] gifts = new Gift[2];
        gifts[0] = new Gift(99, "Folders", "A handy tool for storing papers", 3.99, Priority.MID, 1000);
        gifts[1] = new Gift(100, "Colored Pencils", "Children need to explore their artistic sides", 54.99, Priority.MID, 100);
        // When getSortedGifts is called, return the two gifts above
        when(mockGiftDAO.getSortedGifts(sortPriority)).thenReturn(gifts);

        // Invoke
        ResponseEntity<Gift[]> response = giftController.getAllGiftsByPriority(sortPriority);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gifts, response.getBody());
    }

    @Test
    public void testGetAllGiftsByPriorityHandleBadRequest() throws IOException {
        // Setup
        String sortPriority = "best";
        Gift[] gifts = new Gift[2];
        gifts[0] = new Gift(99, "Folders", "A handy tool for storing papers", 3.99, Priority.MID, 1000);
        gifts[1] = new Gift(100, "Colored Pencils", "Children need to explore their artistic sides", 54.99, Priority.MID, 100);

        // Invoke
        ResponseEntity<Gift[]> response = giftController.getAllGiftsByPriority(sortPriority);

        // Analyze
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetAllGiftsByPriorityHandleException() throws IOException {
        // Setup
        String sortPriority = "lowest";
        Gift[] gifts = new Gift[2];
        gifts[0] = new Gift(99, "Folders", "A handy tool for storing papers", 3.99, Priority.MID, 1000);
        gifts[1] = new Gift(100, "Colored Pencils", "Children need to explore their artistic sides", 54.99, Priority.MID, 100);
        // When getSortedGifts is called, throw an IOException
        doThrow(new IOException()).when(mockGiftDAO).getSortedGifts(sortPriority);

        // Invoke
        ResponseEntity<Gift[]> response = giftController.getAllGiftsByPriority(sortPriority);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteGift() throws IOException {
        // Setup
        int giftId = 99;
        // when deleteGift is called return true, simulating successful deletion
        when(mockGiftDAO.deleteGift(giftId)).thenReturn(true);

        // Invoke
        ResponseEntity<Gift> response = giftController.deleteGift(giftId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteGiftNotFound() throws IOException {
        // Setup
        int giftId = 99;
        // when deleteGift is called return false, simulating failed deletion
        when(mockGiftDAO.deleteGift(giftId)).thenReturn(false);

        // Invoke
        ResponseEntity<Gift> response = giftController.deleteGift(giftId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteGiftHandleException() throws IOException {
        // Setup
        int giftId = 99;
        // When deleteGift is called on the Mock Gift DAO, throw an IOException
        doThrow(new IOException()).when(mockGiftDAO).deleteGift(giftId);

        // Invoke
        ResponseEntity<Gift> response = giftController.deleteGift(giftId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
