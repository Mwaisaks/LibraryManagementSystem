package com.mwaisaka.Library.Management.System.Service;

import com.mwaisaka.Library.Management.System.Dto.mapper.UserMapper;
import com.mwaisaka.Library.Management.System.Dto.request.UpdateUserRequest;
import com.mwaisaka.Library.Management.System.Dto.response.MessageResponse;
import com.mwaisaka.Library.Management.System.Dto.response.UserResponse;
import com.mwaisaka.Library.Management.System.enums.UserRole;
import com.mwaisaka.Library.Management.System.models.User;
import com.mwaisaka.Library.Management.System.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toResponseList(users);
    }

    public MessageResponse updateUser(Long id, UpdateUserRequest request, String currentUserEmail, UserRole currentUserRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Check if user can update (themselves or admin)
        if (!user.getEmail().equals(currentUserEmail) && currentUserRole != UserRole.LIBRARIAN) {
            throw new RuntimeException("Access denied: You can only update your own profile");
        }

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            if (userRepository.existsByEmail(request.getEmail()) && !user.getEmail().equals(request.getEmail())) {
                throw new RuntimeException("Email is already taken!");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
        return new MessageResponse("User updated successfully!");
    }

    public MessageResponse deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        userRepository.delete(user);
        return new MessageResponse("User deleted successfully!");
    }
}