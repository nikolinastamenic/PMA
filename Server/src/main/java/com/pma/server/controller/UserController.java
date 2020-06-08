package com.pma.server.controller;

import com.pma.server.Dto.PictureDto;
import com.pma.server.Dto.UserDto;
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

    @PostMapping(value = "/{id}/picture")
    public ResponseEntity<UserDto> setUserProfilePicture(@PathVariable("id") Long id, @RequestBody PictureDto picture) {
        return new ResponseEntity(this.userService.setUserProfilePicture(id, picture), HttpStatus.OK);
    }

}
