package com.chatapp.backend.admin.user.service;

import com.chatapp.backend.admin.user.dto.CreateUserRequest;
import com.chatapp.backend.admin.user.dto.UpdateUserRequest;
import com.chatapp.backend.admin.user.dto.UserDto;
import com.chatapp.backend.common.constants.MessageConstants;
import com.chatapp.backend.common.exception.UserNotFoundException;
import com.chatapp.backend.user.entity.Role;
import com.chatapp.backend.user.entity.User;
import com.chatapp.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public Page<UserDto> getAllUsersPaginated(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return userRepository.findAll(pageable).map(this::mapToDto);
    }
    
    public UserDto getUserById(UUID id) {
        User user = findUserById(id);
        return mapToDto(user);
    }
    
    public UserDto createUser(CreateUserRequest request) {
        validateUserCreation(request);
        User user = buildUserFromRequest(request);
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }
    
    public UserDto updateUser(UUID id, UpdateUserRequest request) {
        User user = findUserById(id);
        updateUserFields(user, request);
        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }
    
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }
    
    public UserDto promoteToAdmin(UUID id) {
        User user = findUserById(id);
        user.setRole(Role.ADMIN);
        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }
    
    public UserDto demoteToUser(UUID id) {
        User user = findUserById(id);
        user.setRole(Role.USER);
        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }
    
    // Helper methods
    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
    
    private void validateUserCreation(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException(MessageConstants.ERROR_USERNAME_EXISTS);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(MessageConstants.ERROR_EMAIL_EXISTS);
        }
    }
    
    private User buildUserFromRequest(CreateUserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .avatar(request.getAvatar())
                .role(request.getRole())
                .build();
    }
    
    private void updateUserFields(User user, UpdateUserRequest request) {
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new RuntimeException(MessageConstants.ERROR_USERNAME_EXISTS);
            }
            user.setUsername(request.getUsername());
        }
        
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException(MessageConstants.ERROR_EMAIL_EXISTS);
            }
            user.setEmail(request.getEmail());
        }
        
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
    }
    
    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
