package com.example.videohosting.data.repos;

import com.example.videohosting.models.Likee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likee,Long> {
}
