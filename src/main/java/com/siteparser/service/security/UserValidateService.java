package com.siteparser.service.security;

import com.siteparser.domain.User;
import com.siteparser.service.jpa.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserValidateService {

    @Autowired
    private UserService userService;

    public enum ValidateResult {
        ok,
        invalidEmail,
        shortPassword,
        userExists,
        invalidPassword;

        public String getStringRepresentation() {
            switch (this) {
                case ok:
                    return "OK";
                case invalidEmail:
                    return "Incorrect email.";
                case shortPassword:
                    return "Password is short! Password must be more than 5 characters.";
                case userExists:
                    return "A user with this email or login already exists.";
                case invalidPassword:
                    return "Incorrect password.";
                default:
                    return "";
            }
        }
    }

    public ValidateResult validate(User user) {
        if (userService.getByEmail(user.getEmail()) != null || userService.getUserByLogin(user.getLogin()) != null) {
            return ValidateResult.userExists;
        }
        if (user.getPassword().length() < 5) return ValidateResult.shortPassword;
        if (!isValidEmailAddress(user.getEmail())) return ValidateResult.invalidEmail;
        return ValidateResult.ok;
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public ValidateResult validatePassword(User user, String oldPassword, String newPassword) {
        if (newPassword.length() < 5) return ValidateResult.shortPassword;
        // сравнение паролей
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) return ValidateResult.invalidPassword;
        return ValidateResult.ok;
    }
}
