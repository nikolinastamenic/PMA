package com.pma.server.Dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private byte[] picture;
    private String pictureName;
}
