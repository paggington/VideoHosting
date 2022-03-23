package com.example.videohosting.controllers;

import com.example.videohosting.data.servicesImpl.UserServiceImpl;
import com.example.videohosting.files.FileUpload;
import com.example.videohosting.models.User;
import com.example.videohosting.user_profile_pic_generator.ProfileImageGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersREST {
    private final UserServiceImpl userService;
    private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    @PostMapping(value = "/new",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveNewUser(@RequestBody UserRequest request) {
        if(request.isOK()){
            User user=new User();
            user.setEmail(request.email);
            user.setName(request.name);
            user.setSurname(request.surname);
            user.setPassword(request.password);
            user.setUsername(request.username);
            user.setDateOfRegistration(dateTimeFormatter.format(LocalDateTime.now()));
            user.setProfilePicture(new ProfileImageGenerator().generateImage(user.getUsername()));
            userService.saveNewUser(user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok().body(userService.getAll());
    }
    @GetMapping("/user")
    public ResponseEntity<User> getUserByUsername(@RequestParam("username")String username){
        User user=userService.getUserByUsername(username);
        if(user!=null){
            return ResponseEntity.ok().body(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @Getter
    @Setter
    private static class UserRequest {
        private String name;
        private String surname;
        private String username;
        private String password;
        private String email;

        public boolean isOK() {
            if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty()){
                return true;
            }
            return false;
        }
    }
}
