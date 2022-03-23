package com.example.videohosting.data.servicesImpl;

import com.example.videohosting.data.repos.LikeRepository;
import com.example.videohosting.data.repos.VideoRepository;
import com.example.videohosting.data.repos.ViewsRepository;
import com.example.videohosting.data.servicesImpl.service_exceptions.VideoNotFoundException;
import com.example.videohosting.models.Video;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VideosServiceImpl{
    private final VideoRepository videoRepository;
    private final LikeRepository likeRepository;
    private final ViewsRepository viewsRepository;

    public Video getVideo(UUID id){
        return videoRepository.getById(id);
    }
    public Long getLikes(Video id){
        return likeRepository.countByVideo(id);
    }
    public Long getViews(UUID id){
        return viewsRepository.countByVideoId(id);
    }
    public void saveVideo(Video video){
        videoRepository.save(video);
    }
    public Page<Video> getAllVideosForNonRegistredUsers(int page,int numberOnAPage){
        Pageable pageable1= PageRequest.of(page,numberOnAPage);
        return videoRepository.findAll(pageable1);
    }
}
