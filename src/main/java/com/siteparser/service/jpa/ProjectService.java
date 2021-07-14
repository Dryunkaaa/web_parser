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

    public List<Project> getAllWithEnabledParsing() {
        return projectRepository.findAllWithEnableParsing();
    }

    public void delete(Project project) {
        projectRepository.delete(project);
    }

    public List<Project> getAll(User user) {
        return projectRepository.getAllByUserId(user.getId());
    }

    public boolean exist(long projectId) {
        return projectRepository.existsById(projectId);
    }
}
