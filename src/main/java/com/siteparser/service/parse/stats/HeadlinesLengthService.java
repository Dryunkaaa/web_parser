package com.siteparser.service.parse.stats;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class HeadlinesLengthService {

    private static final String HEAD_TAGS[] = new String[]{
            "h1", "h2", "h3", "h4", "h5", "h6"
    };

    public int getHeadlinesAmount(Document document) {
        int length = 0;
        for (String head : HEAD_TAGS) {
            length += document.select(head).size();
        }

        return length;
    }
}
