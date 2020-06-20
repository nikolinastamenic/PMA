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

        List<AllTaskDto> tasksWithoutUser = this.taskService.getAllTasks();
        List<AllTaskDto> finishedTasks = this.taskService.getFinishedTasks(emailDto.getEmail());
        List<AllTaskDto> tasksInProcess = this.taskService.getTasksInProcess(emailDto.getEmail());

        //todo vratiti samo tasks without user kad se podesi brisanje na androidu
//        tasksWithoutUser.addAll(finishedTasks);
//        tasksWithoutUser.addAll(tasksInProcess);

        return new ResponseEntity<>(tasksWithoutUser,HttpStatus.OK);
    }


    @PostMapping(value = "/change/state")
    public ResponseEntity changeTaskState(@RequestBody ChangeTaskStateDto changeTaskStateDto) {
        boolean success = this.taskService.changeTaskState(changeTaskStateDto);
        if(success) {

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


}
