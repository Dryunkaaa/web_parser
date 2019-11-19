package com.siteparser.service.search;

import com.siteparser.domain.Page;
import com.siteparser.domain.Project;
import com.siteparser.service.jpa.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SearchService {

    @Autowired
    public PageService pageService;

    public static class SearchSpecification {
        public static final SearchSpecification ALL = new SearchSpecification();
        public boolean url = true;
        public boolean content = true;
        public boolean title = true;
        public boolean description = true;

        public void reset(){
            url = false;
            content = false;
            title = false;
            description = false;
        }

        public static SearchSpecification load(String[] values) {
            SearchSpecification searchSpecification = new SearchSpecification();
            searchSpecification.reset();
            if (values == null) return searchSpecification;
            for (String value : values){
                if (value.equals("title")) searchSpecification.title = true;
                else if (value.equals("description")) searchSpecification.description = true;
                else if (value.equals("url")) searchSpecification.url = true;
                else if(value.equals("content")) searchSpecification.content = true;
            }
            return searchSpecification;
        }
    }

    public Set<Page> search(Project project, String ... keywords){
        return search(project, SearchSpecification.ALL, keywords);
    }

    public Set<Page> search(Project project, SearchSpecification searchSpecification, String... keywords) {
        Set<Page> result = new HashSet<>();
        if (searchSpecification.url) result.addAll(pageService.findByKeywordsInUrl(project, keywords));

        if (searchSpecification.title) result.addAll(pageService.findByKeywordsInTitle(project, keywords));

        if (searchSpecification.description) result.addAll(pageService.findByKeywordsInDescription(project, keywords));

        if (searchSpecification.content) result.addAll(pageService.findByKeywordsInContent(project, keywords));

        return result;
    }
}
