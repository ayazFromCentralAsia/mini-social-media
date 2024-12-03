package com.example.social.media.Api;

import com.example.social.media.Dto.Extra.SimpleResponse;
import com.example.social.media.Service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikesApi {
    private final LikesService likesService;

    @PostMapping("/like/{postId}")
    public SimpleResponse likePost(@PathVariable UUID postId) {
        return likesService.likePost(postId);
    }

    @PostMapping("/unlike/{postId}")
    public SimpleResponse unlikePost(@PathVariable UUID postId) {
        return likesService.unlikePost(postId);
    }
}
