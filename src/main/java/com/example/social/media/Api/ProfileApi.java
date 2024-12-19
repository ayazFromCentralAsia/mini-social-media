package com.example.social.media.Api;

import com.example.social.media.Dto.Extra.SimpleResponse;
import com.example.social.media.Dto.Profile.ProfileRequest;
import com.example.social.media.Service.MessageService;
import com.example.social.media.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileApi {

    private final ProfileService profileService;

    @PostMapping("/edit-profile")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse editProfile(@RequestBody ProfileRequest profileRequest) {
        return profileService.editProfile(profileRequest);
    }
}
