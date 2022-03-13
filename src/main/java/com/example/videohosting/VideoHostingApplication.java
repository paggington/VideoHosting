package com.example.videohosting;

import com.example.videohosting.user_profile_pic_generator.ProfileImageGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VideoHostingApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoHostingApplication.class, args);
        new ProfileImageGenerator().generateImage();
    }

}
