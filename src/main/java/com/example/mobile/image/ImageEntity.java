package com.example.mobile.image;

import com.example.mobile.projects.entity.ProjectEntities;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class ImageEntity { // project images table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_path", nullable = false)
    private String filePath; // Store the relative path or URL

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "title", nullable = true)
    private String title;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntities project;

    // Constructors
    public ImageEntity() {}

    public ImageEntity(String filePath, String fileName, String title, ProjectEntities project) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.title = title;
        this.project = project;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ProjectEntities getProject() {
        return project;
    }

    public void setProject(ProjectEntities project) {
        this.project = project;
    }
}
