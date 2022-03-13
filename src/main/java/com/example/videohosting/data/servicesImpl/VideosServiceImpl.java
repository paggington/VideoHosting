package com.example.videohosting.data.servicesImpl;

import com.example.videohosting.data.repos.VideoRepository;
import com.example.videohosting.data.servicesImpl.service_exceptions.VideoNotFoundException;
import com.example.videohosting.models.Video;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VideosServiceImpl{
    private final VideoRepository videoRepository;

    public Video getVideo(UUID id){
        return videoRepository.getById(id);
    }
    public void saveVideo(Video video){
        videoRepository.save(video);
    }
}
