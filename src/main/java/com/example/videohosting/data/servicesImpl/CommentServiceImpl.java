package com.example.videohosting.data.servicesImpl;

import com.example.videohosting.data.repos.CommentRepository;
import com.example.videohosting.models.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl {
    private final CommentRepository commentRepository;
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }
}
