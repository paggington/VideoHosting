package com.example.videohosting.data.repos;

import com.example.videohosting.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ViewsRepository extends JpaRepository<User.View,Long> {
    List<User.View> findByUsername(String username);

    Long countByVideoId(UUID id);
}
