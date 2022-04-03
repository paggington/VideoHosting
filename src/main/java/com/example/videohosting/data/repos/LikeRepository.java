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
//    @Query("select count(e) from Likee e where e.dislikeSet=?1 and e.video=?2")
//    Long findByDislikeAndVideo(boolean dislikeSet,Video id);
    Long countAllByDislikeSetAndVideo(boolean dislikeSet,Video video);
    Likee findByUsernameAndVideo(String username,Video video);
}
