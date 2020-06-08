package com.pma.server.controller;

import com.pma.server.Dto.*;
import com.pma.server.mappers.UserMapper;
import com.pma.server.model.Task;
import com.pma.server.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/api/task", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping(value = "/all")
    public ResponseEntity<List<AllTaskDto>> getAllTasks(@RequestBody EmailDto emailDto) {

        List<AllTaskDto> dtos = new ArrayList<>();
        List<Task> tasksWithoutUser = this.taskService.getAllTasks();
        List<Task> finishedTasks = this.taskService.getFinishedTasks(emailDto.getEmail());

        List<Task> tasksInProcess = this.taskService.getTasksInProcess(emailDto.getEmail());

        tasksWithoutUser.addAll(finishedTasks);
        tasksWithoutUser.addAll(tasksInProcess);

        for(Task task:tasksWithoutUser){
            UserDto userDto = null;
            if(task.getUser() != null) {
                 userDto = UserMapper.toUserDto(task.getUser());
            }
            BuildingDto buildingDto = new BuildingDto(task.getApartment().getBuilding().getId(), task.getApartment().getBuilding().getAddress());
            ApartmentDto apartmentDto = new ApartmentDto(task.getApartment().getId(), task.getApartment().getNumber(),buildingDto);
            AllTaskDto dto = new AllTaskDto(task.getId(), apartmentDto, task.getTypeOfApartment(),task.getState(),task.isUrgent(),task.getDeadline(), userDto);
            dtos.add(dto);
        }
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }


}
