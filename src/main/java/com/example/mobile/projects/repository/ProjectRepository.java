package com.example.mobile.projects.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.mobile.projects.entity.ProjectEntities;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntities, String> {
    boolean existsByProjectName(String projectName);

    @Query("SELECT p FROM ProjectEntities p LEFT JOIN FETCH p.technologiesUsed")
    Page<ProjectEntities> findAllWithDetails(Pageable pageable);

    @Query("SELECT p FROM ProjectEntities p LEFT JOIN FETCH p.technologiesUsed WHERE p.projectId = :id")
    Optional<ProjectEntities> findByIdWithDetails(String id);
}
