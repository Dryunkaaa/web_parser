package com.siteparser.service.parse.stats;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class ContentLengthService {

    public int parseContentLength(Document document){
        String content = document.body().text();
        content = cleanText(content);
        return content.length();
    }

    // убираем все пробелы
    private String cleanText(String content) {
        return content.replaceAll("\\s","");
    }
}
