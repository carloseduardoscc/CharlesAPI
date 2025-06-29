package com.carlos.charles_api.service;

import com.carlos.charles_api.model.entity.User;
import com.carlos.charles_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User getCurrentAuthenticatedUser(){
        return userRepository.getReferenceById (((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

}
