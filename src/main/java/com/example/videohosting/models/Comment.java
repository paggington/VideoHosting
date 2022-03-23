package com.example.videohosting.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "comments_hosting",schema = "video_host")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private Long likes;
    private String dateOfPublication;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Video video;
}
