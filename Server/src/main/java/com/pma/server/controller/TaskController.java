package com.pma.server.controller;

import com.pma.server.Dto.*;
import com.pma.server.model.Address;
import com.pma.server.model.Report;
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


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<AllTaskDto>> getAllTasks() {

        List<AllTaskDto> dtos = new ArrayList<>();
        List<Task> tasks = this.taskService.getAllTasks();

        for(Task task:tasks){
            BuildingDto buildingDto = new BuildingDto(task.getApartment().getBuilding().getId(), task.getApartment().getBuilding().getAddress());
            ApartmentDto apartmentDto = new ApartmentDto(task.getApartment().getId(), task.getApartment().getNumber(),buildingDto);
            AllTaskDto dto = new AllTaskDto(task.getId(), apartmentDto, task.getTypeOfApartment(),task.getState(),task.isUrgent(),task.getDeadline());
            dtos.add(dto);
        }
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }

    @PostMapping(value = "/inprocess")
    public ResponseEntity<List<AllTaskDto>> getTaksInProcess(@RequestBody EmailDto emailDto) {

        List<AllTaskDto> dtos = new ArrayList<>();
        List<Task> tasks = this.taskService.getTasksInProcess(emailDto.getEmail());

        for(Task task:tasks){
            UserDto userDto = new UserDto(task.getUser());
            BuildingDto buildingDto = new BuildingDto(task.getApartment().getBuilding().getId(), task.getApartment().getBuilding().getAddress());
            ApartmentDto apartmentDto = new ApartmentDto(task.getApartment().getId(), task.getApartment().getNumber(),buildingDto);
            AllTaskDto dto = new AllTaskDto(task.getId(), apartmentDto, task.getTypeOfApartment(),task.getState(),task.isUrgent(),task.getDeadline(), userDto);
            dtos.add(dto);
        }
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }


    @PostMapping(value = "/finished")
    public ResponseEntity<List<AllTaskDto>> getFinishedTasks(@RequestBody EmailDto emailDto) {

        List<AllTaskDto> dtos = new ArrayList<>();
        List<Task> tasks = this.taskService.getFinishedTasks(emailDto.getEmail());

        for(Task task:tasks){
            UserDto userDto = new UserDto(task.getUser());
            ReportDto reportDto = new ReportDto(task.getReport());
            BuildingDto buildingDto = new BuildingDto(task.getApartment().getBuilding().getId(), task.getApartment().getBuilding().getAddress());
            ApartmentDto apartmentDto = new ApartmentDto(task.getApartment().getId(), task.getApartment().getNumber(),buildingDto);
            AllTaskDto dto = new AllTaskDto(task.getId(), apartmentDto, task.getTypeOfApartment(),task.getState(),task.isUrgent(),task.getDeadline(), userDto, reportDto);
            dtos.add(dto);
        }
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }

}
