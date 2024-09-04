package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ufund.api.ufundapi.model.CartItem;
import com.ufund.api.ufundapi.model.Order;
import com.ufund.api.ufundapi.model.OrderItem;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.GiftDAO;
import com.ufund.api.ufundapi.persistence.UserDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the User Controller class
 * 
 * @author Howard Kong
 * @author Christopher Brooks
 */
@Tag("Controller-tier")
public class UserControllerTest {
    private UserController userController;
    private UserDAO mockUserDAO;
    private GiftDAO mockGiftDAO;

    /**
     * Before each test, create a new User Controller object and inject a mock User DAO
     */
    @BeforeEach
    public void setupUserController() {
        mockUserDAO = mock(UserDAO.class);
        userController = new UserController(mockUserDAO, mockGiftDAO);
    }

    @Test
    public void testGetUser() throws IOException {
        // Setup
        User user = new User(99, "Howard", "password123", null, null, null);
        // When the same id is passed in, our mock User DAO will return the User object
        when(mockUserDAO.getUser(user.getEmail())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getEmail());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUserNotFound() throws Exception {
        // Setup
        String userEmail = "doesnt exist";
        // When the same id is passed in, our mock User DAO will return null, simulating no User found
        when(mockUserDAO.getUser(userEmail)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.getUser(userEmail);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetUserHandleException() throws Exception {
        String userEmail = "doesnt exist";
        // When getUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUser(userEmail);

        // Invoke
        ResponseEntity<User> response = userController.getUser(userEmail);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateUser() throws IOException {
        // Setup
        User user = new User(99, "Aiden", "iloveswen261", null, null, null);
        // when createUser is called, return true simulating successful creation and save
        when(mockUserDAO.createUser(user)).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testCreateUserFailed() throws IOException {
        // Setup
        User user = new User(99, "N", "black&white", null, null, null);
        // when createUser is called, return false simulating failed creation and save
        when(mockUserDAO.createUser(user)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateUserHandleException() throws IOException {
        // Setup
        User user = new User(99, "Applejack", "mlp", null, null, null);
        // When createUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).createUser(user);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateUser() throws IOException {
        // Setup
        User user = new User(99, "Howard", "password123", null, null, null);
        // when updateUser is called, return true simulating successful update and save
        when(mockUserDAO.updateUser(user)).thenReturn(user);
        ResponseEntity<User> response = userController.updateUser(user);
        user.setUsername("Kong");

        // Invoke
        response = userController.updateUser(user);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testUpdateUserFailed() throws IOException {
        // Setup
        User user = new User(99, "Daniel", "----", null, null, null);
        // when updateUser is called, return null simulating unsuccessful update and save
        when(mockUserDAO.updateUser(user)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.updateUser(user);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateUserHandleException() throws IOException {
        // Setup
        User user = new User(99, "Howard", "password123", null, null, null);
        // When updateUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).updateUser(user);

        // Invoke
        ResponseEntity<User> response = userController.updateUser(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetUsers() throws IOException {
        // Setup
        User[] users = new User[2];
        users[0] = new User(99, "Howard", "password123", null, null, null);
        users[1] = new User(100, "Kevin", "writing", null, null, null);
        // When getUsers is called, return the Users created above
        when(mockUserDAO.getUsers()).thenReturn(users);

        // Invoke
        ResponseEntity<User[]> response = userController.getAllUsers();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testGetUsersHandleException() throws IOException {
        // Setup
        // When getUsers is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUsers();

        // Invoke
        ResponseEntity<User[]> response = userController.getAllUsers();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchUsers() throws IOException {
        // Setup
        String searchString = "la";
        User[] users = new User[2];
        users[0] = new User(99, "Howard", "password123", null, null, null);
        users[1] = new User(100, "Kevin", "writing", null, null, null);
        // When findUsers is called with the search string, return the two Users above
        when(mockUserDAO.findUsers(searchString)).thenReturn(users);

        // Invoke
        ResponseEntity<User[]> response = userController.searchUsers(searchString);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testSearchUsersHandleException() throws IOException {
        // Setup
        String searchString = "an";
        // When findUsers is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).findUsers(searchString);

        // Invoke
        ResponseEntity<User[]> response = userController.searchUsers(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteUser() throws IOException {
        // Setup
        String userEmail = "doesnt exist";
        // when deleteUser is called return true, simulating successful deletion
        when(mockUserDAO.deleteUser(userEmail)).thenReturn(true);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(userEmail);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() throws IOException {
        // Setup
        String userEmail = "doesnt exist";
        // when deleteUser is called return false, simulating failed deletion
        when(mockUserDAO.deleteUser(userEmail)).thenReturn(false);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(userEmail);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteUserHandleException() throws IOException {
        // Setup
        String userEmail = "doesnt exist";
        // When deleteUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).deleteUser(userEmail);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(userEmail);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void addItemUserCartNewItem() throws IOException {

        // Setup
        String email = "manager@manage.com";
        CartItem newItem = new CartItem(1, "item", 1);
        List<CartItem> cart = new ArrayList<>();
        
        User user = new User(99, "manager", "manager", email, cart, null);
        when(mockUserDAO.getUser(email)).thenReturn(user);
        when(mockUserDAO.updateUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Invoke
        ResponseEntity<User> response = userController.addItemUserCart(email, newItem);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void addItemUserCartExistingItem() throws IOException {
        
        // Test for adding an existing item in the cart. It should increase the number of that item.
        // Setup
        String email = "manager@manage.com";
        CartItem existingItem = new CartItem(1, "existing item", 5); // 5 items
        List<CartItem> cart = new ArrayList<>();
        cart.add(existingItem);

        User user = new User(99, "manager", "manager", email, cart, null);
        when(mockUserDAO.getUser(email)).thenReturn(user);
        when(mockUserDAO.updateUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartItem addItem = new CartItem(1, "existing item", 1);

        // Invoke
        ResponseEntity<User> response = userController.addItemUserCart(email, addItem);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User responseBody = response.getBody();
        CartItem updatedItem = responseBody.getCart().get(0);
        assertEquals(6, updatedItem.getItemAmount()); // 5 + 1 items added should be 6.
    }

    @Test
    public void testGetUserOrders() throws IOException {
        // Setup
        String email = "manager@manage.com";
        User user = new User(1, "manager", "manager", email, null, null);

        // Convoluted? Add items to an OrderItem list, create a new Order with those OrderItems, then set that to the user.
        List<OrderItem> itemsToOrder = new ArrayList<>();
        itemsToOrder.add(new OrderItem(1, "",15));
        itemsToOrder.add(new OrderItem(2, "",15));
        Order testOrder = new Order(LocalDateTime.now(), itemsToOrder);
        List<Order> orderList = new ArrayList<>();
        orderList.add(testOrder);

        user.setOrders(orderList);
        
        when(mockUserDAO.getUser(email)).thenReturn(user);

        // Invoke
        ResponseEntity<Order[]> response = userController.getUserOrders(email);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUserCheckout() throws IOException {
        // Setup
        String email = "manager@manage.com";

        // Add some test items to the cart
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1, "Laptop", 1));
        cartItems.add(new CartItem(2, "Pencil", 1));
        List<Order> emptyOrdersList = new ArrayList<>();

        User user = new User(1, "manager", "manager", email, cartItems, emptyOrdersList);
        when(mockUserDAO.getUser(email)).thenReturn(user);
        when(mockUserDAO.updateUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Invoke
        ResponseEntity<Order> response = userController.userCheckout(email);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    } 
    @Test
    public void testRemoveItemUserCart() throws IOException {

        // Setup
        String email = "manager@manage.com";
        CartItem item = new CartItem(1, "item", 1);
        List<CartItem> cart = new ArrayList<>();
        User user = new User(99, "manager", "manager", email, cart, null);
        
        when(mockUserDAO.getUser(email)).thenReturn(user);
        when(mockUserDAO.updateUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // First add an item to the cart
        userController.addItemUserCart(email, item);

        // Then test removing it
        // Invoke
        ResponseEntity<User> response = userController.removeItemUserCart(email, item);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetUserCart() throws IOException {

        // Setup
        String email = "manager@manage.com";
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1, "Laptop", 1));
        cartItems.add(new CartItem(2, "Pencil", 1));

        User user = new User(1, "manager", "manager", email, cartItems, null);
        when(mockUserDAO.getUser(email)).thenReturn(user);

        // Invoke
        ResponseEntity<CartItem[]> response = userController.getUserCart(email);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
