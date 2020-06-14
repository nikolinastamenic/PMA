package com.pma.server.Dto;

import lombok.Data;

@Data
public class ChangeTaskStateDto {

    private String email;

    private String taskId;

    private String state;

    public ChangeTaskStateDto() {
    }


}
