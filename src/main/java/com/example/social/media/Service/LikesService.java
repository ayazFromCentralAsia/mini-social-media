package com.example.social.media.Service;

import com.example.social.media.Dto.Extra.SimpleResponse;
import com.example.social.media.Entities.Likes;
import com.example.social.media.Entities.User;
import com.example.social.media.Repository.LikesRepository;
import com.example.social.media.Repository.PostRepository;
import com.example.social.media.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@RequiredArgsConstructor
@Service
public class LikesService {
    private final LikesRepository likerRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);


    public SimpleResponse likePost(UUID postId) {
        try {
            logger.info("Liking post with id: " + postId);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepository.findByName(username).orElseThrow(() -> new RuntimeException("User not found"));
            if (likerRepository.existsByPostIdAndUserId(postRepository.findById(postId).get(), user)){
                return SimpleResponse.builder().message("Post already liked").status(HttpStatus.BAD_REQUEST).build();
            }
            Likes likes = new Likes();
            likes.setPostId(postRepository.findById(postId).get());
            likes.setUserId(user);
            likerRepository.save(likes);
            logger.info("Post liked successfully");
            return SimpleResponse.builder().message("Post liked successfully").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return SimpleResponse.builder().message("Error liking post").status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public SimpleResponse unlikePost(UUID postId) {
        try {
            logger.info("Unliking post with id: " + postId);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepository.findByName(username).orElseThrow(() -> new RuntimeException("User not found"));
            Likes love = likerRepository.findByPostIdAndUserId(postRepository.findById(postId).get(), user);
            logger.info("Found like: " + love);
            if (love.getId() == null){
                return SimpleResponse.builder().message("Post not liked").status(HttpStatus.BAD_REQUEST).build();
            }
            likerRepository.delete(love);
            logger.info("Post unliked successfully");
            return SimpleResponse.builder().message("Post unliked successfully").status(HttpStatus.OK).build();
        }catch (Exception e) {
            return SimpleResponse.builder().message("Error unliking post").status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}