package com.pma.server.Dto;

import lombok.Data;

@Data
public class ChangePasswordDto {

    private String email;
    private String password;
}
