package com.example.demo.controllertests;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("Jai");
        user.setPassword("1234abc");
        user.setCart(cart);
        when(userRepository.findByUsername("Jai")).thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget that is round");
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
    }

    @Test
    public void addToCartHappyPath(){
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("Jai");
        r.setItemId(1L);
        r.setQuantity(1);
        final ResponseEntity<Cart> response= cartController.addTocart(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().getItems().size() > 0);
    }

    @Test
    public void addToCartNegativeTest(){
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("bob");
        r.setItemId(1L);
        r.setQuantity(1);
        final ResponseEntity<Cart> response= cartController.addTocart(r);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void removeFromCartHappyPath(){
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("Jai");
        r.setItemId(1L);
        r.setQuantity(1);
        final ResponseEntity<Cart> response= cartController.addTocart(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        final ResponseEntity<Cart> responseObj= cartController.removeFromcart(r);
        assertNotNull(responseObj);
        assertEquals(200, responseObj.getStatusCodeValue());
        assertNotNull(responseObj.getBody());

    }

    @Test
    public void removeFromCartNegativeTest(){
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("Jai");
        r.setItemId(3L);
        r.setQuantity(1);

        final ResponseEntity<Cart> responseObjTry= cartController.removeFromcart(r);
        assertNotNull(responseObjTry);
        assertEquals(404, responseObjTry.getStatusCodeValue());
        assertNull(responseObjTry.getBody());
    }

}
