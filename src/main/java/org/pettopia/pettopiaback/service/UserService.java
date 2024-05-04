package org.pettopia.pettopiaback.service;

import lombok.RequiredArgsConstructor;
import org.pettopia.pettopiaback.domain.RoleType;
import org.pettopia.pettopiaback.domain.Users;
import org.pettopia.pettopiaback.dto.UserDTO;
import org.pettopia.pettopiaback.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Users saveMember(UserDTO userDTO) {
        Users member = Users.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .roleType(RoleType.USER).build();
        return userRepository.save(member);
    }
}