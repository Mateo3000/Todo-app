package com.Mateusz.musttodoapp.logic;

import com.Mateusz.musttodoapp.TaskConfigurationProperties;
import com.Mateusz.musttodoapp.model.*;
import com.Mateusz.musttodoapp.model.projection.GroupReadModel;
import com.Mateusz.musttodoapp.model.projection.GroupTaskWriteModel;
import com.Mateusz.musttodoapp.model.projection.GroupWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskGroupService service;
    private TaskConfigurationProperties config;


    public ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository,
                          final TaskGroupService service, final TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.service = service;
        this.config = config;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(final Project toSave) {
        return repository.save(toSave);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        return repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> {
                                                var task = new GroupTaskWriteModel();
                                                task.setDescription(projectStep.getDescription());
                                                task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                                return task;
                                            }
                                    ).collect(Collectors.toSet())
                    );
                    return service.createGroup(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}