package com.example.videohosting.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "likes_hosting",schema = "video_host")
public class Likee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private String username;
    private boolean dislikeSet=false;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="video_id")
    @JsonIgnore
    private Video video;

    public Likee(String username, Video video,boolean dislikeSet) {
        this.username = username;
        this.video = video;
        this.dislikeSet=dislikeSet;
    }
}
