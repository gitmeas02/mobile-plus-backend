package com.example.mobile.technology;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechnologyRepository extends JpaRepository<TechnologyEntity, String> {
    Optional<TechnologyEntity> findByTechnologyName(String technologyName);
    List<TechnologyEntity> findByTechnologyNameIn(List<String> technologyNames);
}