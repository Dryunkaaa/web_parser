package com.siteparser.service.parse;

import org.springframework.stereotype.Service;

@Service
public class CleanTextService {

    public String clean(String text) {
        if (text == null) return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (isValid(c)) stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    private boolean isValid(char c) {
        if (Character.isDigit(c)) return true;

        if (Character.isAlphabetic(c)) return true;

        if (Character.isLetterOrDigit(c)) return true;

        if (Character.isSpaceChar(c)) return true;

        return false;
    }
}
