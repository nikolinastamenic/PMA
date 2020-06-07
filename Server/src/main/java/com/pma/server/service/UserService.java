package com.pma.server.service;

import com.pma.server.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User findUserByEmail (String email);
}
