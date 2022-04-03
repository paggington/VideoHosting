package com.example.videohosting.data.servicesImpl;

import com.example.videohosting.data.repos.LikeRepository;
import com.example.videohosting.data.repos.VideoRepository;
import com.example.videohosting.data.repos.ViewsRepository;
import com.example.videohosting.data.servicesImpl.service_exceptions.VideoNotFoundException;
import com.example.videohosting.models.Likee;
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
        return videoRepository.findById(id).get();
    }
    public Long getLikes(Video video){
        return likeRepository.countByVideo(video);
    }
    public Long getDislikes(Video video){
        return likeRepository.countAllByDislikeSetAndVideo(true,video);
    }
    public Long getViews(UUID id){
        return viewsRepository.countByVideoId(id);
    }
    public Video saveVideo(Video video){
        return videoRepository.save(video);
    }
    public void updateViews(UUID videoId){
        Video video=videoRepository.findById(videoId).get();
        video.setViews(video.getViews()+1);
        videoRepository.save(video);
    }
    public Page<Video> getAllVideosForNonRegistredUsers(int page,int numberOnAPage){
        Pageable pageable1= PageRequest.of(page,numberOnAPage);
        return videoRepository.findAll(pageable1);
    }
    public Likee getLikeSetByUser(String username,String videoID){
        return likeRepository.findByUsernameAndVideo(username,getVideo(UUID.fromString(videoID)));
    }
}
