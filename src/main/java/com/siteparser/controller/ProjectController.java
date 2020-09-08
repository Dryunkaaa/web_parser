package com.siteparser.controller;

import com.siteparser.domain.Page;
import com.siteparser.domain.Project;
import com.siteparser.domain.User;
import com.siteparser.service.export.PageCsvExporter;
import com.siteparser.service.jpa.PageService;
import com.siteparser.service.jpa.ProjectService;
import com.siteparser.service.jpa.UserService;
import com.siteparser.service.search.SearchService;
import com.siteparser.service.security.SecurityProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ProjectController extends BaseSecurityController {

    private final static int COUNT_OF_RECORDS = 20;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PageCsvExporter pageCsvExporter;

    @Autowired
    private PageService pageService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityProcessorService securityProcessorService;

    @Autowired
    private SearchService searchService;

    private User getCurrentUser() {
        String userEmail = securityProcessorService.getCurrentUserEmail();
        return userService.getByEmail(userEmail);
    }

    @GetMapping("/projects")
    public ModelAndView getAllProjects(@RequestParam(required = false, defaultValue = "0") String pageNumber) {
        ModelAndView modelAndView = createModelAndView("/project/projects");
        User user = getCurrentUser();
        int pNumber = Integer.parseInt(pageNumber);
        List<Project> allProjects = projectService.getAll(user);
        List<Project> projectsToShow = allProjects.stream()
                .skip(pNumber * COUNT_OF_RECORDS)
                .limit(COUNT_OF_RECORDS).collect(Collectors.toList());
        int countOfPages = allProjects.size() / COUNT_OF_RECORDS;
        if (allProjects.size() % COUNT_OF_RECORDS != 0) countOfPages++;
        modelAndView.addObject("projects", projectsToShow);
        modelAndView.addObject("pageNumber", pNumber);
        modelAndView.addObject("countOfPages", countOfPages);
        modelAndView.addObject("totalCountOfProjects", allProjects.size());
        return modelAndView;
    }

    @GetMapping("/project/get/{projectId}")
    @ResponseBody
    public Object getById(@PathVariable long projectId) {
        if (projectService.exist(projectId)) return projectService.getById(projectId);
        else return "Project not found";
    }

    @GetMapping("/project/changeParsingState")
    public String changeParsingState(@RequestParam(name = "projectId", required = true) long projectId,
                                     @RequestParam(name = "parsingStatus", required = true) boolean parsingStatus) {
        if (projectService.exist(projectId)) {
            Project project = projectService.getById(projectId);
            project.setParsingStatus(!parsingStatus);
            projectService.saveProject(project);
        }
        return "redirect:/projects";
    }

    @GetMapping("/project/delete")
    public String deleteProject(@RequestParam long projectId) {
        if (projectService.exist(projectId)) {
            projectService.delete(projectService.getById(projectId));
        }
        return "redirect:/projects";
    }

    @PostMapping("/project/add")
    public String addProject(@ModelAttribute Project project) {
        project.setUser(getCurrentUser());
        projectService.saveProject(project);
        return "redirect:/projects";
    }

    @PostMapping("/project/export")
    @ResponseBody
    public byte[] exportProject(@RequestParam(value = "searchSpecification", required = false) String[] searchSpecifications,
                                @RequestParam(value = "searchPhrases") String searchPhrases,
                                @RequestParam("projectId") long projectId,
                                HttpServletRequest request, HttpServletResponse response) throws IOException {

        SearchService.SearchSpecification searchSpecification = SearchService.SearchSpecification.load(searchSpecifications);

        Set<Page> pages = searchService.search(projectService.getById(projectId), searchSpecification, searchPhrases);

        String csv = pageCsvExporter.exportPages(pages);

        //Download file
        response.setContentType("application/text; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"pages.csv\"");

        //creating byteArray stream, make it bufforable and passing this buffor to ZipOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(csv.getBytes(StandardCharsets.UTF_8));

        return byteArrayOutputStream.toByteArray();
    }
}
