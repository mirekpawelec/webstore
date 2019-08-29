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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pawelec.webshop.model.Faq;
import pl.pawelec.webshop.model.status.FaqStatus;
import pl.pawelec.webshop.service.FaqService;
import pl.pawelec.webshop.controller.utils.AtributesModel;
import pl.pawelec.webshop.service.validator.FaqValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author mirek
 */
@Controller
public class FaqController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaqController.class);
    @Autowired
    private FaqService faqService;
    @Autowired
    private FaqValidator faqValidator;


    @RequestMapping("/faq")
    public String getFaq(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("faq", faqService.getAll()
                .stream()
                .filter(f -> f.getStatus().equals(FaqStatus.OK.name()))
                .collect(Collectors.toList()));
        return "redirect:/home";
    }

    @RequestMapping("/admin/faq")
    public String getAllQuestionsFaq(Model model, HttpServletRequest request) {
        model.addAttribute("questions", faqService.getAll());
        AtributesModel.addGlobalAtributeToModel(model, request);
        model.addAttribute("jspFile", "questionsFaq");
        return "questionsFaq";
    }

    @RequestMapping(value = "/admin/faq/add", method = RequestMethod.GET)
    public String addQuestionForm(Model model, HttpServletRequest request) {
        model.addAttribute("modelFaq", new Faq());
        model.addAttribute("jspFile", "addUpdateQuestionFaq");
        AtributesModel.addGlobalAtributeToModel(model, request);
        addLocalAttributesToModel(model);
        return "addUpdateQuestionFaq";
    }

    @RequestMapping(value = "/admin/faq/{id}/update", method = RequestMethod.GET)
    public String updateQuestionForm(@PathVariable("id") String faqId, Model model, HttpServletRequest request) {
        model.addAttribute("modelFaq", faqService.getOneById(Long.valueOf(faqId)));
        model.addAttribute("jspFile", "addUpdateQuestionFaq");
        AtributesModel.addGlobalAtributeToModel(model, request);
        addLocalAttributesToModel(model);
        return "addUpdateQuestionFaq";
    }

    @RequestMapping(value = "/admin/faq/save", method = RequestMethod.POST)
    public String processSaveQuestionForm(@ModelAttribute("modelFaq") @Valid Faq modelFaqToBeAdd, BindingResult result, Model model,
                                          HttpServletRequest request, final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            AtributesModel.addGlobalAtributeToModel(model, request);
            addLocalAttributesToModel(model);
            model.addAttribute("jspFile", "addUpdateQuestionFaq");
            return "addUpdateQuestionFaq";
        }
        LOGGER.info("Save... [" + modelFaqToBeAdd + ']');
        if (modelFaqToBeAdd.isNew()) {
            Long id = faqService.createAndGetId(modelFaqToBeAdd);
            redirectAttributes.addFlashAttribute("cssCreate", "success");
            redirectAttributes.addFlashAttribute("createInfo", id);
        } else {
            modelFaqToBeAdd.setLastModyficationDate(LocalDateTime.now());
            faqService.update(modelFaqToBeAdd);
            redirectAttributes.addFlashAttribute("cssUpdate", "success");
            redirectAttributes.addFlashAttribute("updateInfo", modelFaqToBeAdd.getFaqId());
        }
        return "redirect:/admin/faq";
    }

    @RequestMapping(value = "/admin/faq/{id}/delete")
    public String deleteQuestion(@PathVariable("id") String faqId, final RedirectAttributes redirectAttributes) {
        if (faqService.exists(Long.valueOf(faqId))) {
            faqService.deleteById(Long.valueOf(faqId));
        }
        redirectAttributes.addFlashAttribute("cssDelete", "danger");
        redirectAttributes.addFlashAttribute("deleteInfo", faqId);
        return "redirect:/admin/faq";
    }

    @InitBinder
    public void processBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(faqValidator);
    }

    private Model addLocalAttributesToModel(Model model) {
        model.addAttribute("statuses", Arrays.asList(FaqStatus.values()));
        return model;
    }
}
