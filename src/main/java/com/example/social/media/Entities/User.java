package com.example.social.media.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String email;
    private String password;
    private String name;
    private String bio;
    private String profile_picture;
    private Boolean is_active;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.<GrantedAuthority>of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getUsername() {
        return this.name;
    }
}
