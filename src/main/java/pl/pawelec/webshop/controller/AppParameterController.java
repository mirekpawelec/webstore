/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pawelec.webshop.controller.attributes.AlertAttribute;
import pl.pawelec.webshop.controller.attributes.CrudAttribute;
import pl.pawelec.webshop.controller.utils.ModelUtils;
import pl.pawelec.webshop.model.AppParameter;
import pl.pawelec.webshop.service.AppParameterService;
import pl.pawelec.webshop.service.validator.AppParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

@SessionAttributes(names = {"jspFile", "updateInfo"})
@Controller
@RequestMapping("admin/parameters")
public class AppParameterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppParameterController.class);

    @Autowired
    private AppParameterService appParameterService;
    @Autowired
    private AppParameterValidator appParameterValidator;


    @RequestMapping
    public String getAllAppParameters(Model model, HttpServletRequest request) {

        model.addAttribute("allParameters", appParameterService.getAll());
        model.addAttribute("jspFile", "appParameters");
        ModelUtils.addGlobalAtribute(model, request);

        return "appParameters";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addAppParameter(Model model, HttpServletRequest request) {

        model.addAttribute("newParameterForm", new AppParameter());
        model.addAttribute("jspFile", "addAppParameter");
        ModelUtils.addGlobalAtribute(model, request);

        return "addAppParameter";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddAppParameter(@ModelAttribute("newParameterForm") @Valid AppParameter appParameterToBeAdd,
                                         BindingResult result, Model model, HttpServletRequest request,
                                         final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            ModelUtils.addGlobalAtribute(model, request);
            return "addAppParameter";
        }

        String[] suppresedFields = result.getSuppressedFields();
        if (suppresedFields.length > 0) {
            throw new RuntimeException(String.format("It has occurred an attempt bind the illegal fields: %s",
                    StringUtils.arrayToCommaDelimitedString(suppresedFields)));
        }
        appParameterService.create(appParameterToBeAdd);

        addRedirectAttrbutes(redirectAttributes, AlertAttribute.SUCCESS, CrudAttribute.CREATE, true,
                prepareFullAppParamName(appParameterToBeAdd.getSymbol(), appParameterToBeAdd.getName()));

        return "redirect:/admin/parameters";
    }

    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    public String updateAppParameter(@PathVariable("id") String appParameterId, Model model, HttpServletRequest request) {
        AppParameter appParameter = appParameterService.getOneById(Long.valueOf(appParameterId));

        model.addAttribute("updateParameterForm", appParameter);
        model.addAttribute("updateInfo", appParameter.getSymbol() + " - " + appParameter.getName());
        model.addAttribute("jspFile", "updateAppParameter");
        ModelUtils.addGlobalAtribute(model, request);

        return "updateAppParameter";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateAppParameter(@ModelAttribute("updateParameterForm") @Valid AppParameter appParameterToBeUpdate,
                                            BindingResult result, Model model, HttpServletRequest request,
                                            final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            ModelUtils.addGlobalAtribute(model, request);
            model.addAttribute("jspFile", "updateAppParameter");
            return "updateAppParameter";
        }

        String fullAppParamName = prepareFullAppParamName(appParameterToBeUpdate.getSymbol(), appParameterToBeUpdate.getName());
        if(Objects.nonNull(appParameterService.getOneById(appParameterToBeUpdate.getParameterId()))) {
            appParameterService.update(appParameterToBeUpdate);
            addRedirectAttrbutes(redirectAttributes, AlertAttribute.SUCCESS, CrudAttribute.UPDATE, true, fullAppParamName);
        } else {
            addRedirectAttrbutes(redirectAttributes, AlertAttribute.DANGER, CrudAttribute.UPDATE, false, fullAppParamName);
        }

        return "redirect:/admin/parameters";
    }

    @RequestMapping("/{id}/delete")
    public String deleteAppParameter(@PathVariable("id") String appParameterId, final RedirectAttributes redirectAttributes) {
        AppParameter appParameter = appParameterService.getOneById(Long.valueOf(appParameterId));

        if(Objects.nonNull(appParameter)) {
            appParameterService.delete(appParameter);
            addRedirectAttrbutes(redirectAttributes, AlertAttribute.SUCCESS, CrudAttribute.DELETE, true,
                    prepareFullAppParamName(appParameter.getSymbol(), appParameter.getName()));
        } else {
            addRedirectAttrbutes(redirectAttributes, AlertAttribute.DANGER, CrudAttribute.DELETE, false, appParameterId);
        }

        return "redirect:/admin/parameters";
    }

    @InitBinder(value = {"newParameterForm"})
    public void addAppParameterBinder(WebDataBinder webDataBinder) {
        webDataBinder.setDisallowedFields("parameterId", "lastModificationDate", "createDate");
        webDataBinder.setValidator(appParameterValidator);
    }

    @InitBinder(value = {"updateParameterForm"})
    public void updateAppParameterBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(appParameterValidator);
    }

    private void addRedirectAttrbutes(RedirectAttributes redirectAttributes, AlertAttribute alertAttribute,
                                      CrudAttribute crudAttribute, boolean isSuccess, String message) {
        redirectAttributes.addFlashAttribute("alertType", alertAttribute.getNameAsLowerCase());
        redirectAttributes.addFlashAttribute(
                String.format("%s%sInfo", crudAttribute.getNameAsLowerCase(), isSuccess? "Success": "Error"),
                message
        );
    }

    private String prepareFullAppParamName(String symbol, String name) {
        return String.format("%s - %s", symbol, name);
    }
}
