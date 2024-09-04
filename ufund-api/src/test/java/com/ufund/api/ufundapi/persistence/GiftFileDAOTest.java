package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Gift;
import com.ufund.api.ufundapi.model.Priority;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Gift File DAO class
 * 
 * @author Howard Kong
 * @author Christopher Brooks
 */
@Tag("Persistence-tier")
public class GiftFileDAOTest {
    GiftFileDAO giftFileDAO;
    Gift[] testGifts;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupGiftFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testGifts = new Gift[3];
        testGifts[0] = new Gift(99, "Wifi Routers", "Internet", 150.99, Priority.getPriority(4), 50);
        testGifts[1] = new Gift(100, "Tables", "To work on", 30.00, Priority.getPriority(3), 100);
        testGifts[2] = new Gift(101, "Projectors", "To display screens", 350.99, Priority.getPriority(2), 75);

        // When the object mapper is supposed to read from the file the mock object mapper will return the gift array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"), Gift[].class))
                .thenReturn(testGifts);
        giftFileDAO = new GiftFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetGifts() throws IOException {
        // Invoke
        Gift[] gifts = giftFileDAO.getGifts();

        // Analyze
        assertEquals(gifts.length, testGifts.length);
        for (int i = 0; i < testGifts.length; ++i)
            assertEquals(gifts[i], testGifts[i]);
    }

    @Test
    public void testFindGifts() throws IOException {
        // Invoke
        Gift[] gifts = giftFileDAO.findGifts("rs");

        // Analyze
        assertEquals(gifts.length,2);
        assertEquals(gifts[0], testGifts[0]);
        assertEquals(gifts[1], testGifts[2]);
    }

    @Test
    public void testGetGift() throws IOException {
        // Invoke
        Gift gift = giftFileDAO.getGift(99);

        // Analzye
        assertEquals(gift, testGifts[0]);
    }

    @Test
    public void testDeleteGift() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> giftFileDAO.deleteGift(99),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result, true);
        // We check the internal tree map size against the length
        // of the test gifts array - 1 (because of the delete)
        // Because gifts attribute of GiftFileDAO is package private
        // we can access it directly
        assertEquals(giftFileDAO.gifts.size(), testGifts.length-1);
    }

    @Test
    public void testCreateGift() throws IOException {
        // Setup
        Gift gift = new Gift(102, "Computers", "Labs", 250.00, Priority.getPriority(3), 500);

        // Invoke
        Gift result = assertDoesNotThrow(() -> giftFileDAO.createGift(gift),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Gift actual = giftFileDAO.getGift(gift.getId());
        assertEquals(actual.getId(), gift.getId());
        assertEquals(actual.getName(), gift.getName());
    }

    @Test
    public void testUpdateGift() throws IOException {
        // Setup
        Gift gift = new Gift(99, "Chairs", "To sit on", 10.99, Priority.getPriority(4), 250);

        // Invoke
        Gift result = assertDoesNotThrow(() -> giftFileDAO.updateGift(gift),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Gift actual = giftFileDAO.getGift(gift.getId());
        assertEquals(actual, gift);
    }

    @Test
    public void testSaveException() throws IOException {
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class), any(Gift[].class));

        Gift gift = new Gift(102, "Water Fountains", "Clean drinking water", 1000.00, Priority.getPriority(4), 50);

        assertThrows(IOException.class,
                        () -> giftFileDAO.createGift(gift),
                        "IOException not thrown");
    }

    @Test
    public void testGetGiftNotFound() throws IOException {
        // Invoke
        Gift gift = giftFileDAO.getGift(98);

        // Analyze
        assertEquals(gift, null);
    }

    @Test
    public void testDeleteGiftNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> giftFileDAO.deleteGift(98),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result, false);
        assertEquals(giftFileDAO.gifts.size(), testGifts.length);
    }

    @Test
    public void testUpdateGiftNotFound() {
        // Setup
        Gift gift = new Gift(98, "Food", "Need to eat", 0.99, Priority.getPriority(4), 10000);

        // Invoke
        Gift result = assertDoesNotThrow(() -> giftFileDAO.updateGift(gift),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization into Java objects
        
        // When the Mock Object Mapper readValue method is called
        // from the GiftFileDAO load method, an IOException is raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"), Gift[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new GiftFileDAO("doesnt_matter.txt", mockObjectMapper),
                        "IOException not thrown");
    }

    @Test
    public void testGetSortedGiftsLow() throws IOException{
        // Setup
        Gift[] sortedGifts = giftFileDAO.getSortedGifts("lowest");

        // Assert
        // Should be in this order: Projectors, Tables, Wifi Routers. Based on Priority
        assertEquals(testGifts[2], sortedGifts[0]);
        assertEquals(testGifts[1], sortedGifts[1]);
        assertEquals(testGifts[0], sortedGifts[2]);
    }

    @Test
    public void testGetSortedGiftsHigh() throws IOException{
        // Setup
        Gift[] sortedGifts = giftFileDAO.getSortedGifts("highest"); // doesn't matter what this is. Only checks if string == 'lowest'

        // Assert
        // Should be in this order: Wifi Routers, Tables, Projectors. Based on Priority
        assertEquals(testGifts[0], sortedGifts[0]);
        assertEquals(testGifts[1], sortedGifts[1]);
        assertEquals(testGifts[2], sortedGifts[2]);
    }
}
