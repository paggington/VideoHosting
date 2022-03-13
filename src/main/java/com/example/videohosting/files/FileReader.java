package com.example.videohosting.files;

import com.example.videohosting.data.repos.VideoRepository;
import com.example.videohosting.data.servicesImpl.VideosServiceImpl;
import com.example.videohosting.data.servicesImpl.service_exceptions.VideoNotFoundException;
import com.example.videohosting.models.Video;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.UUID;


public class FileReader implements DIRECTORY {
    @Autowired
    private VideosServiceImpl videosService;
    private String filePath;

    public FileReader() {
    }

    public File getFileByItsNameAndAuthor(UUID id) throws VideoNotFoundException {
        Video video = videosService.getVideo(id);
        return new File(Path.of(video.getFilepath()).normalize().toAbsolutePath().toUri());
    }

    public Resource load(UUID id) throws MalformedURLException {
        Path path = Path.of(videosService.getVideo(id).getFilepath());
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Cannot read the file");
        }
    }
}
