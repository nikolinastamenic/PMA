package com.pma.server.Dto;

import lombok.Data;

import java.util.List;

@Data
public class UserAndTaskDto {

    private UserDto user;
    private List<AllTaskDto> tasks;
}