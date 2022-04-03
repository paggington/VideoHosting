package com.example.videohosting.data.servicesImpl;

import com.example.videohosting.data.repos.LikeRepository;
import com.example.videohosting.models.Likee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeServiceImpl {
    private final LikeRepository likeRepository;

    public Likee saveNewLikeForVideo(Likee like){
        return likeRepository.save(like);
    }
}
