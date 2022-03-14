package com.example.videohosting.files;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUpload implements DIRECTORY{
    public String uploadImage(MultipartFile file,String username){
        Path filepath = Paths.get(DIR+"\\profile_images\\"+username+file.getContentType());
        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(file.getBytes());
            return DIR+"\\profile_images\\"+username+file.getContentType();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DIR+"\\profile_images\\asd.jpg";
    }
}
