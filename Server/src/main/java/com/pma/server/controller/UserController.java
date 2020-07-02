package com.pma.server.controller;

import com.pma.server.Dto.*;
import com.pma.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        return new ResponseEntity(this.userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
        return new ResponseEntity(this.userService.findUserDtoByEmail(email), HttpStatus.OK);
    }

    @PostMapping(value = "/{email}/picture")
    public ResponseEntity<UserDto> setUserProfilePicture(@PathVariable("email") String email, @RequestBody PictureDto picture) {
        return new ResponseEntity(this.userService.setUserProfilePicture(email, picture), HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UserAndTaskDto> loginUser(@RequestBody LoginDto loginDto) {

        UserAndTaskDto userAndTaskDto = this.userService.loginUser(loginDto);
        return new ResponseEntity<>(userAndTaskDto, HttpStatus.OK);
    }

    @PostMapping(value = "/forgot-password/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable("email") String email) {
        try {
            this.userService.forgotPassword(email);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto userDto) {
        try {
            this.userService.newUserPassword(userDto);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
