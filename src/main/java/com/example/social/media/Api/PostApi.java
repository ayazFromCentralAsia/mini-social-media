package com.example.social.media.Api;

import com.example.social.media.Dto.Extra.SimpleResponse;
import com.example.social.media.Dto.Post.PostRequestDto;
import com.example.social.media.Dto.Post.PostResponseUserDto;
import com.example.social.media.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostApi {
    private final PostService postService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/create")
    public SimpleResponse createPost(@RequestBody PostRequestDto postRequest) {
        return postService.createPost(postRequest);
    }


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getAll")
    public List<PostResponseUserDto> getAll() {
        return postService.getAll();
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/changePost/{id}")
    public SimpleResponse changePost(@PathVariable("id") UUID id, @RequestBody PostRequestDto postRequestDto) {
        return postService.changePostInfo(id, postRequestDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/delete/{id}")
    public SimpleResponse deletePost(@PathVariable("id") UUID id) {
        return postService.deletePost(id);
    }
}
