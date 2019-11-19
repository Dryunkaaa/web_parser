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
        try{
            document = Jsoup.connect(url).get();
        }catch (HttpStatusException ep){
            if (ep.getStatusCode() == 404){
                pageService.deletePageByUrl(url);
            }
        }catch (org.jsoup.UnsupportedMimeTypeException umte){ // если невозможно получить док(.pdf, .mp3)
            // получаем тип из url
            String[] parts = url.split("\\.");
            String type = parts[parts.length-1];
            Page page = pageService.findByUrl(url, project.getId());
            pageService.deleteAllByType(type,page.getProject().getId());
        }
        return document;
    }

//    public String loadHtml(String url){
//        String result = null;
//        try {
//            result = loadDocument(url).html();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

}
