package com.example.mobile.projects.service.impl;

import org.springframework.stereotype.Service;

import com.example.mobile.projects.dto.CreateProjectRequestDTO;
import com.example.mobile.projects.entity.ProjectEntities;
import com.example.mobile.projects.mapper.ProjectMapper;
import com.example.mobile.projects.model.ProjectModel;
import com.example.mobile.projects.repository.ProjectRepository;
import com.example.mobile.projects.service.ProjectService;
import com.example.mobile.user.repository.UserRepository;
import com.example.mobile.user.entity.UserEntity;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
     private final ProjectRepository projectRepository;
     private final UserRepository userRepository;

     public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
     }
    // create project method

    @Override
    public ProjectModel createProject(CreateProjectRequestDTO request){
        ProjectEntities project = new ProjectEntities();
        project.setProjectName(request.getProjectName());
        project.setProjectDescription(request.getProjectDescription());
        project.setLinkedInUrl(request.getLinkedInUrl());
        
        // Fetch UserEntity objects from user IDs
        List<String> userIds = request.getTeamMembers();
        if (userIds != null && !userIds.isEmpty()) {
            List<UUID> uuids = userIds.stream().map(UUID::fromString).collect(Collectors.toList());
            List<UserEntity> users = userRepository.findAllById(uuids);
            project.setTeamMembers(users);
        }
        
        project.setImageUrls(request.getImageUrls());
        project.setTechnologiesUsed(request.getTechnologiesUsed());

        ProjectEntities savedProject = projectRepository.save(project);
        return ProjectMapper.toModel(savedProject);
    };
    // get all projects method
    
    @Override
    public List<ProjectModel> getAllProjects(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return projectRepository.findAll(pageable)
                .stream()
                .map(ProjectMapper::toModel)
                .collect(Collectors.toList());
    }


    @Override
    public ProjectModel getProjectById(String projectId) {
        return projectRepository.findById(projectId)
                .map(ProjectMapper::toModel)
                .orElse(null);
    }

    // 🔁 Entity → Model mapper
    // private ProjectModel mapToModel(ProjectEntities entity) {
    //     return new ProjectModel(
    //             entity.getProjectId(),
    //             entity.getProjectName(),
    //             entity.getProjectDescription(),
    //             entity.getLinkedInUrl(),
    //             entity.getTeamMembers(),
    //             entity.getImageUrls(),
    //             entity.getTechnologiesUsed(),
    //             entity.getCreatedAt()
    //     );
    // }

}
