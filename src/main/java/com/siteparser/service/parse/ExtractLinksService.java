package com.siteparser.service.parse;

import com.siteparser.domain.Project;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExtractLinksService {

    @Autowired
    private UrlService urlService;

    public List<String> extract(Project project, Document document) {
        List<String> linksToParse = new ArrayList<>();
        Elements links = document.select("a");
        for (Element element : links) {
            String link = element.absUrl("href");
            boolean hasDomainName = false;
            if (link.startsWith(project.getDomain())) hasDomainName = true;
            if (hasDomainName) {
                link = urlService.normalize(link);
                linksToParse.add(link);
            }
        }
        return linksToParse;
    }

}
