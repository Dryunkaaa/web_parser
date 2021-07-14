package com.siteparser.service.parse;

import com.siteparser.domain.Page;
import com.siteparser.domain.Project;
import com.siteparser.service.jpa.PageService;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HtmlLoadService {

    @Autowired
    private PageService pageService;

    public Document loadDocument(String url, Project project) throws IOException {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (HttpStatusException ep) {
            if (ep.getStatusCode() == 404) {
                pageService.deletePageByUrl(url);
            }
        } catch (org.jsoup.UnsupportedMimeTypeException umte) { // если невозможно получить док(.pdf, .mp3)
            Page page = pageService.findByUrl(url, project.getId());
            pageService.deleteAllByType(getUrlType(url), page.getProject().getId());
        }

        return document;
    }

    private String getUrlType(String url) {
        String[] parts = url.split("\\.");
        return parts[parts.length - 1];
    }
}
