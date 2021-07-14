package com.siteparser.service.parse.stats;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;


@Service
public class ParagraphLengthService {

    private static final int MIN_PARAGRAPH_CHARS_COUNT = 20;

    public int getParagraphsAmount(Document document) {
        int length = 0;

        for (Element element : document.select("p")) {
            if (element.text().length() >= MIN_PARAGRAPH_CHARS_COUNT) {
                length++;
            }
        }

        return length;
    }
}
