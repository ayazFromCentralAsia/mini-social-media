package com.example.social.media.Api;


import com.example.social.media.Dto.Post.ScrollPostResponse;
import com.example.social.media.Service.ScrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scroll")
public class ScrollApi {
    private final ScrollService scrollService;

    @GetMapping("/scroll/{username}")
    @PreAuthorize("hasAuthority('USER')")
    public List<ScrollPostResponse> scroll(@PathVariable String username) {
        return scrollService.scrollUserPosts(username);
    }

    @GetMapping("/scroll")
    @PreAuthorize("hasAuthority('USER')")
    public List<ScrollPostResponse> scroll() {
        return scrollService.scrollAllPosts();
    }
}
