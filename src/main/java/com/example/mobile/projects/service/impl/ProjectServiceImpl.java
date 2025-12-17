package com.example.mobile.projects.service.impl;

import org.springframework.stereotype.Service;

import com.example.mobile.image.ImageEntity;
import com.example.mobile.image.ImageRepository;
import com.example.mobile.projects.service.ProjectService;
import com.example.mobile.projects.repository.ProjectRepository;
import com.example.mobile.projects.dto.CreateProjectRequestDTO;
import com.example.mobile.projects.model.ProjectModel;
import com.example.mobile.projects.entity.ProjectEntities;
import com.example.mobile.projects.mapper.ProjectMapper;
import com.example.mobile.user.repository.UserRepository;
import com.example.mobile.user.entity.UserEntity;
import com.example.mobile.technology.TechnologyRepository;
import com.example.mobile.technology.TechnologyEntity;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProjectServiceImpl implements ProjectService {
     private final ProjectRepository projectRepository;
     private final UserRepository userRepository;
     private final TechnologyRepository technologyRepository;
     private final ImageRepository imageRepository;

     public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository, TechnologyRepository technologyRepository, ImageRepository imageRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.technologyRepository = technologyRepository;
        this.imageRepository = imageRepository;
     }
    // create project method

    @Override
    public ProjectModel createProject(CreateProjectRequestDTO request){
        if (projectRepository.existsByProjectName(request.getProjectName())) {
            throw new RuntimeException("Project name already exists");
        }
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
        
        // Fetch TechnologyEntity objects from technology names, create if not exist
        List<String> techNames = request.getTechnologiesUsed();
        if (techNames != null && !techNames.isEmpty()) {
            List<TechnologyEntity> technologies = techNames.stream()
                .map(name -> technologyRepository.findByTechnologyName(name).orElseGet(() -> {
                    TechnologyEntity newTech = new TechnologyEntity();
                    newTech.setTechnologyName(name);
                    return technologyRepository.save(newTech);
                }))
                .collect(Collectors.toList());
            project.setTechnologiesUsed(technologies);
        }
        
        // Note: Images are handled separately via ImageController

        project.setCreatedDate(LocalDateTime.now());

        ProjectEntities savedProject = projectRepository.save(project);
        return ProjectMapper.toModel(savedProject);
    };
    // get all projects method
    
    @Override
    public List<ProjectModel> getAllProjects(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return projectRepository.findAllWithDetails(pageable)
                .stream()
                .map(ProjectMapper::toModel)
                .collect(Collectors.toList());
    }


    @Override
    public ProjectModel getProjectById(String projectId) {
        return projectRepository.findByIdWithDetails(projectId)
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

    @Override
    public void deleteProject(String projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found");
        }
        // Delete associated image files
        List<ImageEntity> images = imageRepository.findByProject_ProjectId(projectId);
        for (ImageEntity image : images) {
            try {
                Path filePath = Paths.get(image.getFilePath());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // Log the error, but continue
                System.err.println("Failed to delete file: " + image.getFilePath());
            }
        }
        projectRepository.deleteById(projectId);
    }
}
