package com.example.videohosting.controllers;

import com.example.videohosting.data.servicesImpl.CommentServiceImpl;
import com.example.videohosting.data.servicesImpl.VideosServiceImpl;
import com.example.videohosting.models.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentsREST {
    private final VideosServiceImpl videosService;
    private final CommentServiceImpl commentService;
    private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    @PostMapping("/add")
    public ResponseEntity<?> addCommentToAVideo(@RequestParam("username")String username,
                                                @RequestParam("video_id")String videoId,
                                                @RequestBody TextMessage textMessage)
    {
        Comment comment=new Comment();
        try {
            comment.setVideo(videosService.getVideo(UUID.fromString(videoId)));
            comment.setUsername(username);
            comment.setLikes(0L);
            comment.setDateOfPublication(dateTimeFormatter.format(LocalDateTime.now()));
            commentService.saveComment(comment);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Getter
    @Setter
    private static class TextMessage{
        private String text;
    }
}
