/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.controller;

import org.junit.After;
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
import pl.pawelec.webshop.model.Product;
import pl.pawelec.webshop.model.status.ProductStatus;
import pl.pawelec.webshop.service.ProductService;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author mirek
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WebshopApplication.class})
@SpringBootTest
public class HomeControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ProductService productService;
    private MockMvc mockMvc;

    private static final String PRODUCT_NO = "000.000.00";
    private static final String NAME = "Tablet";
    private static final String MANUFACTURER = "SomeManufacturer";
    private static final String CATEGORY = "Tablet";
    private static final String DESCRIPTION = "Some description";
    private static final BigDecimal UNIT_PRICE = new BigDecimal("99.99");
    private static final int QUANTITY = 1;
    private static final String PROMOTION = "N";
    private static final int DISCOUNT = 0;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        Product testProduct = new Product.Builder()
                .withProductNo(PRODUCT_NO)
                .withName(NAME)
                .withManufacturer(MANUFACTURER)
                .withCategory(CATEGORY)
                .withDescription(DESCRIPTION)
                .withUnitPrice(UNIT_PRICE)
                .withQuantityInBox(QUANTITY)
                .withPromotion(PROMOTION)
                .withDiscount(DISCOUNT)
                .withStatus(ProductStatus.ED.name())
                .build();
        productService.create(testProduct);
    }

    @After
    public void clean() {
        productService.delete(productService.getOneByProductNo(PRODUCT_NO));
    }

    @Test
    public void test_get_all_products() throws Exception {
        //when & then
        this.mockMvc.perform(get("/home").secure(true))
                .andExpect(model().attributeExists("allProducts"))
                .andExpect(model().attributeExists("jspFile"))
                .andExpect(model().attribute("jspFile", "homepage"))
                .andExpect(model().attributeExists("lastRequestUrl"))
                .andExpect(view().name("homepage"));
    }

    @Test
    public void test_get_product_by_number() throws Exception {
        // given
        Product patternProduct = productService.getOneByProductNo(PRODUCT_NO);

        // when
        // then
        this.mockMvc.perform(get("/home/product")
                .param("productNo", PRODUCT_NO))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", patternProduct))
                .andExpect(model().attributeExists("jspFile"))
                .andExpect(model().attribute("jspFile", "product"))
                .andExpect(model().attributeExists("lastRequestUrl"))
                .andExpect(view().name("product"));
    }
}
