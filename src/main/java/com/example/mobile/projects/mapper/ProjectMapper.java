package com.example.mobile.projects.mapper;

import java.util.stream.Collectors;
import java.util.List;

import com.example.mobile.projects.entity.ProjectEntities;
import com.example.mobile.projects.model.ProjectModel;
import com.example.mobile.user.entity.UserEntity;
import com.example.mobile.technology.TechnologyEntity;
import com.example.mobile.image.ImageEntity;

public class ProjectMapper {
    public static ProjectModel toModel(ProjectEntities entity) {
        return new ProjectModel(
                entity.getProjectId(),
                entity.getProjectName(),
                entity.getProjectDescription(),
                entity.getLinkedInUrl(),
                mapUsersToNames(entity.getTeamMembers()),
                mapImagesToUrls(entity.getImages()),
                mapTechnologiesToNames(entity.getTechnologiesUsed()),
                entity.getCreatedDate()
        );
    }

    private static List<String> mapUsersToNames(List<?> users) {
        if (users == null) return null;

        return users.stream()
                .map(user -> {
                    UserEntity u = (UserEntity) user;
                    String name = u.getFullName();
                    return name != null ? name : u.getUsername();
                })
                .collect(Collectors.toList());
    }

    private static List<String> mapTechnologiesToNames(List<TechnologyEntity> technologies) {
        if (technologies == null) return null;

        return technologies.stream()
                .map(TechnologyEntity::getTechnologyName)
                .collect(Collectors.toList());
    }

    private static List<String> mapImagesToUrls(List<ImageEntity> images) {
        if (images == null) return null;

        return images.stream()
                .map(image -> "/images/download/" + image.getId()) // Assuming download endpoint
                .collect(Collectors.toList());
    }
}
