package com.example.mobile.projects.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.example.mobile.user.entity.UserEntity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "projects") 
// this is project table that has relation with users 
// technologies used, team members
public class ProjectEntities {

   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "project_id", updatable = false, nullable = false)
    private String projectId;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(length = 600)
    private String projectDescription;

    @Column(name = "link_url", nullable = true)
    private String LinkedInUrl;
    
/* ------------ Team Members (Users Table) ------------ */
    @ManyToMany
    @JoinTable(
        name = "project_users",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> teamMembers;

      
    @ElementCollection
    @CollectionTable(name = "project_images", joinColumns = @JoinColumn(name = "project_id"))
    private List<String> imageUrls;
    
    @ElementCollection
    @CollectionTable(name = "technologies", joinColumns = @JoinColumn(name = "project_id"))
    private List<String> technologiesUsed;
  
    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

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
        return LinkedInUrl;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        LinkedInUrl = linkedInUrl;
    }


    public List<UserEntity> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<UserEntity> teamMembers) {
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

}
