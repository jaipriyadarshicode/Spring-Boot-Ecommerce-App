package com.example.demo.controllertests;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);


        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget that is round");
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepository.findByName("Round Widget")).thenReturn(Collections.singletonList(item));
    }

    @Test
    public void getItems(){
        ResponseEntity<List<Item>> itemResponse = itemController.getItems();
        assertNotNull(itemResponse);
        assertEquals(200, itemResponse.getStatusCodeValue());
        assertTrue(itemResponse.getBody().size() > 0);
    }

    @Test
    public void getItemsByIdHappyPath(){
        ResponseEntity<Item> itemResponse = itemController.getItemById(1L);
        assertNotNull(itemResponse);
        assertEquals(200, itemResponse.getStatusCodeValue());
        assertNotNull(itemResponse.getBody());
    }

    @Test
    public void getItemsByNameHappyPath(){
        ResponseEntity<List<Item>> itemResponse = itemController.getItemsByName("Round Widget");
        assertNotNull(itemResponse);
        assertEquals(200, itemResponse.getStatusCodeValue());
        assertTrue(itemResponse.getBody().size() > 0);
    }


    @Test
    public void getItemsByIdNegativeTest(){
        ResponseEntity<Item> itemResponse = itemController.getItemById(2L);
        assertNotNull(itemResponse);
        assertEquals(404, itemResponse.getStatusCodeValue());
        assertNull(itemResponse.getBody());
    }

    @Test
    public void getItemsByNameNegativeTest(){
        ResponseEntity<List<Item>> itemResponse = itemController.getItemsByName("Round");
        assertNotNull(itemResponse);
        assertEquals(404, itemResponse.getStatusCodeValue());
    }
}
