package com.pma.server.service;

import com.pma.server.Dto.*;
import com.pma.server.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserDto getUserById(Long id);
    User findUserByEmail (String email);
    UserDto findUserDtoByEmail (String email);
    UserDto setUserProfilePicture(String email, PictureDto picture);
    UserAndTaskDto loginUser (LoginDto loginDto);
    void forgotPassword(String email);
    void newUserPassword(ChangePasswordDto userDto);
}
