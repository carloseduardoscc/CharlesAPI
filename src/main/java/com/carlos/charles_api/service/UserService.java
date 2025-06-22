package com.carlos.charles_api.service;

import com.carlos.charles_api.model.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User getCurrentAuthenticatedUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
