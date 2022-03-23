package com.example.videohosting.data.servicesImpl;

import com.example.videohosting.data.repos.RoleRepository;
import com.example.videohosting.data.repos.UserRepository;
import com.example.videohosting.data.repos.ViewsRepository;
import com.example.videohosting.models.Role;
import com.example.videohosting.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ViewsRepository viewsRepository;
    public void saveNewUser(User user){

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setNonLocked(true);
        user.addRole(new Role("ROLE_USER"));
        userRepository.save(user);
    }
    public List<User> getAll(){
        return userRepository.findAll();
    }
    public List<User.View> getViewsForUser(String username){
        return viewsRepository.findByUsername(username);
    }
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " not found!");
        } else {
            log.info(username + " found!");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
