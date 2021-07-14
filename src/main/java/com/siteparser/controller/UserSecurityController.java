package com.siteparser.controller;

import com.siteparser.domain.User;
import com.siteparser.service.jpa.UserService;
import com.siteparser.service.security.UserValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserSecurityController extends BaseSecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidateService userValidateService;

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute User user) {
        ModelAndView modelAndView;
        UserValidateService.ValidateResult result = userValidateService.validate(user);

        if (result.equals(UserValidateService.ValidateResult.OK)) {
            userService.addNewUser(user);
            modelAndView = new ModelAndView("redirect:/?login");
        } else {
            modelAndView = createModelAndView("index");
            modelAndView.addObject("registerError", result.getStringRepresentation());
        }

        return modelAndView;
    }
}
