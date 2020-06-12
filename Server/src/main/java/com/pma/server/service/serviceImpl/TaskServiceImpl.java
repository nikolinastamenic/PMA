package com.pma.server.service.serviceImpl;

import com.pma.server.Dto.AllTaskDto;
import com.pma.server.Dto.ReportItemDto;
import com.pma.server.mappers.ReportItemMapper;
import com.pma.server.mappers.TaskMapper;
import com.pma.server.model.Report;
import com.pma.server.model.ReportItem;
import com.pma.server.model.Task;
import com.pma.server.model.User;
import com.pma.server.repository.TaskRepository;
import com.pma.server.service.ReportItemService;
import com.pma.server.service.ReportService;
import com.pma.server.service.TaskService;
import com.pma.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportItemService reportItemService;


    @Override
    public List<AllTaskDto> getAllTasks() {

        List<AllTaskDto> dtos = new ArrayList<>();

        for(Task task: taskRepository.findAll()) {
            if(task.getUser() == null){
                AllTaskDto dto = TaskMapper.toTaskDto(task);
                dtos.add(dto);
            }
        }
        return dtos;
    }

    @Override
    public List<AllTaskDto> getTasksInProcess(String email) {

        List<AllTaskDto> dtos = new ArrayList<>();
        User user = this.userService.findUserByEmail(email);
        List<Task> tasks = this.taskRepository.findAllByUserAndState(user, "IN_PROCESS");

        for(Task task: tasks){
            AllTaskDto dto = TaskMapper.toTaskDto(task);
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public List<AllTaskDto> getFinishedTasks(String email) {
        User user = this.userService.findUserByEmail(email);

        List<Task> tasks = this.taskRepository.findAllByUserAndState(user, "FINISHED");
        List<AllTaskDto> taskDtos = new ArrayList<>();

        for(Task t: tasks){
            AllTaskDto dto = TaskMapper.toTaskDto(t);
            taskDtos.add(dto);
        }

        return taskDtos;
    }
}
