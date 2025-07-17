package com.carlos.charles_api.service;

import com.carlos.charles_api.model.entity.User;
import com.carlos.charles_api.model.entity.Workspace;
import com.carlos.charles_api.model.enums.Role;
import com.carlos.charles_api.repository.UserRepository;
import com.carlos.charles_api.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkspaceService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;
}
