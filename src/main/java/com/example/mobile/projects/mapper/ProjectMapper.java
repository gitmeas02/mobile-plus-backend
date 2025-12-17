package com.example.mobile.projects.mapper;

import java.util.stream.Collectors;
import java.util.List;

import com.example.mobile.projects.entity.ProjectEntities;
import com.example.mobile.projects.model.ProjectModel;
import com.example.mobile.user.entity.UserEntity;

public class ProjectMapper {
    public static ProjectModel toModel(ProjectEntities entity) {
        return new ProjectModel(
                entity.getProjectId(),
                entity.getProjectName(),
                entity.getProjectDescription(),
                entity.getLinkedInUrl(),
                mapUsersToNames(entity.getTeamMembers()),
                entity.getImageUrls(),
                entity.getTechnologiesUsed(),
                entity.getCreatedDate()
        );
    }

    private static List<String> mapUsersToNames(List<?> users) {
        if (users == null) return null;

        return users.stream()
                .map(user -> ((UserEntity) user).getFullName())
                .collect(Collectors.toList());
    }
}
