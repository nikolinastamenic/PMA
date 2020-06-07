package com.pma.server.service;

import com.pma.server.Dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public UserDto getUserById(Long id);
}
