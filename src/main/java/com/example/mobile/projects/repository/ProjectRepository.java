package com.example.mobile.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mobile.projects.entity.ProjectEntities;

public interface ProjectRepository extends JpaRepository<ProjectEntities, String> {
    
}
