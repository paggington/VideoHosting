package com.example.videohosting.controllers;

import com.example.videohosting.data.servicesImpl.VideosServiceImpl;
import com.example.videohosting.data.servicesImpl.service_exceptions.VideoNotFoundException;
import com.example.videohosting.files.FileReader;
import com.example.videohosting.files.FileUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideosREST {
    private final VideosServiceImpl videosService;
    private FileReader fileReader=new FileReader();
    private FileUpload fileUpload=new FileUpload();
    @GetMapping
    @ResponseBody
    public ResponseEntity<?> getVideoByParams(@RequestParam("id") String id, HttpServletResponse response){
        ResponseEntity<byte[]> result = null;
        try {
            Path path = Path.of(videosService.getVideo(UUID.fromString(id)).getFilepath());
            Resource resource=new UrlResource(path.toUri());

            if(resource.exists() || resource.isReadable()){
                byte[] image = Files.readAllBytes(path);

                response.setStatus(HttpStatus.OK.value());
                HttpHeaders headers=new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentLength(image.length);
                return new ResponseEntity<byte[]>(image, headers, HttpStatus.OK);
            }else{
                throw new RuntimeException("Cannot read the file");
            }
        } catch (VideoNotFoundException e) {
            throw new RuntimeException("Cannot read the file");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
