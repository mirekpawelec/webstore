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
import pl.pawelec.webshop.service.exception.NoParametersKeyFoundException;
import pl.pawelec.webshop.model.AppParameter;
import pl.pawelec.webshop.service.AppParameterService;

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
public class AppParameterControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppParameterControllerTest.class);

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AppParameterService appParameterService;
    private MockMvc mockMvc;

    public static final String FIRST_PARAM_SYMBOL = "first_app_parameter";
    public static final String FIRST_PARAM_NAME = "test1";
    public static final String FIRST_PARAM_VALUE = "0";
    public static final String FIRST_PARAM_DESCRIPTION = "first app parameter";
    public static final String SECOND_PARAM_SYMBOL = "second_app_parameter";
    public static final String SECOND_PARAM_NAME = "test2";
    public static final String SECOND_PARAM_VALUE = "1";
    public static final String SECOND_PARAM_DESCRIPTION = "second app parameter";


    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        appParameterService.create(new AppParameter(FIRST_PARAM_SYMBOL, FIRST_PARAM_NAME, FIRST_PARAM_VALUE, FIRST_PARAM_DESCRIPTION));
    }

    @After
    public void clean() {
        try {
            appParameterService.delete(appParameterService.getByUniqueKey(FIRST_PARAM_SYMBOL, FIRST_PARAM_NAME));
        } catch (NoParametersKeyFoundException nsce) {
            LOGGER.info("No app parameter found: " + nsce.getSymbol() + " - " + nsce.getName());
        }
        try {
            appParameterService.delete(appParameterService.getByUniqueKey(SECOND_PARAM_SYMBOL, SECOND_PARAM_NAME));
        } catch (NoParametersKeyFoundException nsce) {
            LOGGER.info("No app parameter found: " + nsce.getSymbol() + " - " + nsce.getName());
        }
    }

    @Test
    public void get_all_app_parameters__a_pattern_parameter_should_be_inside_the_collection() throws Exception {
        // given
        AppParameter patternAppParameter = appParameterService.getByUniqueKey(FIRST_PARAM_SYMBOL, FIRST_PARAM_NAME);

        // when
        // then
        this.mockMvc.perform(get("/admin/parameters"))
                .andExpect(model().attributeExists("allParameters"))
                .andExpect(model().attribute("allParameters", hasItem(patternAppParameter)))
                .andExpect(model().attributeExists("jspFile"))
                .andExpect(model().attribute("jspFile", "appParameters"))
                .andExpect(model().attributeExists("lastRequestUrl"))
                .andExpect(view().name("appParameters"));
    }

    @Test
    public void add_new_app_parameter__a_empty_object_should_be_added_to_model() throws Exception {
        // given
        AppParameter patternAppParameter = new AppParameter();

        // when
        // then
        this.mockMvc.perform(get("/admin/parameters/add"))
                .andExpect(model().attributeExists("newParameterForm"))
                .andExpect(model().attribute("newParameterForm", patternAppParameter))
                .andExpect(model().attributeExists("jspFile"))
                .andExpect(model().attribute("jspFile", "addAppParameter"))
                .andExpect(model().attributeExists("lastRequestUrl"))
                .andExpect(view().name("addAppParameter"));
    }

    @Test
    public void process_of_adding_new_app_parameter_should_was_created() throws Exception {
        // given
        AppParameter patternAppParameter = new AppParameter(SECOND_PARAM_SYMBOL, SECOND_PARAM_NAME, SECOND_PARAM_VALUE, SECOND_PARAM_DESCRIPTION);

        // when
        // then
        this.mockMvc.perform(post("/admin/parameters/add").flashAttr("newParameterForm", patternAppParameter))
                .andExpect(redirectedUrl("/admin/parameters"));
        AppParameter addedAppParameter = appParameterService.getByUniqueKey(SECOND_PARAM_SYMBOL, SECOND_PARAM_NAME);
        assertNotNull(addedAppParameter);
        assertEquals(SECOND_PARAM_SYMBOL, addedAppParameter.getSymbol());
        assertEquals(SECOND_PARAM_NAME, addedAppParameter.getName());
    }

    @Test
    public void process_of_modify_a_app_parametr() throws Exception {
        // given
        AppParameter patternAppParameter = appParameterService.getByUniqueKey(FIRST_PARAM_SYMBOL, FIRST_PARAM_NAME);
        patternAppParameter.setValue(SECOND_PARAM_VALUE);
        patternAppParameter.setDescription(SECOND_PARAM_DESCRIPTION);

        // when
        // then
        this.mockMvc.perform(post("/admin/parameters/update").flashAttr("updateParameterForm", patternAppParameter))
                .andExpect(redirectedUrl("/admin/parameters"));
        AppParameter modifiedAppParameter = appParameterService.getByUniqueKey(FIRST_PARAM_SYMBOL, FIRST_PARAM_NAME);
        assertNotNull(modifiedAppParameter);
        assertEquals(FIRST_PARAM_SYMBOL, modifiedAppParameter.getSymbol());
        assertEquals(FIRST_PARAM_NAME, modifiedAppParameter.getName());
        assertEquals(SECOND_PARAM_VALUE, modifiedAppParameter.getValue());
        assertEquals(SECOND_PARAM_DESCRIPTION, modifiedAppParameter.getDescription());
    }

    @Test(expected = NoParametersKeyFoundException.class)
    public void delete_a_app_parameter_should_be_removed_and_thrown_out_exception() throws Exception {
        // given
        AppParameter patternAppParameter = appParameterService.getByUniqueKey(FIRST_PARAM_SYMBOL, FIRST_PARAM_NAME);

        // when
        // then
        this.mockMvc.perform(post("/admin/parameters/" + patternAppParameter.getParameterId() + "/delete"))
                .andExpect(redirectedUrl("/admin/parameters"));
        AppParameter removedAppParameter = appParameterService.getByUniqueKey(FIRST_PARAM_SYMBOL, FIRST_PARAM_NAME);
        assertNull(removedAppParameter);
    }
}
