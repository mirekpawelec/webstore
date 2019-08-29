/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pawelec.webshop.model.UserInfo;
import pl.pawelec.webshop.model.role.UserRole;
import pl.pawelec.webshop.model.status.UserStatus;
import pl.pawelec.webshop.service.UserInfoService;
import pl.pawelec.webshop.controller.utils.AtributesModel;
import pl.pawelec.webshop.service.validator.UserInfoValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author mirek
 */
@Controller
@RequestMapping("/admin/users")
public class UserInfoController {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserInfoValidator userInfoValidator;


    @RequestMapping
    public String getAllUser(Model model, HttpServletRequest request) {
        model.addAttribute("users", userInfoService.getAll());
        model.addAttribute("jspFile", "users");
        AtributesModel.addGlobalAtributeToModel(model, request);
        return "users";
    }

    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    public String updateUser(@PathVariable("id") String userId, Model model, HttpServletRequest request) {
        UserInfo updateUser = userInfoService.getOneById(Long.valueOf(userId));
        updateUser.setPassword("");
        updateUser.setRepeatPassword("");
        model.addAttribute("modelUser", updateUser);
        model.addAttribute("jspFile", "updateUser");
        AtributesModel.addGlobalAtributeToModel(model, request);
        addLocalAtributesToModel(model);
        return "addUpdateUser";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateUser(@ModelAttribute("modelUser") @Valid UserInfo userInfoToBeUpdate
            , BindingResult result, Model model, HttpServletRequest request
            , final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("jspFile", "updateUser");
            AtributesModel.addGlobalAtributeToModel(model, request);
            addLocalAtributesToModel(model);
            return "addUpdateUser";
        }

        String[] suppresedFields = result.getSuppressedFields();
        if (suppresedFields.length > 0) {
            throw new RuntimeException("It has occurred an attempt bind the illegal fields: "
                    + StringUtils.arrayToCommaDelimitedString(suppresedFields));
        }
        
        if(!userInfoToBeUpdate.getPassword().isEmpty()){
            BCryptPasswordEncoder encrypt = new BCryptPasswordEncoder();
            userInfoToBeUpdate.setPassword(encrypt.encode(userInfoToBeUpdate.getPassword()));
        } else {
            userInfoToBeUpdate.setPassword(userInfoService.getOneById(userInfoToBeUpdate.getUserId()).getPassword());
        }
        
//        if(userInfoToBeUpdate.getCustomer().getCustomerId()==null){
//            userInfoToBeUpdate.setCustomer(null);
//        }

        String privilegesLevelPersonChangingData = Optional.ofNullable(userInfoService.getByLogin(request.getRemoteUser()).getRole()).orElse("");

        logger.info("Save user... [" + userInfoToBeUpdate + ']');
        userInfoService.update(userInfoToBeUpdate);
        redirectAttributes.addFlashAttribute("updateInfo", userInfoToBeUpdate.getFirstName() + " " + userInfoToBeUpdate.getLastName());
        redirectAttributes.addFlashAttribute("cssUpdate", "success");

        if (privilegesLevelPersonChangingData.equals(UserRole.ROLE_CLIENT.name()) || privilegesLevelPersonChangingData.equals(UserRole.ROLE_USER.name())) {
            return "redirect:/orders/user/" + userInfoToBeUpdate.getLogin();
        } else {
            return "redirect:/admin/users";
        }
    }

    @InitBinder(value = "modelUser")
    public void updateProductBinder(WebDataBinder webDataBinder) {
        webDataBinder.setDisallowedFields("lastModificationDate");
        webDataBinder.setValidator(userInfoValidator);
    }

    private Model addLocalAtributesToModel(Model model) {
        model.addAttribute("roles", Arrays.asList(UserRole.values()));
        model.addAttribute("statuses", Arrays.asList(UserStatus.values()));
        return model;
    }

}
