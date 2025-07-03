package com.carlos.charles_api.service;

import com.carlos.charles_api.dto.request.NewParticipantDTO;
import com.carlos.charles_api.dto.response.NewParticipantResponseDTO;
import com.carlos.charles_api.exceptions.BusinessRuleException;
import com.carlos.charles_api.model.entity.User;
import com.carlos.charles_api.model.entity.Workspace;
import com.carlos.charles_api.model.enums.Role;
import com.carlos.charles_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger("ACCESS_LOGGER");

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User getCurrentAuthenticatedUser() {
        return userRepository.getReferenceById(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

    @Transactional
    public NewParticipantResponseDTO addParticipant(NewParticipantDTO newParticipantDTO) {
        User thisUser = getCurrentAuthenticatedUser();

        // valida se pode proceder a adição do usuário
        validateAddParticipant(thisUser, newParticipantDTO);
        // Criptografa a senha
        String encryptedPassword = passwordEncoder.encode(newParticipantDTO.password());
        // cria o usuário do novo participante dentro do workspace
        Workspace thisWorkspace = thisUser.getWorkspace();
        User newParticipant = new User(newParticipantDTO.email(), encryptedPassword, newParticipantDTO.name(), newParticipantDTO.lastName(), newParticipantDTO.role(), thisWorkspace);
        // Salva novo owner
        userRepository.save(newParticipant);
        logger.atInfo().log("O " + thisUser.getIdentification() + " do workspace " + thisWorkspace.getIdentification() + " adicionou um novo participante: \n" + newParticipant.getIdentification());

        return new NewParticipantResponseDTO(newParticipantDTO.email(), newParticipantDTO.password());
    }

    private void validateUserRegister(String email) {
        if (this.userRepository.findByEmail(email) != null)
            throw new BusinessRuleException("O usuário com e-mail " + email + " já existe!");
    }

    private void validateAddParticipant(User thisUser, NewParticipantDTO newParticipantDTO) {
        // Verifica duplicidade de email
        validateUserRegister(newParticipantDTO.email());

        if (thisUser.getRole().equals(Role.OWNER)) {
            if (!List.of(Role.SUPPORTER, Role.COLLABORATOR, Role.ADMIN).contains(newParticipantDTO.role())) {
                throw new BusinessRuleException("Owners só podem adicionar administradores, suportes e colaboradores! Você tentou adicionar um " + newParticipantDTO.role().name());
            } else {
                return;
            }
        } else if (thisUser.getRole().equals(Role.ADMIN)) {
            if (!List.of(Role.SUPPORTER, Role.COLLABORATOR).contains(newParticipantDTO.role())) {
                throw new BusinessRuleException("Admins só podem adicionar suportes e colaboradores! Você tentou adicionar um " + newParticipantDTO.role().name());
            } else {
                return;
            }
        } else {
            throw new BusinessRuleException("Seu usuário " + thisUser.getRole().name() + " não tem permissão para adicionar um novo participante no workspace!");
        }
    }

}
