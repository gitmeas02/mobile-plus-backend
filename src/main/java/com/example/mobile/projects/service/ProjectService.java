package com.example.mobile.projects.service;

import java.util.List;

import com.example.mobile.projects.dto.CreateProjectRequestDTO;
import com.example.mobile.projects.model.ProjectModel;

public interface ProjectService {
   List<ProjectModel> getAllProjects(int page, int size);
    ProjectModel getProjectById(String projectId);
    ProjectModel createProject(CreateProjectRequestDTO createProjectRequestDTO);  
}
