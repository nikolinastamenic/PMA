package com.pma.server.controller;

import com.pma.server.Dto.*;

import com.pma.server.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/task", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping(value = "/all")
    public ResponseEntity<List<AllTaskDto>> getAllWithoutUser(@RequestBody EmailDto emailDto) {

        List<AllTaskDto> tasksWithoutUser = this.taskService.getTasksWithoutUser();


        return new ResponseEntity<>(tasksWithoutUser, HttpStatus.OK);
    }


    @PostMapping(value = "/change/state")
    public ResponseEntity<ChangeTaskStateDto> changeTaskState(@RequestBody ChangeTaskStateDto changeTaskStateDto) {
        ChangeTaskStateDto changeTaskState = this.taskService.changeTaskState(changeTaskStateDto);

        if (changeTaskState != null) {
            return new ResponseEntity(changeTaskState, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }


}
