package com.example.mobile.image;

import com.example.mobile.projects.entity.ProjectEntities;
import com.example.mobile.projects.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ProjectRepository projectRepository;
    private final String uploadDir = "uploads/images/"; // Relative to project root

    public ImageService(ImageRepository imageRepository, ProjectRepository projectRepository) {
        this.imageRepository = imageRepository;
        this.projectRepository = projectRepository;
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public ImageEntity uploadImage(MultipartFile file, String projectId, String title) throws IOException {
        Optional<ProjectEntities> projectOpt = projectRepository.findById(projectId);
        if (projectOpt.isEmpty()) {
            throw new RuntimeException("Project not found");
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        ImageEntity image = new ImageEntity(filePath.toString(), fileName, title, projectOpt.get());
        return imageRepository.save(image);
    }

    public List<ImageEntity> getImagesByProject(String projectId) {
        return imageRepository.findByProject_ProjectId(projectId);
    }

    public Optional<ImageEntity> getImageById(Long id) {
        return imageRepository.findById(id);
    }

    public Path getImagePath(String fileName) {
        return Paths.get(uploadDir + fileName);
    }
}