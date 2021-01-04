package com.example.demo.controllertests;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("Jai");
        user.setPassword("1234bc");
        user.setCart(cart);
        when(userRepository.findByUsername("Jai")).thenReturn(user);
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.findByUsername("bob")).thenReturn(null);
    }

    @Test
    public void createUserHappyPath(){
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Jai");
        r.setPassword("1234abc");
        r.setConfirmpassword("1234abc");

        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void createUserNegativeTest(){
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Jai");
        r.setPassword("1234");
        r.setConfirmpassword("1234");

        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void confirmPassNotEqualNegativeTest(){
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Jai");
        r.setPassword("1234abc");
        r.setConfirmpassword("1234");

        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void getUserHappyPath(){
        final ResponseEntity<User> responseUserObj = userController.findByUserName("Jai");
        assertNotNull(responseUserObj);
        assertEquals(200, responseUserObj.getStatusCodeValue());
        assertEquals("Jai", responseUserObj.getBody().getUsername());
    }

    @Test
    public void getUserNegativeTest(){
        final ResponseEntity<User> responseUserObj = userController.findByUserName("bob");
        assertEquals(404, responseUserObj.getStatusCodeValue());
        assertNotNull(responseUserObj);
    }

    @Test
    public void getUserByIDHappyPath(){
        final ResponseEntity<User> responseUserObj = userController.findById(0l);
        assertNotNull(responseUserObj);
        assertEquals(200, responseUserObj.getStatusCodeValue());
        assertEquals(0, responseUserObj.getBody().getId());
    }

    @Test
    public void getUserByIDNegativeTest(){
        final ResponseEntity<User> responseUserObj = userController.findById(1L);
        assertEquals(404, responseUserObj.getStatusCodeValue());
        assertNotNull(responseUserObj);
    }
}
