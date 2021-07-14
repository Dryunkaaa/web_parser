package com.siteparser.service.parse;

import org.springframework.stereotype.Service;

@Service
public class CleanTextService {

    public String clean(String text) {
        if (text == null) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (isCharacterValid(c)) {
                stringBuilder.append(c);
            }
        }

        return stringBuilder.toString();
    }

    private boolean isCharacterValid(char c) {
        return Character.isDigit(c) || Character.isAlphabetic(c)
                || Character.isLetterOrDigit(c) || Character.isSpaceChar(c);
    }
}
