package com.pma.server.Dto;

import lombok.Data;

import javax.persistence.Column;
import java.util.List;

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
