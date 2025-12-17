package com.example.mobile.image;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("projectId") String projectId,
            @RequestParam(value = "title", required = false) String title) {
        try {
            ImageEntity image = imageService.uploadImage(file, projectId, title);
            return ResponseEntity.ok("Image uploaded successfully with ID: " + image.getId());
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload image: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Project not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ImageModel>> getImagesByProject(@PathVariable String projectId) {
        List<ImageEntity> images = imageService.getImagesByProject(projectId);
        List<ImageModel> imageModels = images.stream()
                .map(img -> new ImageModel(img.getId(), img.getFileName(), img.getTitle(), "/images/download/" + img.getId()))
                .toList();
        return ResponseEntity.ok(imageModels);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long id) {
        Optional<ImageEntity> imageOpt = imageService.getImageById(id);
        if (imageOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path filePath = imageService.getImagePath(imageOpt.get().getFileName());
            @SuppressWarnings("null")
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageOpt.get().getFileName() + "\"")
                        .contentType(MediaType.parseMediaType("image/jpeg")) // Adjust based on image type
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}