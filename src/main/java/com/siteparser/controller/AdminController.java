package com.siteparser.controller;

import com.siteparser.domain.Role;
import com.siteparser.domain.User;
import com.siteparser.service.jpa.RoleService;
import com.siteparser.service.jpa.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController extends BaseSecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/test")
    public ModelAndView test(){
        return new ModelAndView("test");
    }

    @GetMapping("/admin")
    public ModelAndView showAllUsers() {
        ModelAndView modelAndView = createModelAndView("/admin/users_dashboard");
        modelAndView.addObject("users", userService.getAll());
        return modelAndView;
    }

    @GetMapping("/admin/user/setUserActiveStatus")
    public String setStatus(@RequestParam String email, @RequestParam int status) {
        User user = userService.getByEmail(email);
        user.setActive(status);
        userService.update(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user/delete")
    public String deleteUser(@RequestParam String email) {
        User user = userService.getByEmail(email);
        userService.delete(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user/add")
    public ModelAndView addUser() {
        return createModelAndView("/admin/user_add");
    }

    @PostMapping("/admin/user/create")
    public ModelAndView createUser(@ModelAttribute User user) {
        ModelAndView modelAndView = createModelAndView("/admin/user_add");
        String error = "";
        if (userService.getByEmail(user.getEmail()) != null) {
            error = "Пользователь с таким email уже существует.";
        }
        if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            error = "Пустое поле Email или Пароль";
        }
        if (error.isEmpty()) {
            userService.addNewUser(user);
            return new ModelAndView("redirect:/admin");
        }
        modelAndView.addObject("error", error);
        return modelAndView;
    }

    @GetMapping("/admin/user/edit")
    public ModelAndView userEdit(@RequestParam("email") String email) {
        ModelAndView modelAndView = createModelAndView("/admin/user_edit");
        User user = userService.getByEmail(email);
        modelAndView.addObject("domainUser", user);
        modelAndView.addObject("allRoles", roleService.getAll());
        return modelAndView;
    }

    @PostMapping("/admin/user/acceptEdit")
    public String acceptEdit(@RequestParam(value = "email", required = true) String email,
                             @RequestParam(value = "password", required = false) String password,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "last_name", required = false) String last_name,
                             @RequestParam(value = "userEmail", required = true) String userEmail,
                             @RequestParam(value = "roles", required = true) String[] roles) {
        User user = userService.getByEmail(userEmail);
        if (user != null) {
            if (checkField(email)) user.setEmail(email);
            if (checkField(name)) user.setName(name);
            if (checkField(last_name)) user.setLast_name(last_name);
            if (checkField(password)) user.setPassword(passwordEncoder.encode(password));
            user.getRoles().clear();
            for (String roleName:roles){
                Role role = roleService.getRoleByName(roleName);
                user.getRoles().add(role);
            }
            userService.update(user);
        }
        return "redirect:/admin";
    }

    private boolean checkField(String value) {
        if (value == null) {
            return false;
        }

        if (value.trim().length() == 0) {
            return false;
        }

        return true;
    }

}
