package com.example.videohosting.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "user_hosting",schema = "video_host")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    @JsonIgnore
    private String password;
    private String dateOfRegistration;
    private String profilePicture;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id")
    @JsonIgnore
    private Set<Comment> comments;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "view_id")
    private Set<View> viewed;

    private boolean isEnabled;

    private boolean isNonLocked;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Collection<Role> roles=new ArrayList<>();



    public void addRole(Role role){
        this.roles.add(role);
    }
    @Entity
    @Getter
    @Setter
    @RequiredArgsConstructor
    @Table(name = "views_hosting",schema = "video_host")
    public class View{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long view_id;
        private String videoId;
        private String username;
    }
}
