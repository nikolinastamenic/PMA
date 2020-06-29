package com.pma.server.service.serviceImpl;

import com.pma.server.Dto.*;
import com.pma.server.mappers.UserMapper;
import com.pma.server.model.Task;
import com.pma.server.model.User;
import com.pma.server.repository.UserRepository;
import com.pma.server.service.TaskService;
import com.pma.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, TaskService taskService) {
        this.userRepository = userRepository;
        this.taskService = taskService;
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
    public UserDto findUserDtoByEmail(String email) {
        Optional<User> user = this.userRepository.getUserByEmail(email);
        if (!user.isPresent()) {
            log.error("User with email: " + email + " doesn't exist!");
            throw new NullPointerException("User doesn't exist");
        }

        return UserMapper.toUserDto(user.get());
    }

    @Override
    public UserDto setUserProfilePicture(String email, PictureDto picture) {
        Optional<User> user = this.userRepository.getUserByEmail(email);
        if (!user.isPresent()) {
            log.error("User with email: " + email + " doesn't exist!");
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

    @Override
    public UserAndTaskDto loginUser(LoginDto loginDto) {

        User user = this.userRepository.findUserByEmail(loginDto.getEmail());
        UserAndTaskDto userAndTaskDto = new UserAndTaskDto();

        if (user == null){
            return null;
        } else if (user.getPassword().equals(loginDto.getPassword())){
            userAndTaskDto.setUser(UserMapper.toUserDto(user));
            List<AllTaskDto> tasksInProcess = this.taskService.getTasksInProcess(loginDto.getEmail());
            List<AllTaskDto> finishedTasks = this.taskService.getFinishedTasks(loginDto.getEmail());

            if(!tasksInProcess.isEmpty() && !finishedTasks.isEmpty()) {
                tasksInProcess.addAll(finishedTasks);
                userAndTaskDto.setTasks(tasksInProcess);
            } else if (tasksInProcess.isEmpty() && !finishedTasks.isEmpty()){
                userAndTaskDto.setTasks(finishedTasks);
            } else if (finishedTasks.isEmpty() && !tasksInProcess.isEmpty()){
                userAndTaskDto.setTasks(tasksInProcess);
            } else {
                userAndTaskDto.setTasks(new ArrayList<>());
            }

            return userAndTaskDto;
        } else {
            return null;
        }

    }
}
