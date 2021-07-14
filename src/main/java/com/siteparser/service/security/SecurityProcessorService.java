package com.siteparser.service.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Service
public class SecurityProcessorService {

    public User getUserFromContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
            return null;
        }

        return (User) authentication.getPrincipal();
    }

    public String getCurrentUserEmail() {
        return getUserFromContext().getUsername();
    }

    public ModelAndView createModelAndView(String viewName) {
        ModelAndView modelAndView = new ModelAndView(viewName);

        User principal = getUserFromContext();
        modelAndView.addObject("user", principal);
        modelAndView.addObject("roles", getUserRolesAsString());
        modelAndView.addObject("isAdmin", hasRole("ADMIN"));

        return modelAndView;
    }

    private boolean hasRole(String role) {
        if (getUserFromContext() == null) {
            return false;
        }

        Collection<GrantedAuthority> authorities = getUserFromContext().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().contains(role)) {
                return true;
            }
        }

        return false;
    }

    public String getUserRolesAsString() {
        if (getUserFromContext() == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        for (GrantedAuthority authority : getUserFromContext().getAuthorities()) {
            if (result.length() > 0) {
                result.append("  ");
            }

            result.append(authority.getAuthority());
        }

        return result.toString();
    }
}
