package com.siteparser.service.parse.stats;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;


@Service
public class ParagraphLengthService {

    // минимальное кол-во символов в параграфе
    private static final int MIN_COUNT_OF_CHARACTERS = 20;

    public int getParagraphLength(Document document) {
        int length = 0;
        Elements elements = document.select("p");
        for (Element element : elements) {
            if (element.text().length() >= MIN_COUNT_OF_CHARACTERS) length++;
        }
        return length;
    }
}
