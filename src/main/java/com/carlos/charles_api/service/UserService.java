package com.carlos.charles_api.service;

import com.carlos.charles_api.dto.request.NewParticipantDTO;
import com.carlos.charles_api.dto.response.NewParticipantResponseDTO;
import com.carlos.charles_api.dto.response.ParticipantDTO;
import com.carlos.charles_api.exceptions.BusinessRuleException;
import com.carlos.charles_api.exceptions.ResourceNotFoundException;
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
import java.util.stream.Collectors;


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

        validateAddParticipant(thisUser, newParticipantDTO);

        String encryptedPassword = passwordEncoder.encode(newParticipantDTO.password());
        Workspace thisWorkspace = thisUser.getWorkspace();
        User newParticipant = new User(newParticipantDTO.email(), encryptedPassword, newParticipantDTO.name(), newParticipantDTO.lastName(), newParticipantDTO.role(), thisWorkspace);

        userRepository.save(newParticipant);
        logger.atInfo().log("O " + thisUser.getIdentification() + " do workspace " + thisWorkspace.getIdentification() + " adicionou um novo participante: \n" + newParticipant.getIdentification());

        return new NewParticipantResponseDTO(newParticipantDTO.email(), newParticipantDTO.password());
    }

    @Transactional
    public List<ParticipantDTO> listParticipants() {
        User thisUser = getCurrentAuthenticatedUser();

        logger.atInfo().log("O " + thisUser.getIdentification() + " do workspace " + thisUser.getWorkspace().getIdentification() + " listou os participantes de seu workspace");

        return thisUser.getWorkspace().getUsers().stream()
                .map(ParticipantDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deactivate(Long userId) {
        User thisUser = getCurrentAuthenticatedUser();
        User userToDeactivate = findUserandValidateIfNull(userId);

        validateUserAcessByWorkspace(thisUser, userToDeactivate);
        validateAutoActivation(thisUser, userToDeactivate);
        validateParticipantStatusChange(thisUser, userToDeactivate);

        userToDeactivate.setEnabled(false);
        userRepository.save(userToDeactivate);

        logger.atInfo().log("O " + thisUser.getIdentification() + " do workspace " + thisUser.getWorkspace().getIdentification() + " desativou " + userToDeactivate.getIdentification() + " de seu workspace");
    }

    @Transactional
    public void activate(Long userId) {
        User thisUser = getCurrentAuthenticatedUser();
        User userToActivate = findUserandValidateIfNull(userId);

        validateUserAcessByWorkspace(thisUser, userToActivate);
        validateAutoActivation(thisUser, userToActivate);
        validateParticipantStatusChange(thisUser, userToActivate);

        userToActivate.setEnabled(true);
        userRepository.save(userToActivate);

        logger.atInfo().log("O " + thisUser.getIdentification() + " do workspace " + thisUser.getWorkspace().getIdentification() + " reativou " + userToActivate.getIdentification() + " de seu workspace");
    }

    // VALIDAÇÕES

    private User findUserandValidateIfNull(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuário com id: " + userId + " não foi encontrado!"));
        return user;
    }

    private void validateAutoActivation(User thisUser, User userToDeactivate) {
        if (thisUser.equals(userToDeactivate)) {
            throw new BusinessRuleException("Você não pode se desativar!");
        }
    }

    // valida se a os está no mesmo workspace que o usuário
    private static void validateUserAcessByWorkspace(User user, User acessedUser) {
        Workspace workspace = user.getWorkspace();
        if (!user.getWorkspace().getUsers().contains(acessedUser)) {
            throw new BusinessRuleException("Você não tem acesso a usuários de fora do seu workspace!");
        }
    }

    private static void validateParticipantStatusChange(User thisUser, User userToDeactivate) {
        if (thisUser.getRole().equals(Role.OWNER)) {
            if (!List.of(Role.SUPPORTER, Role.COLLABORATOR, Role.ADMIN).contains(userToDeactivate.getRole())) {
                throw new BusinessRuleException("Owners só podem mudar status de administradores, suportes e colaboradores! Você tentou alterar um " + userToDeactivate.getRole().name());
            }
        } else if (thisUser.getRole().equals(Role.ADMIN)) {
            if (!List.of(Role.SUPPORTER, Role.COLLABORATOR).contains(userToDeactivate.getRole())) {
                throw new BusinessRuleException("Admins só podem alterar status de suportes e colaboradores! Você tentou alterar um " + userToDeactivate.getRole().name());
            }
        } else {
            throw new BusinessRuleException("Seu usuário " + thisUser.getRole().name() + " não tem permissão para alterar status de um participante do workspace!");
        }
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
            }
        } else if (thisUser.getRole().equals(Role.ADMIN)) {
            if (!List.of(Role.SUPPORTER, Role.COLLABORATOR).contains(newParticipantDTO.role())) {
                throw new BusinessRuleException("Admins só podem adicionar suportes e colaboradores! Você tentou adicionar um " + newParticipantDTO.role().name());
            }
        } else {
            throw new BusinessRuleException("Seu usuário " + thisUser.getRole().name() + " não tem permissão para adicionar um novo participante no workspace!");
        }
    }
}
