/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.controller.utils;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

public class ModelUtils {

    public static Model addGlobalAtribute(Model model, HttpServletRequest request) {
        String currentPath = request.getRequestURI();
        if (currentPath.startsWith("/web")) {
            currentPath = currentPath.substring(currentPath.indexOf("/", 1), currentPath.length());
        }

        String requestParameters = request.getQueryString();
        if (requestParameters == null) {
            requestParameters = "?";
        } else {
            if (requestParameters.indexOf("language") > -1) {
                requestParameters = requestParameters.indexOf("&language") > 0 ?
                        requestParameters.substring(0, requestParameters.indexOf("&language")) :
                        requestParameters.substring(0, requestParameters.indexOf("language"));
            }
            requestParameters = requestParameters.length() == 0 ? "?" : "?" + requestParameters + "&";
        }

        model.addAttribute("lastRequestUrl", currentPath + requestParameters);
        return model;
    }
}
