package com.example.mobile.image;

import com.example.mobile.projects.entity.ProjectEntities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class ImageEntity {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filePath; // Store the relative path or URL
    
    // Other image properties like title, description, etc.
    private String title;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntities project;
}
