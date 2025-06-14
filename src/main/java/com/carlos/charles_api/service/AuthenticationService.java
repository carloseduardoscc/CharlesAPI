package com.carlos.charles_api.service;

import com.carlos.charles_api.dto.request.AuthenticationDTO;
import com.carlos.charles_api.dto.LoginResponseDTO;
import com.carlos.charles_api.model.entity.User;
import com.carlos.charles_api.dto.RegisterDTO;
import com.carlos.charles_api.repository.UserRepository;
import com.carlos.charles_api.exceptions.UserAlreadyExistsException;
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
    private UserRepository repository;
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
    public LoginResponseDTO login(AuthenticationDTO data) {
        // Cria objeto com as credenciais recebidas
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        // Realiza a autenticação (pode lançar exceção se as credenciais estiverem erradas)
        var auth = authenticationManager.authenticate(usernamePassword);
        // Gera token para o usuário autenticado
        var token = tokenService.generateToken((User) auth.getPrincipal());
        // Retorna DTO contendo o token JWT
        return new LoginResponseDTO(token);
    }

    /**
     * Realiza o cadastro de um novo usuário.
     * Valida se já existe usuário com o mesmo e-mail, criptografa a senha e salva no banco.
     */
    public void register(RegisterDTO registerDTO) {
        // Verifica duplicidade de e-mail
        validateUserRegister(registerDTO);
        // Criptografa a senha
        String encryptedPassword = passwordEncoder.encode(registerDTO.password());
        // Cria objeto User com os dados do registro
        User newUser = new User(registerDTO.email(), encryptedPassword, registerDTO.name(), registerDTO.lastName());
        // Salva novo usuário no banco de dados
        this.repository.save(newUser);
    }

    /**
     * Verifica se já existe usuário com o e-mail informado.
     * Se existir, lança exceção personalizada de usuário duplicado.
     */
    private void validateUserRegister(RegisterDTO data) {
        if (this.repository.findByEmail(data.email()) != null)
            throw new UserAlreadyExistsException("O usuário " + data.email() + " já existe!");
    }
}