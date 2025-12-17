package com.example.mobile.projects.model;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectModel { // for frontend communication

    private String projectId;
    private String projectName;
    private String projectDescription;
    private String linkedInUrl;
    private List<String> teamMembers;
    private List<String> imageUrls;
    private List<String> technologiesUsed;
    private LocalDateTime createdAt;

    /* ------------ Constructors ------------ */

    public ProjectModel() {
    }

    public ProjectModel(
            String projectId,
            String projectName,
            String projectDescription,
            String linkedInUrl,
            List<String> teamMembers,
            List<String> imageUrls,
            List<String> technologiesUsed,
            LocalDateTime createdAt) {

        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.linkedInUrl = linkedInUrl;
        this.teamMembers = teamMembers;
        this.imageUrls = imageUrls;
        this.technologiesUsed = technologiesUsed;
        this.createdAt = createdAt;
    }

    /* ------------ Getters & Setters ------------ */

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }

    public List<String> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<String> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<String> getTechnologiesUsed() {
        return technologiesUsed;
    }

    public void setTechnologiesUsed(List<String> technologiesUsed) {
        this.technologiesUsed = technologiesUsed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
