package com.example.mobile.projects.dto;
import java.util.List;
public class CreateProjectRequestDTO {
    private String projectName;
    private String projectDescription;
    private String linkedInUrl;
    private List<String> teamMembers; // nullable 
    private List<String> imageUrls; // nullable
    private List<String> technologiesUsed; // nullable

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public List<String> getTeamMembers() {
        return teamMembers;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public List<String> getTechnologiesUsed() {
        return technologiesUsed;
    }
}
