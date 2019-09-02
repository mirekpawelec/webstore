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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.pawelec.webshop.config.WebstoreApplication;
import pl.pawelec.webshop.model.UserInfo;
import pl.pawelec.webshop.service.UserInfoService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author mirek
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WebstoreApplication.class})
@SpringBootTest
public class LoginControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserInfoService userInfoService;
    MockHttpSession mockHttpSession;
    private MockMvc mockMvc;

    private static final String LOGIN = "TestUser";
    private static final String PASSWORD = "Hasl@";
    private static final String REPEAT_PASSWORD = "Hasl@";
    private static final String FIRST_NAME = "Jan";
    private static final String LAST_NAME = "Kowalski";
    private static final String EMAIL = "example@example.com";
    private static final String ROLE = "ROLE_USER";


    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @After
    public void clean() {
        UserInfo userInfo = userInfoService.getByLogin(LOGIN);
        if (userInfo != null) {
            userInfoService.delete(userInfo);
        }
    }

    @Test
    public void test_redirect_to_the_login_page_should_be_valid() throws Exception {
        //when & then
        this.mockMvc.perform(get("/login"))
                .andExpect(model().attributeExists("jspFile"))
                .andExpect(model().attribute("jspFile", "login"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("lastRequestUrl"))
                .andExpect(view().name("login"));
        ;
    }

    @Test
    public void test_create_user_should_be_valid() throws Exception {
        //given
        UserInfo patternUser = new UserInfo();
        //when & then
        this.mockMvc.perform(get("/user/add"))
                .andExpect(model().attributeExists("jspFile"))
                .andExpect(model().attribute("jspFile", "addUser"))
                .andExpect(model().attributeExists("modelUser"))
                .andExpect(model().attribute("modelUser", patternUser))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("statuses"))
                .andExpect(model().attributeExists("lastRequestUrl"))
                .andExpect(view().name("addUpdateUser"));
    }

    @Test
    public void test_process_creation_a_user_should_not_get_any_errors() throws Exception {
        //given
        UserInfo userAdded = new UserInfo();
        UserInfo patternUser = new UserInfo(LOGIN, PASSWORD, REPEAT_PASSWORD, FIRST_NAME, LAST_NAME, EMAIL, ROLE);
        //when
        this.mockMvc.perform(post("/user/add").flashAttr("modelUser", patternUser));
        userAdded = userInfoService.getByLogin(LOGIN);
        //then
        assertNotNull(userInfoService.getByLogin(LOGIN));
        assertEquals(LOGIN, userAdded.getLogin());
        assertEquals(FIRST_NAME, userAdded.getFirstName());
        assertEquals(LAST_NAME, userAdded.getLastName());
    }

}
