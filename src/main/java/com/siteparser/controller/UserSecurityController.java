package com.siteparser.controller;

import com.siteparser.domain.User;
import com.siteparser.service.jpa.UserService;
import com.siteparser.service.security.UserValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserSecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidateService userValidateService;

    @GetMapping("/login")
    public String login(){
        return "security/login";
    }

    @GetMapping("/register")
    public String showRegisterPage(){
        return "security/register";
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute User user){
        UserValidateService.ValidateResult result = userValidateService.validate(user);
        if (result.equals(UserValidateService.ValidateResult.ok)){
            userService.addNewUser(user);
            return new ModelAndView("redirect:/login");
        }else{
            ModelAndView modelAndView = new ModelAndView("security/register");
            modelAndView.addObject("error", result.getStringRepresentation());
            return modelAndView;
        }
    }
}
