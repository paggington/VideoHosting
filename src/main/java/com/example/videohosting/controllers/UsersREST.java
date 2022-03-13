package com.example.videohosting.controllers;

import com.example.videohosting.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UsersREST {
    @PostMapping
    public ResponseEntity<?> saveNewUser(@RequestBody UserRequest request) {
        if(request.isOK()){
            User user=new User();
            user.setEmail(request.email);
            user.setName(request.name);
            user.setSurname(request.surname);
            user.setPassword(request.password);
            user.setUsername(request.username);
            //TODO:make user image
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private static class UserRequest {
        private String name;
        private String surname;
        private String username;
        private String password;
        private MultipartFile profileImage;
        private String email;

        public boolean isOK() {
            if (!username.isEmpty() && !password.isEmpty() && !profileImage.isEmpty() && !email.isEmpty()){
                return true;
            }
            return false;
        }
    }
}
