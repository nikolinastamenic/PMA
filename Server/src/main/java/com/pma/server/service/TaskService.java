package com.pma.server.service;

import com.pma.server.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {

    List<Task> getAllTasks();
    List<Task> getTasksInProcess(String username);
    List<Task> getFinishedTasks(String username);

}
