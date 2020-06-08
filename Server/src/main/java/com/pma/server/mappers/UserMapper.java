package com.pma.server.mappers;

import com.pma.server.Dto.UserDto;
import com.pma.server.model.User;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setPictureName(user.getPicture());

        if (user.getPicture() != null) {
            String filePath = new File("").getAbsolutePath();
            filePath = filePath.concat("/pictures/");

            File in = new File(filePath + user.getPicture());
            try {
                dto.setPicture(IOUtils.toByteArray(new FileInputStream(in)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dto;
    }
}
