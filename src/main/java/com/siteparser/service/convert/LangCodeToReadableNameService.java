package com.siteparser.service.convert;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class LangCodeToReadableNameService {

    private static HashMap<String, String> languages = null;

    public LangCodeToReadableNameService() {
        languages = new HashMap<>();
        languages.put("uk", "Украинский");
        languages.put("ru", "Русский");
        languages.put("en", "Английский");
    }

    public String getReadableName(String lang) {
        if (languages.containsKey(lang)) return languages.get(lang);
        return lang;
    }
}
