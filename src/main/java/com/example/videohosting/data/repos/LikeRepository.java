package com.example.videohosting.data.repos;

import com.example.videohosting.models.Likee;
import com.example.videohosting.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Likee,Long> {
    Long countByVideo(Video id);
}
