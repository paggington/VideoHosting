package com.example.videohosting.models;

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
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String dateOfRegistration;
    private String profilePicture;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id")
    private Set<Comment> comments;

    private boolean isEnabled;
    private boolean isNonLocked;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Collection<Role> roles=new ArrayList<>();

    public void addRole(Role role){
        this.roles.add(role);
    }
}
