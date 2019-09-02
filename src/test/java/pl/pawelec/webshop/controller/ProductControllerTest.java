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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.pawelec.webshop.config.WebstoreApplication;
import pl.pawelec.webshop.service.exception.NoProductFoundUnderProductNoException;
import pl.pawelec.webshop.model.Product;
import pl.pawelec.webshop.model.status.ProductStatus;
import pl.pawelec.webshop.service.ProductService;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author mirek
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WebstoreApplication.class})
@SpringBootTest
public class ProductControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductControllerTest.class);
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ProductService productService;
    private MockMvc mockMvc;

    private static final String PRODUCT_NO = "000.000.00";
    private static final String NAME = "Laptop";
    private static final String MANUFACTURER = "SomeManufacturer";
    private static final String CATEGORY = "Laptop";
    private static final String DESCRIPTION = "Some description";
    private static final BigDecimal UNIT_PRICE = new BigDecimal("99.99");
    private static final int QUANTITY = 1;

    private static final String PRODUCT_NO_2 = "999.999.99";

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
                .withStatus(ProductStatus.ED.name())
                .build();
        productService.create(testProduct);
    }

    @After
    public void clean() {
        try {
            productService.delete(productService.getOneByProductNo(PRODUCT_NO));
        } catch (NoProductFoundUnderProductNoException npe) {
            LOGGER.info("No product found: " + npe.getProductNo());
        }
        try {
            productService.delete(productService.getOneByProductNo(PRODUCT_NO_2));
        } catch (NoProductFoundUnderProductNoException npe) {
            LOGGER.info("No product found: " + npe.getProductNo());
        }
    }

    @Test
    public void test_get_all_products() throws Exception {
        //given
        Product patternProduct = productService.getOneByProductNo(PRODUCT_NO);
        //when & then
        this.mockMvc.perform(get("/admin/products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", hasItem(patternProduct)))
                .andExpect(model().attributeExists("jspFile"))
                .andExpect(model().attribute("jspFile", "products"))
                .andExpect(model().attributeExists("lastRequestUrl"))
                .andExpect(view().name("products"));
    }

    @Test
    public void test_get_by_productId() throws Exception {
        //given
        Product patternProduct = productService.getOneByProductNo(PRODUCT_NO);
        //when & then
        this.mockMvc.perform(get("/admin/products/product").param("id", PRODUCT_NO))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", patternProduct))
                .andExpect(model().attributeExists("jspFile"))
                .andExpect(model().attribute("jspFile", "product"))
                .andExpect(model().attributeExists("lastRequestUrl"))
                .andExpect(view().name("product"));
    }

    @Test
    public void test_add_product_a_empty_object_should_be_added_to_model() throws Exception {
        //given
        Product patternProduct = new Product();
        //when & then
        this.mockMvc.perform(get("/admin/products/add"))
                .andExpect(model().attributeExists("newProductForm"))
                .andExpect(model().attribute("newProductForm", patternProduct))
                .andExpect(model().attributeExists("jspFile"))
                .andExpect(model().attribute("jspFile", "newProductForm"))
                .andExpect(model().attributeExists("lastRequestUrl"))
                .andExpect(view().name("addProductForm"));
    }

    @Test
    public void test_process_of_adding_a_product() throws Exception {
        //given
        Product patternProduct, resultProduct;
        patternProduct = resultProduct = new Product();

        patternProduct = new Product.Builder()
                .withProductNo(PRODUCT_NO_2)
                .withName(NAME)
                .withManufacturer(MANUFACTURER)
                .withCategory(CATEGORY)
                .withDescription(DESCRIPTION)
                .withUnitPrice(UNIT_PRICE)
                .withQuantityInBox(QUANTITY)
                .withStatus(ProductStatus.ED.name())
                .build();

        //when & then
        this.mockMvc.perform(post("/admin/products/add").flashAttr("newProductForm", patternProduct))
                .andExpect(redirectedUrl("/admin/products/product?id=" + patternProduct.getProductNo()));
        resultProduct = productService.getOneByProductNo(PRODUCT_NO_2);
        assertNotNull(resultProduct.getProductId());
        assertEquals(NAME, resultProduct.getName());
        assertEquals(MANUFACTURER, resultProduct.getManufacturer());
    }

    @Test(expected = NoProductFoundUnderProductNoException.class)
    public void test_delete_product_by_id_a_product_should_be_deleted_and_thrown_out_an_exception() throws Exception {
        // given
        Product patternProduct = productService.getOneByProductNo(PRODUCT_NO);
        String deletePath = String.format("/admin/products/params;id=%s;productNo=%s/delete", patternProduct.getProductId(), patternProduct.getProductNo());

        // when
        this.mockMvc.perform(get(deletePath))
                .andExpect(redirectedUrl("/admin/products"));

        // then
        productService.getOneByProductNo(PRODUCT_NO);
    }

}
