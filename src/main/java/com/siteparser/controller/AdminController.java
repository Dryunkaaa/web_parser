package com.siteparser.controller;

import com.siteparser.domain.Role;
import com.siteparser.domain.User;
import com.siteparser.service.convert.DateFormatService;
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

import java.util.List;

@Controller
public class AdminController extends BaseSecurityController {

    private final static int COUNT_OF_RECORDS = 20;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private DateFormatService dateFormatService;

    @GetMapping("/admin")
    public ModelAndView showAllUsers(@RequestParam(required = false, defaultValue = "0") String pageNumber) {
        ModelAndView modelAndView = createModelAndView("/admin/users_dashboard");
        createUsersToModelAndView(modelAndView, pageNumber);
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

    @PostMapping("/admin/user/create")
    public ModelAndView createUser(@ModelAttribute User user) {
        String error = "";
        if (userService.getByEmail(user.getEmail()) != null || userService.getUserByLogin(user.getLogin()) != null) {
            error = "A user with that email or login already exists.";
        }
        if (user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getLogin().isEmpty()) {
            error = "Empty Email, Login or Password field.";
        }
        if (error.isEmpty()) {
            userService.addNewUser(user);
            return new ModelAndView("redirect:/admin");
        }
        ModelAndView modelAndView = createModelAndView("/admin/users_dashboard");
        modelAndView = createUsersToModelAndView(modelAndView, "0");
        modelAndView.addObject("creationUserError", error);
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
                             @RequestParam(value = "login", required = false) String login,
                             @RequestParam(value = "userEmail", required = true) String userEmail,
                             @RequestParam(value = "roles", required = true) String[] roles) {
        User user = userService.getByEmail(userEmail);
        if (user != null) {
            if (checkField(email)) user.setEmail(email);
            if (checkField(login)) user.setLogin(login);
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

    private ModelAndView createUsersToModelAndView(ModelAndView modelAndView, String pageNumber){
        int pNumber = Integer.parseInt(pageNumber);
        List<User> usersForView = userService.getUsersWithOffset(pNumber * COUNT_OF_RECORDS, COUNT_OF_RECORDS);
        long totalUsersCount = userService.getAll().size();
        long countOfPages = totalUsersCount/ COUNT_OF_RECORDS;
        if (totalUsersCount % COUNT_OF_RECORDS != 0) countOfPages++;
        modelAndView.addObject("users", usersForView);
        modelAndView.addObject("countOfPages", countOfPages);
        modelAndView.addObject("pageNumber", pNumber);
        return modelAndView;
    }

}
