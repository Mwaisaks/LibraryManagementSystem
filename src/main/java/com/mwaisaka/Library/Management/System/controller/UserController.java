package com.mwaisaka.Library.Management.System.controller;

import com.mwaisaka.Library.Management.System.Dto.request.BookRequest;
import com.mwaisaka.Library.Management.System.Dto.request.UserRequest;
import com.mwaisaka.Library.Management.System.Service.UserService;
import com.mwaisaka.Library.Management.System.models.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('TEACHER')")
    @GetMapping
    public ResponseEntity<List<UserRequest>> getUsers(){

        List<UserRequest> users = userService.getAllUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('STUDENT') and #id == authentication.principal.id")
   @PutMapping("/{id}")
    public ResponseEntity<UserRequest> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest){

        UserRequest updatedUser = userService.updateUser(id, userRequest);

        if (updatedUser != null){
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }

   @PreAuthorize("hasRole('LIBRARIAN')")
   @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){

        boolean deleted = userService.deleteUser(id);

        if (deleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        //check if it's null, we delete
   }

}
