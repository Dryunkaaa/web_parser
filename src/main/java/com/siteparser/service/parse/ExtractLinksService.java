package com.siteparser.service.parse;

import com.siteparser.domain.Project;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExtractLinksService {

    @Autowired
    private UrlService urlService;

    public List<String> extractAnotherSiteLinks(Project project, Document document) {
        List<String> pageLinks = new ArrayList<>();

        for (Element element : document.select("a")) {
            String link = element.absUrl("href");

            if (link.startsWith(project.getDomain())) {
                link = urlService.normalize(link);
                pageLinks.add(link);
            }
        }

        return pageLinks;
    }
}
