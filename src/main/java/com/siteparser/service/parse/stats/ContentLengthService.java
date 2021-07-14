package com.siteparser.service.parse.stats;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class ContentLengthService {

    public int parseContentLength(Document document) {
        String content = document.body().text();
        content = getTextWithoutSpaces(content);
        return content.length();
    }

    private String getTextWithoutSpaces(String content) {
        return content.replaceAll("\\s", "");
    }
}
