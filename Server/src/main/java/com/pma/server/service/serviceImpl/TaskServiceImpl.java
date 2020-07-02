package com.pma.server.service.serviceImpl;

import com.pma.server.Dto.AllTaskDto;
import com.pma.server.Dto.ChangeTaskStateDto;
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
    public List<AllTaskDto> getTasksWithoutUser() {

        List<AllTaskDto> dtos = new ArrayList<>();

        List<Task> urgentTrue = new ArrayList<>();
        List<Task> urgentFalse = new ArrayList<>();
        List<Task> allTasks = new ArrayList<>();


        for (Task task : taskRepository.findAllByOrderByDeadlineAsc()) {
            if (task.getUser() == null) {


                if (task.isUrgent()) {
                    urgentTrue.add(task);
                } else {
                    urgentFalse.add(task);
                }


            }
        }

        allTasks.addAll(urgentTrue);
        allTasks.addAll(urgentFalse);

        for(Task task:allTasks){
            AllTaskDto dto = TaskMapper.toTaskDto(task);
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public List<AllTaskDto> getTasksInProcess(String email) {

        List<AllTaskDto> dtos = new ArrayList<>();
        User user = this.userService.findUserByEmail(email);
        List<Task> tasks = this.taskRepository.findAllByUserAndState(user, "IN_PROCESS");

        for (Task task : tasks) {
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

        for (Task t : tasks) {
            AllTaskDto dto = TaskMapper.toTaskDto(t);
            taskDtos.add(dto);
        }

        return taskDtos;
    }

    @Override
    public ChangeTaskStateDto changeTaskState(ChangeTaskStateDto changeTaskStateDto) {

        User user = userService.findUserByEmail(changeTaskStateDto.getEmail());
        if (user == null) {
            return null;
        }
        ChangeTaskStateDto returned = new ChangeTaskStateDto();
        List<String> ids = new ArrayList<>();

        for (String taskId : changeTaskStateDto.getMysqlTaskIds()) {
            Long id = Long.parseLong(taskId);
            Task task = this.taskRepository.findTaskById(id);

            if (task != null) {

                if (changeTaskStateDto.getState().equals("IN_PROCESS")) {
                    if (task.getState().equals("NEW")) {

                        task.setState(changeTaskStateDto.getState());
                        task.setUser(user);
                        this.taskRepository.save(task);
                        ids.add(taskId);
                    }
                } else if (changeTaskStateDto.getState().equals("FINISHED")) {
                    task.setState(changeTaskStateDto.getState());
                    this.taskRepository.save(task);
                    ids.add(taskId);
                }

            }

        }
        returned.setMysqlTaskIds(ids);
        returned.setEmail(user.getEmail());
        returned.setState(changeTaskStateDto.getState());

        return returned;
    }

    @Override
    public Task getTaskById(Long id) {
        return this.taskRepository.findTaskById(id);
    }

    @Override
    public Task save(Task task) {
        return this.taskRepository.save(task);
    }
}
