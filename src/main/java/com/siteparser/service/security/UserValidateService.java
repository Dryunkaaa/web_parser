package com.siteparser.service.security;

import com.siteparser.domain.User;
import com.siteparser.service.jpa.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserValidateService {

    @Autowired
    private UserService userService;

    private static final Pattern EMAIL_PATTERN = initEmailPattern();

    public enum ValidateResult {
        OK,
        INVALID_EMAIL,
        SHORT_PASSWORD,
        USER_EXISTS,
        INVALID_PASSWORD;

        public String getStringRepresentation() {
            switch (this) {
                case OK:
                    return "OK";
                case INVALID_EMAIL:
                    return "Incorrect email.";
                case SHORT_PASSWORD:
                    return "Password is short! Password must be more than 5 characters.";
                case USER_EXISTS:
                    return "A user with this email or login already exists.";
                case INVALID_PASSWORD:
                    return "Incorrect password.";
                default:
                    return "";
            }
        }
    }

    public ValidateResult validate(User user) {
        if (userService.getByEmail(user.getEmail()) != null || userService.getUserByLogin(user.getLogin()) != null) {
            return ValidateResult.USER_EXISTS;
        }

        if (user.getPassword().length() < 5) {
            return ValidateResult.SHORT_PASSWORD;
        }
        if (!isValidEmailAddress(user.getEmail())) {
            return ValidateResult.INVALID_EMAIL;
        }

        return ValidateResult.OK;
    }

    public boolean isValidEmailAddress(String email) {
        Matcher m = EMAIL_PATTERN.matcher(email);
        return m.matches();
    }

    public ValidateResult validatePassword(User user, String oldPassword, String newPassword) {
        if (newPassword.length() < 5) {
            return ValidateResult.SHORT_PASSWORD;
        }

        if (!passwordsEquals(oldPassword, user.getPassword())) {
            return ValidateResult.INVALID_PASSWORD;
        }

        return ValidateResult.OK;
    }

    private boolean passwordsEquals(String firstPassword, String secondPassword) {
        return BCrypt.checkpw(firstPassword, secondPassword);
    }

    private static Pattern initEmailPattern() {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        return Pattern.compile(ePattern);
    }
}
