package com.example.videohosting.controllers;

import com.example.videohosting.data.servicesImpl.UserServiceImpl;
import com.example.videohosting.data.servicesImpl.VideosServiceImpl;
import com.example.videohosting.data.servicesImpl.service_exceptions.VideoNotFoundException;
import com.example.videohosting.files.FileReader;
import com.example.videohosting.files.FileUpload;
import com.example.videohosting.models.Likee;
import com.example.videohosting.models.User;
import com.example.videohosting.models.Video;
import com.example.videohosting.video_sampling.VideoFrameExtractor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideosREST {
    private final VideosServiceImpl videosService;
    private final UserServiceImpl userService;
    private FileReader fileReader = new FileReader();
    private FileUpload fileUpload = new FileUpload();

    @GetMapping

    @Async
    public ResponseEntity<Resource> getVideoByParams(@RequestParam("id") String id) {
        try {
            Path path = Path.of(videosService.getVideo(UUID.fromString(id)).getFilepath());
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {

                return ResponseEntity.ok()
                        .contentType(MediaTypeFactory
                                .getMediaType(resource)
                                .orElse(MediaType.APPLICATION_OCTET_STREAM))
                        .body(resource);
            } else {
                throw new RuntimeException("Cannot read the file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Async
    @GetMapping("/preview")
    public ResponseEntity<Resource> getPreview(@RequestParam("id") String id) {
        Video video = videosService.getVideo(UUID.fromString(id));
        String resourceLink = video.getPreviewDirectory();
        if (resourceLink == null || resourceLink.isEmpty()) {
            Path path = Path.of(videosService.getVideo(UUID.fromString(id)).getFilepath());
            Resource resource = new VideoFrameExtractor().getFirstFrame(path);
            try {
                video.setPreviewDirectory(String.valueOf(resource.getURL()));
                videosService.saveVideo(video);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok().contentType(MediaTypeFactory
                            .getMediaType(resource)
                            .orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .body(resource);
        } else {
            try {
                Resource resource = new UrlResource(video.getPreviewDirectory());
                return ResponseEntity.ok().contentType(MediaTypeFactory
                                .getMediaType(resource)
                                .orElse(MediaType.APPLICATION_OCTET_STREAM))
                        .body(resource);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Async
    @GetMapping("/get-all-new")
    public ResponseEntity<Page<Video>> getVideosForNonRegistered(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "6") int size
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(videosService.getAllVideosForNonRegistredUsers(page, size));
    }
    @Getter
    @Setter
    @AllArgsConstructor
    private static class VideoToSend {
        private UUID id = UUID.randomUUID();
        private String videoName;
        private String username;
        private String dateOfPublication;
        private Long likes;
        private Long views;
    }

    private static class SortByDate implements Comparator<VideoToSend> {
        @Override
        public int compare(VideoToSend o1, VideoToSend o2) {
            return o1.getDateOfPublication().compareTo(o2.getDateOfPublication());
        }
    }
}
