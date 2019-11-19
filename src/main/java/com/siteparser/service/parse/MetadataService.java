package com.siteparser.service.parse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class MetadataService {

    public String getTitle(Document document) {
        return document.title();
    }

    public String getDescription(Document document) {
        String description = null;
        Elements elements = document.select("meta");

        for (Element element : elements) {
            if (element.hasAttr("name") && element.attr("name").equals("description")) {
                description = element.attr("content");
                break;
            }
        }
        return description;
    }
}
