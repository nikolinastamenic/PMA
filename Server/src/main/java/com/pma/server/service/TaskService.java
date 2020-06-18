package com.pma.server.service;

import com.pma.server.Dto.AllTaskDto;
import com.pma.server.Dto.ChangeTaskStateDto;
import com.pma.server.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {

    List<AllTaskDto> getAllTasks();
    List<AllTaskDto> getTasksInProcess(String username);
    List<AllTaskDto> getFinishedTasks(String username);
    boolean changeTaskState (ChangeTaskStateDto changeTaskStateDto);

}
