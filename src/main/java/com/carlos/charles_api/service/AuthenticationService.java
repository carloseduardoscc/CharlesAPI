package com.carlos.charles_api.service;

import com.carlos.charles_api.dto.request.AuthenticationRequestDTO;
import com.carlos.charles_api.dto.response.LoginDTO;
import com.carlos.charles_api.exceptions.BusinessRuleException;
import com.carlos.charles_api.model.entity.User;
import com.carlos.charles_api.dto.request.RegisterRequestDTO;
import com.carlos.charles_api.model.entity.Workspace;
import com.carlos.charles_api.model.enums.EntityState;
import com.carlos.charles_api.model.enums.UserRole;
import com.carlos.charles_api.repository.UserRepository;
import com.carlos.charles_api.exceptions.UserAlreadyExistsException;
import com.carlos.charles_api.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por autenticação e cadastro de usuários.
 */
@Service
public class AuthenticationService {

    // Repositório de acesso ao banco de usuários
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

    /**
     * Realiza o processo de login/autenticação do usuário.
     * Autentica as credenciais via Spring Security e gera um token JWT para o usuário autenticado.
     */
    public LoginDTO login(AuthenticationRequestDTO data) {
        // Cria objeto com as credenciais recebidas
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        // Realiza a autenticação (pode lançar exceção se as credenciais estiverem erradas)
        var auth = authenticationManager.authenticate(usernamePassword);
        // Pega o usuário logado e verifica se ele está ativo
        User user = (User) auth.getPrincipal();
        if (!user.getState().equals(EntityState.ACTIVE)){
            throw new BusinessRuleException("Você não tem mais acesso, sua conta foi desativada! Procure seu responsável para saber mais.");
        }
        // Gera token para o usuário autenticado
        var token = tokenService.generateToken(user);
        // Retorna DTO contendo o token JWT
        return new LoginDTO(token);
    }

    /**
     * Realiza o cadastro de um novo usuário.
     * Valida se já existe usuário com o mesmo e-mail, criptografa a senha e salva no banco.
     */
    public void register(RegisterRequestDTO registerDTO) {
        // Verifica duplicidade de e-mail
        validateUserRegister(registerDTO);
        // Criptografa a senha
        String encryptedPassword = passwordEncoder.encode(registerDTO.password());
        // Cria workspace
        Workspace newWorkspace = new Workspace(registerDTO.workspaceName());
        // Cria owner
        User newOwner = new User(registerDTO.email(), encryptedPassword, registerDTO.name(), registerDTO.lastName(), UserRole.OWNER, newWorkspace);
        // põe owner no workspace
        newWorkspace.getUsers().add(newOwner);
        // Salva novo owner e workspace
        userRepository.save(newOwner);
        workspaceRepository.save(newWorkspace);
    }

    /**
     * Verifica se já existe usuário com o e-mail informado.
     * Se existir, lança exceção personalizada de usuário duplicado.
     */
    private void validateUserRegister(RegisterRequestDTO data) {
        if (this.userRepository.findByEmail(data.email()) != null)
            throw new UserAlreadyExistsException("O usuário " + data.email() + " já existe!");
    }
}