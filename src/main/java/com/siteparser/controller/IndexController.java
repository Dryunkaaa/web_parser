package com.siteparser.controller;

import com.siteparser.service.security.SecurityProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("/")
public class IndexController extends BaseSecurityController {

    @Autowired
    private SecurityProcessorService securityProcessorService;

    private static boolean showSuccessfulAuthorization = true;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = createModelAndView("index");
        modelAndView.addObject("showSuccessfulAuthorization", showSuccessfulAuthorization);
        showSuccessfulAuthorization = securityProcessorService.getUserFromContext() == null;

        return modelAndView;
    }
}
