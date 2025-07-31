package com.mwaisaka.Library.Management.System.service.impl;

import com.mwaisaka.Library.Management.System.domain.dto.request.RegisterRequest;
import com.mwaisaka.Library.Management.System.domain.dto.request.UpdateUserRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.UserResponse;
import com.mwaisaka.Library.Management.System.domain.models.User;
import com.mwaisaka.Library.Management.System.exceptions.user.EmailAlreadyExistsException;
import com.mwaisaka.Library.Management.System.exceptions.user.InvalidEmailFormatException;
import com.mwaisaka.Library.Management.System.exceptions.user.UserNotFoundException;
import com.mwaisaka.Library.Management.System.exceptions.user.WeakPasswordException;
import com.mwaisaka.Library.Management.System.mapper.UserMapper;
import com.mwaisaka.Library.Management.System.repository.UserRepository;
import com.mwaisaka.Library.Management.System.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    // Password validation constants
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    );

    @Override
    public UserResponse registerUser(RegisterRequest request) {
        log.debug("Registering new user with email: {}", request.getEmail());

        validateRegistrationRequest(request);

        if (userRepository.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        User user = createUserFromRequest(request);
        User savedUser = userRepository.save(user);

        log.info("Successfully registered user with id: {} and email: {}", savedUser.getId(), savedUser.getEmail());
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        log.debug("Fetching user with id: {}", userId);

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + userId));

        log.debug("Successfully fetched user with id: {}", userId);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<User> users = userRepository.findAll(pageable);
        Page<UserResponse> userResponses = users.map(userMapper::toDto);

        log.debug("Retrieved {} users from database", users.getTotalElements());
        return userResponses;
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        log.debug("Updating user with id: {}", id);

        validateInput(id, request);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + id));

        updateUserFields(user, request);

        User updatedUser = userRepository.save(user);
        log.info("Successfully updated user with id: {}", id);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.debug("Deleting user with id: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with Id: " + id);
        }

        userRepository.deleteById(id);
        log.info("Successfully deleted user with id: {}", id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);

        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }

        return userRepository.findByEmail(email.toLowerCase().trim());
    }

    // Helper Methods

    private void validateRegistrationRequest(RegisterRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Registration request cannot be null");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        if (!isValidEmail(request.getEmail())) {
            throw new InvalidEmailFormatException("Invalid email format: " + request.getEmail());
        }

        validatePassword(request.getPassword());
    }

    private User createUserFromRequest(RegisterRequest request) {
        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        return user;
    }

    private void validateInput(Long id, UpdateUserRequest request) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (request == null) {
            throw new IllegalArgumentException("Update request cannot be null");
        }
    }

    private void updateUserFields(User user, UpdateUserRequest request) {
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            user.setName(request.getName().trim());
        }

        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            updateUserEmail(user, request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            updateUserPassword(user, request.getPassword());
        }
    }

    private void updateUserEmail(User user, String newEmail) {
        String trimmedEmail = newEmail.trim().toLowerCase();

        if (!isValidEmail(trimmedEmail)) {
            throw new InvalidEmailFormatException("Invalid email format: " + newEmail);
        }

        if (!user.getEmail().equals(trimmedEmail) &&
                userRepository.existsByEmail(trimmedEmail)) {
            throw new EmailAlreadyExistsException("Email already exists: " + newEmail);
        }

        user.setEmail(trimmedEmail);
    }

    private void updateUserPassword(User user, String newPassword) {
        validatePassword(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new WeakPasswordException(
                    String.format("Password must be at least %d characters long", MIN_PASSWORD_LENGTH)
            );
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new WeakPasswordException(
                    "Password must contain at least one digit, one lowercase letter, " +
                            "one uppercase letter, one special character, and no whitespace"
            );
        }
    }
}