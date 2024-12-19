package com.example.social.media.Dto.Profile;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRequest {
    private String email;
    private String password;
    private String name;
    private String bio;
    private String profile_picture;
}
