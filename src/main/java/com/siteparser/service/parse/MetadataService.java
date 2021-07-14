package com.siteparser.service.parse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class MetadataService {

    public String getTitle(Document document) {
        return document.title();
    }

    public String getDescription(Document document) {
        for (Element element : document.select("meta")) {
            if (element.hasAttr("name") && element.attr("name").equals("description")) {
                return element.attr("content");
            }
        }

        return "";
    }
}
