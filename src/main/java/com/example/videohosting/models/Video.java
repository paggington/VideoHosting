package com.example.videohosting.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "videos_hosting",schema = "video_host")
public class Video {
    @Id
    private UUID id=UUID.randomUUID();
    @JsonIgnore
    private String filepath;
    private String videoName;
    private String username;
    private String dateOfPublication;
    @Column(nullable = true)
    @JsonIgnore
    private String previewDirectory;
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Likee> likes;
    private Long views=0L;
}
//TODO:make APi to return evrth ex set Likes