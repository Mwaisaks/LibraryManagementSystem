package com.mwaisaka.Library.Management.System.Service;

import com.mwaisaka.Library.Management.System.Dto.request.BookRequest;
import com.mwaisaka.Library.Management.System.Dto.request.UserRequest;
import com.mwaisaka.Library.Management.System.Repository.UserRepository;
import com.mwaisaka.Library.Management.System.models.Book;
import com.mwaisaka.Library.Management.System.models.User;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserRequest> getAllUsers() {

        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserRequest userRequest= new UserRequest();
                    BeanUtils.copyProperties(user, userRequest);
                    return userRequest;
                })
                .collect(Collectors.toList());
    }

    public UserRequest updateUser(Long id, @Valid UserRequest userRequest) {

        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()){

            User user = userOptional.get();

            if (userRequest.getName() != null) user.setName(userRequest.getName());
            if (userRequest.getEmail() != null) user.setEmail(userRequest.getEmail());
            if (userRequest.getPassword() != null) user.setPassword(userRequest.getPassword());
            if (userRequest.getRole() != null) user.setRole(userRequest.getRole());

            User updatedUser = userRepository.save(user);

            UserRequest updatedUserRequest = new UserRequest();
            BeanUtils.copyProperties(updatedUser, updatedUserRequest);
            return updatedUserRequest;
        }

        return null;
    }


    public boolean deleteUser(Long id) {

        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
