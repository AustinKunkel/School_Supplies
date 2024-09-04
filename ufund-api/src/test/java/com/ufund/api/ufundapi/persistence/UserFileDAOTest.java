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
import com.ufund.api.ufundapi.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the User File DAO class
 * 
 * @author Howard Kong
 */
@Tag("Persistence-tier")
public class UserFileDAOTest {
    /**
     * TODO
     * Fix methods or tests
     */


    UserFileDAO userFileDAO;
    User[] testUsers;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupUserFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testUsers = new User[3];
        testUsers[0] = new User(99, "manager", "manager", "manager@google", null, null);
        testUsers[1] = new User(100, "Howard", "iloveswen261", "howard@google", null, null);
        testUsers[2] = new User(101, "Taeyong", "bounce", "Taeyong@google", null, null);

        // When the object mapper is supposed to read from the file the mock object mapper will return the user array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"), User[].class))
                .thenReturn(testUsers);
        userFileDAO = new UserFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetUsers() throws IOException {
        // Invoke
        User[] users = userFileDAO.getUsers();

        // Analyze
        assertEquals(users.length, testUsers.length);
        // for (int i = 0; i < testUsers.length; ++i)
        //     assertEquals(users[i], testUsers[i]);
    }

    @Test
    public void testFindUsers() throws IOException {
        // Invoke
        User[] users = userFileDAO.findUsers("g");

        // Analyze
        assertEquals(users.length, 3);
        // assertEquals(users[0], testUsers[0]);
        // assertEquals(users[1], testUsers[1]);
        // assertEquals(users[2], testUsers[2]);
    }

    @Test
    public void testGetUser() throws IOException {
        // Invoke
        User user = userFileDAO.getUser("manager@google");

        // Analzye
        assertEquals(user, testUsers[0]);
    }

    @Test
    public void testDeleteUser() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser("manager@google"),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result, true);
        // We check the internal tree map size against the length
        // of the test users array - 1 (because of the delete)
        // Because users attribute of UserFileDAO is package private
        // we can access it directly
        assertEquals(userFileDAO.usersID.size(), testUsers.length-1);
        assertEquals(userFileDAO.usersEmail.size(), testUsers.length-1);
    }

    @Test
    public void testCreateUser() throws IOException {
        // Setup
        User user = new User(102, "COM", "coms1123", "coms@com.com", null, null);

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.createUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = userFileDAO.getUser(user.getEmail());
        assertEquals(actual.getId(), user.getId());
        assertEquals(actual.getUsername(), user.getUsername());
    }

    @Test
    public void testUpdateUser() throws IOException {
        // Setup
        User user = new User(99, "Chris", "chris", "manager@google", null, null);

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = userFileDAO.getUser(user.getEmail());
        assertEquals(actual, user);
    }

    @Test
    public void testSaveException() throws IOException {
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class), any(User[].class));

        User user = new User(102, "Dean", "dean", "dean@dean.com", null, null);

        assertThrows(IOException.class,
                        () -> userFileDAO.createUser(user),
                        "IOException not thrown");
    }

    @Test
    public void testGetUserNotFound() throws IOException {
        // Invoke
        User user = userFileDAO.getUser("DNE");

        // Analyze
        assertEquals(user, null);
    }

    @Test
    public void testDeleteUserNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser("DNE"),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result, false);
        assertEquals(userFileDAO.usersID.size(), testUsers.length);
        assertEquals(userFileDAO.usersEmail.size(), testUsers.length);
    }

    @Test
    public void testUpdateUserNotFound() {
        // Setup
        User user = new User(98, "User", "user", null, null, null);

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(user),
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
        // from the UserFileDAO load method, an IOException is raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"), User[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new UserFileDAO("doesnt_matter.txt", mockObjectMapper),
                        "IOException not thrown");
    }
}
