package com.siteparser.controller;

import com.siteparser.service.security.SecurityProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BaseSecurityController {

    @Autowired
    private SecurityProcessorService securityProcessorService;

    public ModelAndView createModelAndView(String viewName){
        return securityProcessorService.createModelAndView(viewName);
    }
}
