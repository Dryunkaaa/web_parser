package com.siteparser.service.jpa;

import com.siteparser.domain.Page;
import com.siteparser.domain.Project;
import com.siteparser.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PageService {

    @Autowired
    private PageRepository pageRepository;

    public Page save(Page page) {
        long projectId = page.getProject().getId();
        Page existingPage = pageRepository.findByUrl(page.getUrl(), projectId);
        if (existingPage != null && page.getId() == 0) return page;
        pageRepository.save(page);
        return page;
    }

    public Page getById(long id) {
        return pageRepository.findById(id).get();
    }

    public Page findByUrl(String url, long projectId) {
        return pageRepository.findByUrl(url, projectId);
    }

    public List<Page> getAll(long projectId) {
        return pageRepository.findAllByProjectId(projectId);
    }

    public void delete(Page page) {
        pageRepository.delete(page);
    }

    public Page findByUrl(String url) {
        return pageRepository.findByUrl(url);
    }

    public boolean hasUnparsedProjectPages(Project project) {
        return pageRepository.findAllUnparsedPages(project.getId()).size() > 0;
    }

    public List<Page> getUnparsedProjectPages(Project project) {
        return pageRepository.findAllUnparsedPages(project.getId());
    }

    public void clean() {
        pageRepository.deleteAll();
    }

    public void deletePageByUrl(String ulr){
        delete(findByUrl(ulr));
    }

    public Set<Page> findByKeywordsInUrl(Project project, String[] keywords) {
        Set<Page> result = new HashSet<>();
        for (String keyword : keywords) {
            result.addAll(pageRepository.findKeywordsInUrl(project.getId(), keyword));
        }
        return result;
    }

    public Set<Page> findByKeywordsInTitle(Project project, String[] keywords) {
        Set<Page> result = new HashSet<>();
        for (String keyword : keywords) {
            result.addAll(pageRepository.findKeywordsInTitle(project.getId(), keyword));
        }
        return result;
    }

    public Set<Page> findByKeywordsInDescription(Project project, String[] keywords) {
        Set<Page> result = new HashSet<>();
        for (String keyword : keywords) {
            result.addAll(pageRepository.findKeywordsInDescription(project.getId(), keyword));
        }
        return result;
    }

    public Set<Page> findByKeywordsInContent(Project project, String[] keywords) {
        Set<Page> result = new HashSet<>();
        for (String keyword : keywords) {
            result.addAll(pageRepository.findKeywordsInContent(project.getId(), keyword));
        }
        return result;
    }

    public List<Page> getPagesByProjectIdWithOffset(Project project, long count, long offset){
        return pageRepository.getPagesByProjectIdWithOffset(project.getId(),count,offset);
    }

    public void deleteAllByType(String type, long projectId){
        pageRepository.deleteAllByTypeInUrl(type, projectId);
    }
}
