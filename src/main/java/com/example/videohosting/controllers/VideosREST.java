package com.example.videohosting.controllers;

import com.example.videohosting.data.servicesImpl.UserServiceImpl;
import com.example.videohosting.data.servicesImpl.VideosServiceImpl;
import com.example.videohosting.files.FileReader;
import com.example.videohosting.files.FileUpload;
import com.example.videohosting.models.Video;
import com.example.videohosting.video_sampling.VideoFrameExtractor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"}, methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class VideosREST {
    private final VideosServiceImpl videosService;
    private final UserServiceImpl userService;
    private FileReader fileReader = new FileReader();
    private FileUpload fileUpload = new FileUpload();
    @Autowired
    private HttpServletRequest request;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Async
    @GetMapping
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

    @PostMapping(value = "/video-new")
    public ResponseEntity<Video> saveNewVideo(@RequestParam(name = "username") String username,
                                              @RequestParam(name = "file") MultipartFile file,
                                              @RequestParam(name = "videoName") String videoName,
                                              @RequestParam(name = "videoDesc") String videoDesc) {
        Video video = new Video();
        video.setVideoName(videoName);
        video.setVideoDesc(videoDesc);
        video.setFilepath(saveNewVideo(file, username));
        video.setViews(0L);
        video.setDateOfPublication(LocalDateTime.now().format(dateTimeFormatter));
        video.setLikes(new HashSet<>());
        System.out.println(video.toString());
        videosService.saveVideo(video);
        return ResponseEntity.ok(video);
    }

    @GetMapping("/video")
    public ResponseEntity<Video> getVideoById(@RequestParam("id") String id) {
        System.out.println(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(videosService.getVideo(UUID.fromString(id)));
    }

    @Async
    @GetMapping("/search")
    public ResponseEntity<List<String>> getNamesForEnteredData(@RequestParam("data") String data) {
        return ResponseEntity.ok(new ArrayList<>());
    }
    @Async
    @PostMapping("/setView")
    public ResponseEntity<?> setViewForVideo(@RequestParam("id")String id){
        videosService.updateViews(UUID.fromString(id));
        return ResponseEntity.ok(id);
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

    private String saveNewVideo(MultipartFile file, String username) {
        String dir = "C:\\Users\\PAG\\Desktop\\VIDEOS\\videos\\";
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path fileStorage = get(dir, filename).toAbsolutePath().normalize();
        try {
            copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);//получаем входящий файл,место хранения,и удаляем,если такой уже существует!
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileStorage.toAbsolutePath().normalize().toString();
    }
}
