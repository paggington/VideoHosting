package com.example.videohosting.controllers;

import com.example.videohosting.data.servicesImpl.VideosServiceImpl;
import com.example.videohosting.data.servicesImpl.service_exceptions.VideoNotFoundException;
import com.example.videohosting.files.FileReader;
import com.example.videohosting.files.FileUpload;
import com.example.videohosting.video_sampling.VideoFrameExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.File;
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

    @Async
    public ResponseEntity<Resource> getVideoByParams(@RequestParam("id") String id){
        try {
            Path path = Path.of(videosService.getVideo(UUID.fromString(id)).getFilepath());
            Resource resource=new UrlResource(path.toUri());

            if(resource.exists() || resource.isReadable()){

                return ResponseEntity.ok()
                        .contentType(MediaTypeFactory
                                .getMediaType(resource)
                                .orElse(MediaType.APPLICATION_OCTET_STREAM))
                        .body(resource);
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
    @Async
    @GetMapping("/preview")
    public ResponseEntity<Resource> getPreview(@RequestParam("id")String id){
        try{
            Path path = Path.of(videosService.getVideo(UUID.fromString(id)).getFilepath());
            Resource resource=new VideoFrameExtractor().getFirstFrame(path);
            return ResponseEntity.ok().contentType(MediaTypeFactory
                    .getMediaType(resource)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .body(resource);
        } catch (VideoNotFoundException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
