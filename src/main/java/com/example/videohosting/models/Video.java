package com.example.videohosting.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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
    private String filepath;
    private String videoName;
    private String username;
    private String dateOfPublication;
    @Column(nullable = true)
    private String previewDirectory;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Likee> likes;
}
