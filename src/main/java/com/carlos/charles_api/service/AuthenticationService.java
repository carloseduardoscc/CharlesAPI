package com.carlos.charles_api.service;

import com.carlos.charles_api.dto.request.AuthenticationRequestDTO;
import com.carlos.charles_api.dto.response.LoginResponseDTO;
import com.carlos.charles_api.dto.response.UserInfoDTO;
import com.carlos.charles_api.exceptions.BusinessRuleException;
import com.carlos.charles_api.model.entity.User;
import com.carlos.charles_api.dto.request.RegisterRequestDTO;
import com.carlos.charles_api.model.entity.Workspace;
import com.carlos.charles_api.model.enums.Role;
import com.carlos.charles_api.repository.UserRepository;
import com.carlos.charles_api.repository.WorkspaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    private static final Logger logger = LoggerFactory.getLogger("ACCESS_LOGGER");
    @Autowired
    private UserService userService;

    public LoginResponseDTO login(AuthenticationRequestDTO data) {
        // Cria objeto com as credenciais recebidas
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        // Realiza a autenticação (pode lançar exceção se as credenciais estiverem erradas)
        Authentication auth = authenticationManager.authenticate(usernamePassword);
        // Pega o usuário logado e verifica se ele está ativo
        User user = (User) auth.getPrincipal();
        // Gera token para o usuário autenticado
        var token = tokenService.generateToken(user);

        logger.atInfo().log("Usuário {} autenticado com sucesso!", user.getIdentification());

        // Retorna DTO contendo o token JWT
        return new LoginResponseDTO(token);
    }

    public void register(RegisterRequestDTO registerDTO) {
        // Verifica duplicidade de e-mail
        validateUserRegister(registerDTO);
        // Criptografa a senha
        String encryptedPassword = passwordEncoder.encode(registerDTO.password());
        // Cria workspace
        Workspace newWorkspace = new Workspace(registerDTO.workspaceName());
        // Cria owner
        User newOwner = new User(registerDTO.email(), encryptedPassword, registerDTO.name(), registerDTO.lastName(), Role.OWNER, newWorkspace);
        // põe owner no workspace
        newWorkspace.getUsers().add(newOwner);
        // Salva novo owner e workspace
        workspaceRepository.save(newWorkspace);
        userRepository.save(newOwner);

        logger.atInfo().log("Usuário {} criado com sucesso!", newOwner.getIdentification());
    }

    public UserInfoDTO me(){
        User user = userService.getCurrentAuthenticatedUser();
        user = userRepository.findById(user.getId()).get();
        return UserInfoDTO.fromEntity(user);
    }

    private void validateUserRegister(RegisterRequestDTO data) {
        if (this.userRepository.findByEmail(data.email()) != null)
            throw new BusinessRuleException("O usuário com e-mail " + data.email() + " já existe!");
    }
}