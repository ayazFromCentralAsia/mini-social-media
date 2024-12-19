package com.example.social.media.Service;

import com.example.social.media.Config.JWT.JWTService;
import com.example.social.media.Dto.Extra.SimpleResponse;
import com.example.social.media.Dto.Profile.ProfileRequest;
import com.example.social.media.Entities.User;
import com.example.social.media.Repository.ActivationCodeRepository;
import com.example.social.media.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileService {
    private final UserRepository userRepository;
    ModelMapper modelMapper = new ModelMapper();

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public SimpleResponse editProfile(ProfileRequest profileRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByName(username).orElseThrow(() -> new RuntimeException("User not found, ProfileService.editProfile"));
        if (profileRequest.getName() != null){
            user.setName(profileRequest.getName());
        }
        if (profileRequest.getBio() != null){
            user.setBio(profileRequest.getBio());
        }
        if (profileRequest.getEmail() != null){
            user.setEmail(profileRequest.getEmail());
        }
        if (profileRequest.getProfile_picture() != null){
            user.setProfile_picture(profileRequest.getProfile_picture());
        }
        userRepository.save(user);
        return SimpleResponse.builder().message("Profile updated successfully").status(HttpStatus.OK).build();
    }
}
