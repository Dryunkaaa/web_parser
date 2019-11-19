package com.siteparser.service.parse.stats;

import com.detectlanguage.DetectLanguage;
import com.detectlanguage.errors.APIError;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class DetectLanguageService {

    private static final String API_KEY = "3460c2c2e88c0b89d4e17924d766f510";

    public DetectLanguageService(){
        DetectLanguage.apiKey = API_KEY;
    }

    public String detectLanguage(Document document){
        String language = null;
        String text = document.body().text();
        try {
            language = DetectLanguage.simpleDetect(text);
        } catch (APIError apiError) {
            apiError.printStackTrace();
        } catch (Exception e){
            return "unknown";
        }
        return language;
    }
}
