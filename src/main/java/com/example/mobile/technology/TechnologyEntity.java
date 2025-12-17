package com.example.mobile.technology;

import jakarta.persistence.*;
import java.util.List;
import com.example.mobile.projects.entity.ProjectEntities;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "technologies")
public class TechnologyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "technology_id", updatable = false, nullable = false)
    private String technologyId;

    @Column(name = "technology_name", nullable = false, unique = true)
    private String technologyName;

    @Column(length = 500)
    private String description;

    @ManyToMany(mappedBy = "technologiesUsed")
    @JsonIgnore
    private List<ProjectEntities> projects;

    // Constructors
    public TechnologyEntity() {}

    public TechnologyEntity(String technologyName, String description) {
        this.technologyName = technologyName;
        this.description = description;
    }

    // Getters and Setters
    public String getTechnologyId() {
        return technologyId;
    }

    public void setTechnologyId(String technologyId) {
        this.technologyId = technologyId;
    }

    public String getTechnologyName() {
        return technologyName;
    }

    public void setTechnologyName(String technologyName) {
        this.technologyName = technologyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProjectEntities> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectEntities> projects) {
        this.projects = projects;
    }
}
