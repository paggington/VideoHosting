package com.example.videohosting.controllers;

import com.example.videohosting.data.servicesImpl.LikeServiceImpl;
import com.example.videohosting.data.servicesImpl.VideosServiceImpl;
import com.example.videohosting.models.Likee;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikesREST {
    private final LikeServiceImpl likeService;
    private final VideosServiceImpl videosService;
    @GetMapping("/set-like")
    public ResponseEntity<Likee> setLike(@RequestParam("id")String videoId){
        return ResponseEntity.ok(likeService.saveNewLikeForVideo(new Likee(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                videosService.getVideo(UUID.fromString(videoId)),
                false
        )));
    }
    @GetMapping("/set-dislike")
    public ResponseEntity<Likee> setDisike(@RequestParam("id")String videoId){
        return ResponseEntity.ok(likeService.saveNewLikeForVideo(new Likee(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                videosService.getVideo(UUID.fromString(videoId)),
                true
        )));
    }
    @Async
    @GetMapping("/all")
    public ResponseEntity<LikeDislikeStatForVideo> getAllLikesForAVideo(@RequestParam("id")String id){
        LikeDislikeStatForVideo likeDislikeStatForVideo=new LikeDislikeStatForVideo();
        Long likes=videosService.getLikes(videosService.getVideo(UUID.fromString(id)));
        Long dislikes= videosService.getDislikes(videosService.getVideo(UUID.fromString(id)));

        likeDislikeStatForVideo.setLike(likes);
        likeDislikeStatForVideo.setDislike(dislikes);
        return ResponseEntity.ok(likeDislikeStatForVideo);
    }
    @GetMapping("/get-user-like-stat-for-video")
    public ResponseEntity<Likee> getStatsForUser(@RequestParam("videoID")String videoID,
                                                                 @RequestParam("username")String username){
        return ResponseEntity.ok(videosService.getLikeSetByUser(username,videoID));
    }
    @Getter
    @Setter
    @RequiredArgsConstructor
    private class LikeDislikeStatForVideo{
        private Long dislike=0L;
        private Long like=0L;
    }
}

