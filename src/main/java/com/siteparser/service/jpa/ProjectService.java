package com.siteparser.service.jpa;

import com.siteparser.domain.Page;
import com.siteparser.domain.Project;
import com.siteparser.domain.User;
import com.siteparser.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PageService pageService;

    public Project saveProject(Project project) {
        projectRepository.save(project);
        /*
        * при отсутствии страниц у проекта создается страница, у которой url == домену проекта
         */
        if (project.getPages().size() == 0) {
            Page page = new Page();
            page.setUrl(project.getDomain());
            page.setProject(project);
            pageService.save(page);
        }
        return project;
    }

    public Project getById(long projectId) {
        return projectRepository.findById(projectId).get();
    }

    public List<Project> getAllByDomain(String domain) {
        return projectRepository.findAllByDomain(domain);
    }

    public Project getByDomain(String domain) {
        return projectRepository.findByDomain(domain);
    }

    public List<Project> getAllWithEnabledParsing() {
        return projectRepository.findAllWithEnableParsing();
    }

    public void delete(Project project) {
        projectRepository.delete(project);
    }

    public void clean() {
        projectRepository.deleteAll();
    }

    public boolean existById(long projectId) {
        return projectRepository.existsById(projectId);
    }

    public List<Project> getAll(User user) {
        return projectRepository.getAllByUserId(user.getId());
    }

    public boolean exist(long projectId) {
        return projectRepository.existsById(projectId);
    }
}
