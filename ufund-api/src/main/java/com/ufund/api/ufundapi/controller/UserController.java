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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.model.CartItem;
import com.ufund.api.ufundapi.model.Order;
import com.ufund.api.ufundapi.model.OrderItem;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.GiftDAO;
import com.ufund.api.ufundapi.persistence.GiftFileDAO;
import com.ufund.api.ufundapi.persistence.UserDAO;

/**
 * Handles the REST API requests for the User resource
 * 
 * {@literal @}RestController Spring annotation identifies this class as a REST API method handler to the Spring framework
 * 
 * @author Howard Kong
 * @author Austin Kunkel
 * @author
 * @author
 */

@RestController
@RequestMapping("users")
public class UserController {

    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private UserDAO userDAO;
    private GiftDAO giftDAO;

    /**
     * Create a REST API controller to respond to requests
     * 
     * @param userDAO The {@link UserDAO User Data Access Object} to perform CRUD operations
     *                 This dependency is injected by the Spring Framework
     */
    public UserController(UserDAO userDAO, GiftDAO giftDAO){
        this.userDAO = userDAO;
        this.giftDAO = giftDAO;
    }

    /**
     * Respond to the GET request for a {@linkplain User user} for the given email
     * 
     * @param email The email used to locate the {@link User user}
     * @return ResponseEntity with {@link User user} object and HTTP status of OK if found
     *         ResponseEntity with HTTP status of NOT_FOUND if not found
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        LOG.info("GET /users/" + email);
        try {
            User user = userDAO.getUser(email);

            if (user != null)
                return new ResponseEntity<User>(user, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Respond to the GET request for all {@linkplain User users}
     * 
     * @return ResponseEntity with array of {@link User user} objects (may be empty) and HTTP status of OK
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<User[]> getAllUsers() {
        LOG.info("GET /users");

        try {
            User[] allUsers = userDAO.getUsers();
            return new ResponseEntity<User[]>(allUsers, HttpStatus.OK);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Respond to the GET request for all {@linkplain User users} whose name contains the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link User users}
     * @return ResponseEntity with array of {@link User user} objects (may be empty) and HTTP status of OK
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     *
     * Example: Find all users that contain the text "ma"
     * GET http://localhost:8080/users/?username=ma
     */
    @GetMapping("/")
    public ResponseEntity<User[]> searchUsers(@RequestParam String username) {
        LOG.info("GET /users/?username=" + username);

        try {
            username = username.toLowerCase();
            User[] matchingUsers = userDAO.findUsers(username);
            return new ResponseEntity<User[]>(matchingUsers, HttpStatus.OK);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create a {@linkplain User user} with the provided user object
     * 
     * @param user - The {@link User user} to create
     * @return ResponseEntity with created {@link User user} object and HTTP status of CREATED
     *         ResponseEntity with HTTP status of CONFLICT if {@link User user} object already exists
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        LOG.info("POST /users/" + user.getId());

        try {
            if (userDAO.getUser(user.getEmail()) != null) // check if there is a user with the email
                return new ResponseEntity<>(HttpStatus.CONFLICT);

            User newUser = userDAO.createUser(user);

            if (newUser != null)
                return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
            else
                return new ResponseEntity<User>(newUser, HttpStatus.CONFLICT);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update the {@linkplain User user} with the provided {@linkplain User user} object, if it exists
     * 
     * @param user The {@link User user} to update
     * @return ResponseEntity with updated {@link User user} object and HTTP status of OK if updated
     *         ResponseEntity with HTTP status of NOT_FOUND if not found
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        LOG.info("PUT /users/" + user.getId());

        try {
            User userWithEmail = userDAO.getUser(user.getEmail());

            if (userWithEmail == null) // check if there is a user with the email
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            
            //means that there is a conflict if emails are same but id's do not match
            if(userWithEmail.getId() != user.getId()) {
               return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            User updatedUser = userDAO.updateUser(user);

            if (updatedUser != null)
                return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a {@linkplain User user} with the given e,ail
     * 
     * @param id The id of the {@link User user} to delete
     * @return ResponseEntity HTTP status of OK if deleted
     *         ResponseEntity with HTTP status of NOT_FOUND if not found
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{email}")
    public ResponseEntity<User> deleteUser(@PathVariable String email) {
        LOG.info("DELETE /users/" + email);

        try {
            boolean deleted = userDAO.deleteUser(email);

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

    /**
     * Respond to the GET request for the cart of the {@linkplain User user}
     * 
     * @param email The email of the {@link User user}
     * @return ResponseEntity with array of {@link CartItem cartItem} objects (may be empty) and HTTP status of OK
     *         ResponseEntity with HTTP status of NOT_FOUND if not found
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{email}/cart")
    public ResponseEntity<CartItem[]> getUserCart(@PathVariable String email) {
        LOG.info("GET /users/" + email + "/cart");

        try {
            User user = userDAO.getUser(email);
            if (user == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            List<CartItem> userCart = user.getCart();
            CartItem[] cart = userCart.toArray(new CartItem[userCart.size()]);
            
            return new ResponseEntity<CartItem[]>(cart, HttpStatus.OK);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Add a {@linkplain CartItem cartItem} object into the cart of the {@linkplain User user} with the provided email, if it exists
     * 
     * @param email The email of the {@link User user} to update
     * @param cartItem The cart item to update for the {@link User user}
     * @return ResponseEntity with updated {@link User user} object and HTTP status of OK if updated
     *         ResponseEntity with HTTP status of NOT_FOUND if not found
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/{email}/cart")
    public ResponseEntity<User> addItemUserCart(@PathVariable String email, @RequestBody CartItem cartItem) {
        LOG.info("POST /users/" + email + "/cart/" + cartItem.getItemId());

        try {
            User user = userDAO.getUser(email);
            if (user == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            List<CartItem> userCart = user.getCart();

            if (userCart.contains(cartItem)) {
                int initialAmount = userCart.get(userCart.indexOf(cartItem)).getItemAmount();

                userCart.get(userCart.indexOf(cartItem)).setItemAmount(initialAmount + cartItem.getItemAmount());
            }
            else
                userCart.add(cartItem);

            User newUser = new User(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), userCart, user.getOrders());
            User updatedUser = userDAO.updateUser(newUser);

            if (updatedUser != null)
                return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
            else 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete the {@linkplain CartItem cartItem} object from the cart of the {@linkplain User user} with the provided email, if it exists
     * 
     * @param email The email of the {@link User user} to update
     * @param cartItem The cart item to update for the {@link User user}
     * @return ResponseEntity with updated {@link User user} object and HTTP status of OK if updated
     *         ResponseEntity with HTTP status of NOT_FOUND if not found
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/{email}/cart")
    public ResponseEntity<User> removeItemUserCart(@PathVariable String email, @RequestBody CartItem cartItem) {
        LOG.info("DELETE /users/" + email + "/cart/" + cartItem.getItemId());

        try {
            User user = userDAO.getUser(email);
            if (user == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            List<CartItem> userCart = user.getCart();

            if (userCart.contains(cartItem))
                userCart.remove(cartItem);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            User newUser = new User(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), userCart, user.getOrders());
            User updatedUser = userDAO.updateUser(newUser);

            if (updatedUser != null)
                return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
            else 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Respond to the GET request for the purchases of the {@linkplain User user}
     * 
     * @param email The email of the {@link User user}
     * @return ResponseEntity with array of {@link OrderItem purchasedItem} objects (may be empty) and HTTP status of OK
     *         ResponseEntity with HTTP status of NOT_FOUND if not found
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{email}/purchases")
    public ResponseEntity<Order[]> getUserOrders(@PathVariable String email) {
        LOG.info("GET /users/" + email + "/purchases");

        try {
            User user = userDAO.getUser(email);
            if (user == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            List<Order> userOrders = user.getOrders();
            Order[] orders = userOrders.toArray(new Order[userOrders.size()]);
            
            return new ResponseEntity<Order[]>(orders, HttpStatus.OK);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    

    /**
     * Respond to the GET request for the user's total contribution
     * @param email The email of the {@link User user}
     * @return ResponseEntity with array of {@link Integer} total (may be 0) and HTTP status of OK
     *         ResponseEntity with HTTP status of NOT_FOUND if not found
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{email}/purchases/total")
    public ResponseEntity<Double> getUserTotalContributed(@PathVariable String email) {
        LOG.info("GET /users/" + email + "/purchases/total");

        try {
            User user = userDAO.getUser(email);
            if (user == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            Double total = this.userDAO.getUserTotalContributed(user.getOrders(), giftDAO);
            
            return new ResponseEntity<Double>(total, HttpStatus.OK);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{email}/orders/{id}/{cost}")
    public ResponseEntity<Order> setOrderTotalCost(@PathVariable String email, 
                                                   @PathVariable int id, 
                                                   @PathVariable Double cost) {
        LOG.info("POST /users/order/set/" + email);

        try {
            if(email == null)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            if(email.equals(""))
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            User user = userDAO.getUser(email);

            //sets the user's order cost to the current cost
            user.getOrder(id).setOrderCost(cost);

            return new ResponseEntity<Order>(user.getOrder(id), HttpStatus.OK);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }   
    }

    /**
     * Checkout the cart of the {@linkplain User user} with the provided id and update the user's cart and purchases
     * 
     * @param email The email of the {@link User user} to checkout cart
     * @return ResponseEntity with updated {@link User user} object and HTTP status of OK if updated
     *         ResponseEntity with HTTP status of NOT_FOUND if user not found
     *         ResponseEntity with HTTP status of CONFLICT if cart is empty
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/{email}/cart/checkout")
    public ResponseEntity<Order> userCheckout(@PathVariable String email) {
        LOG.info("POST /users/" + email + "/cart/checkout");

        try {
            User user = userDAO.getUser(email);

            if (user == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            List<CartItem> userCart = user.getCart();
            if (userCart == null)
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            
            List<OrderItem> orderItems = new ArrayList<>(userCart.size());

            for (CartItem item : userCart) {

                giftDAO.removeGiftQuantity(item.getItemId(), item.getItemAmount());

                OrderItem newOrderItem = new OrderItem(item.getItemId(),item.getItemName(), item.getItemAmount());
                orderItems.add(newOrderItem);

            }
            user.clearCart();

            // Create new order and add it to a user's orders
            Order newOrder = new Order(LocalDateTime.now(), orderItems);

            user.getOrders().add(newOrder);

            return new ResponseEntity<Order>(newOrder, HttpStatus.OK);
        }
        catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
