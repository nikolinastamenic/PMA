package com.pma.server.service.serviceImpl;

import com.pma.server.Dto.PictureDto;
import com.pma.server.Dto.UserDto;
import com.pma.server.mappers.UserMapper;
import com.pma.server.model.User;
import com.pma.server.repository.UserRepository;
import com.pma.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto getUserById(Long id) {

        Optional<User> user = this.userRepository.findById(id);
        if (!user.isPresent()) {
            log.error("User with id: " + id + " doesn't exist!");
            throw new NullPointerException("User doesn't exist");
        }

        return UserMapper.toUserDto(user.get());
    }

    @Override
    public User findUserByEmail(String email)
    {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public UserDto setUserProfilePicture(Long id, PictureDto picture) {
        Optional<User> user = this.userRepository.findById(id);
        if (!user.isPresent()) {
            log.error("User with id: " + id + " doesn't exist!");
            throw new NullPointerException("User doesn't exist");
        }

       try (FileOutputStream stream = new FileOutputStream(new File("").getAbsolutePath() + "/pictures/"+picture.getPictureName())) {
            stream.write(picture.getPicture());

            user.get().setPicture(picture.getPictureName());
            this.userRepository.save(user.get());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return UserMapper.toUserDto(user.get());
    }
}
