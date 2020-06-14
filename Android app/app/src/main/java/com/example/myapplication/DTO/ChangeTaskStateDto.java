package com.example.myapplication.DTO;

import lombok.Data;

@Data
public class ChangeTaskStateDto {

    private String email;

    private String taskId;

    private String state;

    public ChangeTaskStateDto() {
    }


}
