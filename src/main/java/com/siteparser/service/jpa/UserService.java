package com.siteparser.service.jpa;

import com.siteparser.domain.Role;
import com.siteparser.domain.User;
import com.siteparser.repository.UserRepository;
import com.siteparser.service.security.SecurityProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SecurityProcessorService securityProcessorService;

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void addNewUser(User user) {
        Date createdDate = new Date();
        Role userRole = roleService.getRoleByName("USER");

        user.getRoles().add(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedDate(createdDate);
        user.setCreatedTime(createdDate);

        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void update(User user) {
        Date modifiedDate = new Date();
        user.setLastModifiedDate(modifiedDate);
        user.setLastModifiedTime(modifiedDate);
        user.setLastModifiedBy(securityProcessorService.getUserFromContext().getUsername());

        userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void createAdmin() {
        final String adminEmail = "admin@localhost";
        final String adminLogin = "admin";
        final Date createdDate = new Date();

        createAdminAndUserRoleIfNotExist();

        User admin = new User();
        admin.setLogin(adminLogin);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminLogin));
        admin.setCreatedDate(createdDate);
        admin.setCreatedTime(createdDate);
        admin.getRoles().add(roleService.getRoleByName("USER"));
        admin.getRoles().add(roleService.getRoleByName("ADMIN"));

        userRepository.save(admin);
    }

    private void createAdminAndUserRoleIfNotExist() {
        if (roleService.getRoleByName("USER") == null) {
            roleService.createRole("USER");
        }

        if (roleService.getRoleByName("ADMIN") == null) {
            roleService.createRole("ADMIN");
        }
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public List<User> getUsersWithOffset(long offset, long count) {
        return userRepository.getUsersWithOffset(offset, count);
    }
}
