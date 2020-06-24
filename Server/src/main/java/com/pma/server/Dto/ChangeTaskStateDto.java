package com.pma.server.Dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChangeTaskStateDto {

    private String email;

    private List<String> taskIds;

    private String state;

    public ChangeTaskStateDto() {
        taskIds = new ArrayList<>();
    }


}
