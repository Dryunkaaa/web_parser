package com.siteparser.service.parser;

import com.siteparser.domain.Page;
import com.siteparser.domain.Project;
import com.siteparser.service.jpa.PageService;
import com.siteparser.service.jpa.ProjectService;
import com.siteparser.service.parse.CleanTextService;
import com.siteparser.service.parse.ExtractLinksService;
import com.siteparser.service.parse.HtmlLoadService;
import com.siteparser.service.parse.MetadataService;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ParserService {

    @Autowired
    private HtmlLoadService htmlLoadService;

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private CleanTextService cleanTextService;

    @Autowired
    private PageService pageService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ExtractLinksService extractLinksService;

    @Scheduled(fixedDelay = 1000)
    public void parse() {
        for (Project project : projectService.getAllWithEnabledParsing()) {
            if (pageService.hasUnparsedProjectPages(project)) {
                parseProject(project);
                break;
            } else {
                project.setParsingStatus(false);
                projectService.saveProject(project);
            }
        }
    }

    private void parseProject(Project project) {
        projectService.saveProject(project);

        List<Page> pagesToParse = pageService.getUnparsedProjectPages(project);
        Page firstPage = pagesToParse.get(0);
        parsePage(firstPage, project);
    }

    private void parsePage(Page page, Project project) {
        Document document = null;
        try {
            document = htmlLoadService.loadDocument(page.getUrl(), project);

            if (document != null) {
                fillPageWithInfo(page, document);
                pageService.save(page);

                List<String> extractedLinks = extractLinksService.extractAnotherSiteLinks(page.getProject(), document);
                saveLinks(extractedLinks, page.getProject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillPageWithInfo(Page page, Document doc) {
        String title = cleanTextService.clean(metadataService.getTitle(doc));
        String description = cleanTextService.clean(metadataService.getDescription(doc));
        String content = cleanTextService.clean(doc.body().text());

        page.setTitle(title);
        page.setDescription(description);
        page.setContent(content);
    }

    private void saveLinks(List<String> extractedLinks, Project project) {
        for (String link : extractedLinks) {
            Page newPage = new Page();
            newPage.setUrl(link);
            newPage.setProject(project);

            pageService.save(newPage);
        }
    }
}
