package com.siteparser.listener;

import com.siteparser.service.jpa.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AppReadyListener {

    @Autowired
    private UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void appReady() {
        if (userService.getByEmail("admin@localhost") == null) {
            userService.createAdmin();
        }
    }
}
