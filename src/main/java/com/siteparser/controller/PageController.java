package com.siteparser.controller;

import com.siteparser.domain.Page;
import com.siteparser.domain.Project;
import com.siteparser.service.convert.ContentFormatService;
import com.siteparser.service.jpa.PageService;
import com.siteparser.service.jpa.ProjectService;
import com.siteparser.service.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class PageController extends BaseSecurityController {

    public static final int COUNT_OF_PAGES = 50;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PageService pageService;

    @Autowired
    private ContentFormatService contentFormatService;

    @Autowired
    private SearchService searchService;

    @GetMapping("/project/pages")
    public ModelAndView getPages(@RequestParam long projectId,
                                 @RequestParam(required = false, defaultValue = "0") String pageNumber) {
        ModelAndView modelAndView = createModelAndView("/project/page/pages");
        int pNumber = Integer.parseInt(pageNumber);
        if (projectService.exist(projectId)) {
            Map<Long, String> content = new HashMap<>();
            // нумерация страниц
            Map<Long, Long> indexes = new HashMap<>();

            Project project = projectService.getById(projectId);
            List<Page> pages = pageService.getPagesByProjectIdWithOffset(project, COUNT_OF_PAGES, pNumber * COUNT_OF_PAGES);
            // текущий номер на данной странице
            long index = pNumber * COUNT_OF_PAGES;

            for (Page page : pages) {
                // если контент слишком большой - урезаем его
                content.put(page.getId(), contentFormatService.formatContent(page.getContent()));
                // страницы отображаются для пользователя начиная не с 0, а с 1
                indexes.put(page.getId(), index + 1);
                index++;
            }

            long totalPageCountInProject = pageService.getAll(projectId).size();
            long pagesCount = totalPageCountInProject / COUNT_OF_PAGES;
            // если на след странице еще хранятся данные и она не полная
            if (totalPageCountInProject % COUNT_OF_PAGES != 0) pagesCount++;
            modelAndView.addObject("pages", pages);
            modelAndView.addObject("project", project);
            modelAndView.addObject("content", content);
            modelAndView.addObject("indexes", indexes);
            modelAndView.addObject("pageNumber", pNumber);
            modelAndView.addObject("pagesCount", pagesCount);
        }

        return modelAndView;
    }

    @PostMapping("/project/pages/search")
    public ModelAndView search(@RequestParam(value = "searchSpecification", required = false) String[] searchSpecifications,
                               @RequestParam(value = "searchPhrases") String searchPhrases,
                               @RequestParam("projectId") long projectId) {
        ModelAndView modelAndView = createModelAndView("/project/page/pages");
        SearchService.SearchSpecification specification = SearchService.SearchSpecification.load(searchSpecifications);
        Project project = projectService.getById(projectId);
        Set<Page> pages = searchService.search(project, specification, searchPhrases);
        long index = 1;
        Map<Long, Long> indexes = new HashMap<>();
        Map<Long, String> content = new HashMap<>();
        for (Page page : pages){
            indexes.put(page.getId(), index);
            content.put(page.getId(), contentFormatService.formatContent(page.getContent()));
            index++;
        }

        modelAndView.addObject("pages", pages);
        modelAndView.addObject("project", project);
        modelAndView.addObject("content", content);
        modelAndView.addObject("indexes", indexes);
        modelAndView.addObject("searchPhrase", searchPhrases);
        modelAndView.addObject("searchInTitle", specification.title);
        modelAndView.addObject("searchInDescription", specification.description);
        modelAndView.addObject("searchInUrl", specification.url);
        modelAndView.addObject("searchInContent", specification.content);
        return modelAndView;
    }

}
