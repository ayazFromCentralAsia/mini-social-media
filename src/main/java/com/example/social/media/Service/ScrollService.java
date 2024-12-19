package com.example.social.media.Service;


import com.example.social.media.Dto.Post.ScrollPostResponse;
import com.example.social.media.Entities.Post;
import com.example.social.media.Entities.User;
import com.example.social.media.Repository.PostRepository;
import com.example.social.media.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ScrollService {
    private final UserRepository userRepository;
    private final PostRepository postsRepository;

    public List<ScrollPostResponse> scrollUserPosts(String username) {
        User user = userRepository.findByName(username).orElseThrow(() -> new RuntimeException("User not found, ScollService.scrollUserPosts()"));
        List<Post> posts = postsRepository.findAllByUser_Id(user);
        List<ScrollPostResponse> scrollPostResponses = new ArrayList<>();
        for (Post post : posts) {
            ScrollPostResponse scrollPostResponse = new ScrollPostResponse();
            scrollPostResponse.setContent(post.getContent());
            scrollPostResponse.setCreated_at(post.getCreated_at());
            scrollPostResponse.setImage_url(post.getImage_url());
            scrollPostResponses.add(scrollPostResponse);
        }
        return scrollPostResponses;
    }

    public List<ScrollPostResponse> scrollAllPosts() {
        List<Post> posts = postsRepository.findAll();
        List<ScrollPostResponse> scrollPostResponses = new ArrayList<>();
        for (Post post: posts){
            ScrollPostResponse scrollPostResponse = new ScrollPostResponse();
            scrollPostResponse.setContent(post.getContent());
            scrollPostResponse.setCreated_at(post.getCreated_at());
            scrollPostResponse.setImage_url(post.getImage_url());
            scrollPostResponses.add(scrollPostResponse);
        }
        return scrollPostResponses;
    }
    // End of ScrollService.java (
}
