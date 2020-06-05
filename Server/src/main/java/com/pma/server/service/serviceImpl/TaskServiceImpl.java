package com.pma.server.service.serviceImpl;

import com.pma.server.model.Task;
import com.pma.server.repository.TaskRepository;
import com.pma.server.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasksWithoutUser = new ArrayList<>();
        for(Task task: taskRepository.findAll()) {
            if(task.getUser() == null){
                tasksWithoutUser.add(task);
            }
        }
        return tasksWithoutUser;
    }
}
