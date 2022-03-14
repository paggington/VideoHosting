package com.example.videohosting.controllers;

import com.example.videohosting.files.FileUpload;
import com.example.videohosting.models.User;
import com.example.videohosting.user_profile_pic_generator.ProfileImageGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
@RequestMapping("/api/users")
public class UsersREST {
    private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    @PostMapping("/new")
    public ResponseEntity<?> saveNewUser(@RequestBody UserRequest request) {
        if(request.isOK()){
            User user=new User();
            user.setEmail(request.email);
            user.setName(request.name);
            user.setSurname(request.surname);
            user.setPassword(request.password);
            user.setUsername(request.username);
            user.setDateOfRegistration(dateTimeFormatter.format(new Date().toInstant()));
            if(request.profileImage.isEmpty()){
                user.setProfilePicture(new ProfileImageGenerator().generateImage(user.getUsername()));
            }else {
                user.setProfilePicture(new FileUpload().uploadImage(request.profileImage, user.getUsername()));
            }
            return ResponseEntity.ok().build();
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
            if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty()){
                return true;
            }
            return false;
        }
    }
}
