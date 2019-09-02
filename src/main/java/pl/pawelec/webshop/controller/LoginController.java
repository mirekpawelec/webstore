/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pawelec.webshop.model.UserDetailsAdapter;
import pl.pawelec.webshop.model.UserInfo;
import pl.pawelec.webshop.model.role.UserRole;
import pl.pawelec.webshop.model.status.UserStatus;
import pl.pawelec.webshop.service.UserInfoService;
import pl.pawelec.webshop.controller.utils.ModelUtils;
import pl.pawelec.webshop.service.validator.UserInfoValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author mirek
 */
@SessionAttributes(names = {"loggedInUser"})
@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserInfoValidator userInfoValidator;

    @RequestMapping(value = "/*")
    public String homepage(HttpServletRequest request) {
        if (Optional.ofNullable(request.getRemoteUser()).isPresent()) {
            UserInfo loginUser = userInfoService.getByLogin(request.getRemoteUser());
            loginUser.setLastLoginDate(((UserDetailsAdapter) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLoginDate());
            userInfoService.update(loginUser);
        }
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, HttpServletRequest request) {
        model.addAttribute("jspFile", "login");
        ModelUtils.addGlobalAtribute(model, request);
        addLocalAtributesToModel(model);
        return "login";
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.GET)
    public String createUser(Model model, HttpServletRequest request) {
        model.addAttribute("modelUser", new UserInfo());
        model.addAttribute("jspFile", "addUser");
        ModelUtils.addGlobalAtribute(model, request);
        addLocalAtributesToModel(model);
        return "addUpdateUser";
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public String processCreateUser(@ModelAttribute("modelUser") @Valid UserInfo userInfoToBeAdd,
                                    BindingResult result, Model model, HttpServletRequest request,
                                    final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            ModelUtils.addGlobalAtribute(model, request);
            addLocalAtributesToModel(model);
            model.addAttribute("jspFile", "addUser");
            return "addUpdateUser";
        }
        String[] suppresedFields = result.getSuppressedFields();
        if (suppresedFields.length > 0) {
            throw new RuntimeException("It has occurred an attempt bind the illegal fields: "
                    + StringUtils.arrayToCommaDelimitedString(suppresedFields));
        }
        BCryptPasswordEncoder encrypt = new BCryptPasswordEncoder();
        userInfoToBeAdd.setPassword(encrypt.encode(userInfoToBeAdd.getPassword()));
        LOGGER.info("Save user... [" + userInfoToBeAdd + ']');
        userInfoService.create(userInfoToBeAdd);
        if (Optional.ofNullable(request.getRemoteUser()).isPresent()) {
            redirectAttributes.addFlashAttribute("createInfo", userInfoToBeAdd.getFirstName() + " " + userInfoToBeAdd.getLastName());
            redirectAttributes.addFlashAttribute("cssCreate", "success");
            return "redirect:/admin/users";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping("/user/edit/{login}")
    public String updateUser(@PathVariable("login") String userLogin, final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("cameFromUserPanel", true);
        return "redirect:/admin/users/" + userInfoService.getByLogin(userLogin).getUserId() + "/update";
    }

    @InitBinder(value = "modelUser")
    public void createProductBinder(WebDataBinder webDataBinder) {
        webDataBinder.setAllowedFields("login", "password", "repeatPassword", "firstName", "lastName", "email", "status", "role");
        webDataBinder.setValidator(userInfoValidator);
    }

    private Model addLocalAtributesToModel(Model model) {
        model.addAttribute("roles", Arrays.asList(UserRole.values()));
        model.addAttribute("statuses", Arrays.asList(UserStatus.values()));
        return model;
    }
}
