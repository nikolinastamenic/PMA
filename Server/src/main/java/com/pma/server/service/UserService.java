package com.pma.server.service;

import com.pma.server.Dto.LoginDto;
import com.pma.server.Dto.PictureDto;
import com.pma.server.Dto.UserAndTaskDto;
import com.pma.server.Dto.UserDto;
import com.pma.server.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserDto getUserById(Long id);
    User findUserByEmail (String email);
    UserDto findUserDtoByEmail (String email);
    UserDto setUserProfilePicture(String email, PictureDto picture);
    UserAndTaskDto loginUser (LoginDto loginDto);
    void changePassword(String email);
}
