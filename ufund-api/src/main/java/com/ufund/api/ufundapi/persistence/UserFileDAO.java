package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Gift;
import com.ufund.api.ufundapi.model.Order;
import com.ufund.api.ufundapi.model.OrderItem;
import com.ufund.api.ufundapi.model.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for Users
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Howard Kong
 * @author Austin Kunkel
 * @author
 * @author
 */
@Component
public class UserFileDAO implements UserDAO {
    
    private static final Logger LOG = Logger.getLogger(UserFileDAO.class.getName());

    // Provides a local cache of the user object so that we don't need to read from the file each time
    Map<Integer, User> usersID;
    Map<String, User> usersEmail;

    private ObjectMapper objectMapper;  // Provides conversion between User objects and JSON text format written to the file
    private static int nextId;  // The next Id to assign to a new user
    private String filename;    // Filename to read from and write to

     /**
     * Create a User File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * @throws IOException when file cannot be accessed or read from
     */
    public UserFileDAO(@Value("${users.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the users from the file
    }

    /**
     * Generate the next id for a new {@linkplain User user}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generate an array of {@linkplain User users} from the tree map
     * 
     * @return  The array of {@link User users}, may be empty
     */
    private User[] getUsersArray() {
        return getUsersArray(null);
    }

    /**
     * Generate an array of {@linkplain User users} from the tree map for any
     * {@linkplain User users} that contains the text specified by containsText
     * 
     * If containsText is null, the array contains all of the {@linkplain User users} in the tree map
     * 
     * @return The array of {@link User users}, may be empty
     */
    private User[] getUsersArray(String containsText) { // if containsText == null, no filter
        ArrayList<User> usersArrayList = new ArrayList<>();

        for (User user : usersEmail.values()) {
            if (containsText == null || user.getEmail().contains(containsText)) {
                usersArrayList.add(user);
            }
        }

        User[] usersArray = new User[usersArrayList.size()];
        usersArrayList.toArray(usersArray);
        return usersArray;
    }

    /**
     * Save the {@linkplain User users} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link User users} were written successfully
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        User[] usersArray = getUsersArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), usersArray);
        return true;
    }

    /**
     * Load {@linkplain User users} from the JSON file into the map
     * 
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        usersID = new TreeMap<>();
        usersEmail = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of users
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        User[] usersArray = objectMapper.readValue(new File(filename), User[].class);

        // Add each user to the tree map and keep track of the greatest id
        for (User user : usersArray) {
            usersID.put(user.getId(), user);
            if (user.getId() > nextId)
                nextId = user.getId();
        }

        // Add each user to the tree map by email
        for (User user : usersArray) {
            usersEmail.put(user.getEmail(), user);
        }

        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(int id) throws IOException {
        synchronized (usersID) {
            if (usersID.containsKey(id))
                return usersID.get(id);

            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(String email) throws IOException {
        synchronized (usersEmail) {
            if (usersEmail.containsKey(email))
                return usersEmail.get(email);

            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User[] getUsers() throws IOException {
        synchronized (usersID) {
            return getUsersArray();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User[] findUsers(String containsText) throws IOException {
        synchronized (usersID) {
            return getUsersArray(containsText);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createUser(User user) throws IOException {
        synchronized (usersID) {
            User newUser = new User(nextId(), 
                                    user.getUsername(), 
                                    user.getPassword(), 
                                    user.getEmail(), 
                                    user.getCart(), 
                                    user.getOrders());

            usersID.put(newUser.getId(), newUser);
            usersEmail.put(newUser.getEmail(), newUser);
            save();
            return newUser;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User updateUser(User user) throws IOException {
        synchronized (usersID) {
            if (usersID.containsKey(user.getId()) == false)
                return null;

            if (usersEmail.containsKey(user.getEmail()) == false)
                return null;

            String previousEmail = usersID.get(user.getId()).getEmail();
            usersID.put(user.getId(), user);
            usersEmail.remove(previousEmail);
            usersEmail.put(user.getEmail(), user);

            save();
            return user;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteUser(String email) throws IOException {
        synchronized (usersEmail) {
            if (usersEmail.containsKey(email)) {
                User user = usersEmail.get(email);

                usersEmail.remove(email);
                usersID.remove(user.getId());
                return save();
            }

            return false;
        }
    }

    @Override
    public Double getUserTotalContributed(List<Order> orders, GiftDAO giftDAO) throws IOException {
        Double total = 0.0;
        for(Order order : orders) {
            total += order.getOrderCost();
        }
        return total;
    }

    
}