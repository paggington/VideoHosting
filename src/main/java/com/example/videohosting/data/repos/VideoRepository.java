package com.example.videohosting.data.repos;

import com.example.videohosting.models.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {

    Video findByUsernameAndVideoName(String username,String videoName);

}
