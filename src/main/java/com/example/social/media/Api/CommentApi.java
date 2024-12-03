package com.example.social.media.Api;

import com.example.social.media.Dto.Comment.CommentRequestDto;
import com.example.social.media.Dto.Comment.CommentTextDto;
import com.example.social.media.Dto.Extra.SimpleResponse;
import com.example.social.media.Dto.Post.PostRequestDto;
import com.example.social.media.Dto.Post.PostResponseUserDto;
import com.example.social.media.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentApi {

    private final CommentService commentService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/addComment")
    public SimpleResponse changePost(@RequestBody CommentRequestDto commentRequestDto) {
        return commentService.addComment(commentRequestDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getAll/{id}")
    public List<CommentRequestDto> getAllByBlogId(@PathVariable("id")  UUID blogId) {
        return commentService.getAll(blogId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/changeComment/{id}")
    public SimpleResponse changeComment(@PathVariable("id") UUID id, @RequestBody CommentTextDto commentTextDto) {
        return commentService.changeCommentInfo(id, commentTextDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/delete/{id}")
    public SimpleResponse deletePost(@PathVariable("id") UUID id) {
        return commentService.deleteComment(id);
    }
}
