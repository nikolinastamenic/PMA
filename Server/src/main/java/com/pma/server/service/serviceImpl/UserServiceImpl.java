package com.pma.server.service.serviceImpl;

import com.pma.server.model.User;
import com.pma.server.repository.UserRepository;
import com.pma.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserByEmail(String email)
    {
        return userRepository.findUserByEmail(email);
    }
}
