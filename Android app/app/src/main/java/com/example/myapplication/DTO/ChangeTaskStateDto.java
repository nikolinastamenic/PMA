package com.example.myapplication.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ChangeTaskStateDto {

    private String email;

    private List<String> taskIds;

    private String state;

    public ChangeTaskStateDto() {
        taskIds = new ArrayList<>();
    }


}
