package com.Mateusz.musttodoapp.logic;

import com.Mateusz.musttodoapp.model.TaskGroup;
import com.Mateusz.musttodoapp.model.TaskGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProjectServiceTest {

    @Test
    @DisplayName("Should throw IllegalStateException when configured to allow just 1 group and the other" +
            "undone group")
    void createGroup_noMultipleGroupsConfig_And_openGroups_throwsIllegalStateException() {
        //given
        var mockGroupRepository = new TaskGroupRepository() {
            @Override
            public List<TaskGroup> findAll() {
                return null;
            }

            @Override
            public Optional<TaskGroup> findById(Integer id) {
                return Optional.empty();
            }

            @Override
            public TaskGroup save(TaskGroup entity) {
                return null;
            }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
                return false;
            }
        }

    }
}
