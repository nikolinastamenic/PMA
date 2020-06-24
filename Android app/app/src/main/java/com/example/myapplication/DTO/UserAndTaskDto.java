package com.example.myapplication.DTO;

import java.util.List;

import lombok.Data;

@Data
public class UserAndTaskDto {

    private UserDto user;
    private List<AllTaskDto> tasks;
}
