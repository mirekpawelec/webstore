/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.pawelec.webshop.config.WebshopApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author mirek
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WebshopApplication.class})
@SpringBootTest
public class DeliveryControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void test_get_all_deliveries() throws Exception {
        //when & then
        this.mockMvc.perform(get("/admin/deliveries"))
                .andExpect(model().attributeExists("deliveries"))
                .andExpect(model().attributeExists("jspFile"))
                .andExpect(model().attribute("jspFile", "deliveries"))
                .andExpect(model().attributeExists("lastRequestUrl"))
                .andExpect(view().name("deliveries"));
    }
}
