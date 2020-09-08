package com.siteparser.controller;

import com.siteparser.domain.User;
import com.siteparser.service.jpa.UserService;
import com.siteparser.service.security.SecurityProcessorService;
import com.siteparser.service.security.UserValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController extends BaseSecurityController {

    @Autowired
    private SecurityProcessorService securityProcessorService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserValidateService userValidateService;

    @GetMapping("/user/info")
    public ModelAndView info() {
        ModelAndView modelAndView = createModelAndView("/user/showUserData");
        User user = getUser();
        String roles = securityProcessorService.getUserRolesAsString();
        modelAndView.addObject("domainUser", user);
        modelAndView.addObject("roles", roles);
        return modelAndView;
    }

    @GetMapping("/user/changePassword")
    public ModelAndView changePassword() {
        ModelAndView modelAndView = createModelAndView("/user/changePassword");
        User user = getUser();
        modelAndView.addObject("domainUser", user);
        return modelAndView;
    }

    @RequestMapping("/user/acceptChangePassword")
    public ModelAndView acceptChangePassword(@RequestParam("oldPassword") String oldPassword,
                                             @RequestParam("newPassword") String newPassword) {
        User user = getUser();
        UserValidateService.ValidateResult result = userValidateService.validatePassword(user, oldPassword, newPassword);
        if (result.equals(UserValidateService.ValidateResult.ok)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.update(user);
            return new ModelAndView("redirect:/user/info");
        }
        ModelAndView modelAndView = createModelAndView("/user/changePassword");
        modelAndView.addObject("domainUser", user);
        modelAndView.addObject("error", result.getStringRepresentation());
        return modelAndView;
    }

    public User getUser() {
        return userService.getByEmail(securityProcessorService.getCurrentUserEmail());
    }
}
